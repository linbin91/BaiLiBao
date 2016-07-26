package com.bailibao.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.user.WithdrawBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.Base64Util;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/7/27.
 * 提现界面
 */

public class WithdrawActivity extends BaseActivity implements IGetDataView {


    @InjectView(R.id.title_right)
    ImageView titleRight;
    @InjectView(R.id.safety_title)
    RelativeLayout safetyTitle;
    @InjectView(R.id.tv_accont)
    TextView tvAccont;
    @InjectView(R.id.et_money)
    EditText etMoney;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.tv_withdraw)
    TextView tvWithdraw;
    @InjectView(R.id.iv_loading)
    ImageView ivLoading;
    @InjectView(R.id.loading_layout)
    LinearLayout loadingLayout;

    private ViewPresenter mPresenter;
    @Override
    protected void initData() {
        tvAccont.setText(getIntent().getStringExtra("account"));
    }

    @Override
    protected void setListener() {
        titleRight.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_withdraw);
        ButterKnife.inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:
                finish();
                break;
            case R.id.tv_withdraw:
                doWithdrawAction();
                break;
            default:
                break;
        }
    }

    /**
     * 执行提现的操作
     */
    private void doWithdrawAction() {
        String money = etMoney.getText().toString();
        String password = etPassword.getText().toString();
        if (money != null && !money.isEmpty()) {
            if (password != null && !password.isEmpty()) {
                request(money, password);
            } else {
                Toast.makeText(this, "请输入交易密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求的操作
     * @param money
     * @param password
     */
    private void request(String money, String password) {
        if (mPresenter == null){
            mPresenter = new ViewPresenter(this);
        }
        String url = HttpURLData.APPFUN_MONEY_WITHDRAW;
        UrlParse parse = new UrlParse(url);
        parse.putValue("money",Integer.parseInt(money) * 100);
        parse.putValue("password", Base64Util.encodeAndMD5(password));
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        mPresenter.postNetDataWithAuth(parse.toString(), auth);
    }


    @Override
    public void fillView(String content) {
        if (content != null && !content.isEmpty()){
            Gson gson = new Gson();
            WithdrawBean bean = gson.fromJson(content,WithdrawBean.class);
            if (bean != null){
                Intent intent = new Intent(this,WithdrawResultActivity.class);
                intent.putExtra("money",bean.money);
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
        loadingLayout.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        loadingLayout.setVisibility(View.GONE);
    }
}
