package com.bailibao.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.product.RedeemBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.dialog.RedeemDialog;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.Base64Util;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/20.
 * 赎回的界面
 */
public class RedeemActivity extends BaseActivity implements IGetDataView, RedeemDialog.OnContinueClickListener {

    @InjectView(R.id.et_acccount)
    EditText etAcccount;
    @InjectView(R.id.et_password)
    EditText etPassword;
    TextView tvShare;
    private ImageView mClose;
    private TextView mRedeemButton;
    private ViewPresenter mPresenter;
    private int mCount;
    private int mProductId;
    private LinearLayout llLoading;
    private ImageView ivLoading;

    @Override
    protected void initData() {
        mCount = getIntent().getIntExtra("count", 0);
        tvShare.setText(mCount+"");
        mProductId = getIntent().getIntExtra("productId", 0);
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mRedeemButton.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_redeem);
        ButterKnife.inject(this);
        mClose = (ImageView) findViewById(R.id.title_right);
        mRedeemButton = (TextView) findViewById(R.id.redemption_activity_shuhui_btn);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvShare = (TextView) findViewById(R.id.tv_share);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                finish();
                break;
            case R.id.redemption_activity_shuhui_btn:
                doRedeemAction();
                break;
            default:
                break;
        }
    }

    /**
     * 点击赎回后的操作
     */
    private void doRedeemAction() {
        String account = etAcccount.getText().toString();
        String password = etPassword.getText().toString();
        if (account != null && !account.isEmpty()) {
            if (password != null && !password.isEmpty()) {
                RedeemDialog dialog = new RedeemDialog();
                dialog.setListener(this);
                dialog.show(getSupportFragmentManager(), "");
            } else {
                Toast.makeText(this, "请输入交易密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入要赎回份额", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void fillView(String content) {
        if (content != null && !content.isEmpty()){
            Gson gson = new Gson();
            RedeemBean bean = gson.fromJson(content,RedeemBean.class);
            if (bean != null){
                Intent intent = new Intent(this,RedemptionResultActivity.class);
                intent.putExtra("redeemMoney",bean.redeemMoney);
                startActivity(intent);
            }

        }

    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        llLoading.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        llLoading.setVisibility(View.GONE);
    }


    @Override
    public void doContinueAction() {
        //去请求
        if (mPresenter == null) {
            mPresenter = new ViewPresenter(this);
        }
        String url = HttpURLData.APPFUN_PRODUCT_REDEEM;
        UrlParse parse = new UrlParse(url);
        parse.putValue("orderId",mProductId);
        parse.putValue("count",Integer.parseInt(etAcccount.getText().toString()));
        parse.putValue("password", Base64Util.encodeAndMD5(etPassword.getText().toString()));

        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        mPresenter.postNetDataWithAuth(parse.toString(), auth);
    }
}
