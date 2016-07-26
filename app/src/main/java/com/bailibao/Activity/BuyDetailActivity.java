package com.bailibao.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.InvestDetailBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

/**
 * 在投资金中的item点击进来的每项详情
 * Created by Administrator on 2016/4/19.
 */
public class BuyDetailActivity extends BaseActivity implements IGetDataView {

    private ImageView mClose;
    private TextView mRedemation;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvEarning;
    private TextView tvShare;
    private TextView tvAllEarning;
    private TextView tvTitlte;
    private InvestDetailBean bean;

    @Override
    protected void initData() {
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        int id = getIntent().getIntExtra("id",0);
        String name = getIntent().getStringExtra("name");
        String url = HttpURLData.APPFUN_ACCOUNT_INVEST;
        UrlParse parse = new UrlParse(url);
        parse.putValue("id",id);
        ViewPresenter presenter = new ViewPresenter(this);
        presenter.getNetDataWithAuth(parse.toString(),auth);
        tvTitlte.setText(name);
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mRedemation.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_buy_detail);
        mClose = (ImageView) findViewById(R.id.title_left);
        mRedemation = (TextView) findViewById(R.id.huoqiu_detail_redemation);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvEarning = (TextView) findViewById(R.id.all_earnings);
        tvShare = (TextView) findViewById(R.id.amount);
        tvAllEarning = (TextView) findViewById(R.id.earnings);
        tvTitlte = (TextView) findViewById(R.id.title_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.huoqiu_detail_redemation:
                toRedeemActivity();
                break;
            default:
                break;
        }
    }

    private void toRedeemActivity() {
        if (bean != null && bean.respCode == 0){
            Intent intent = new Intent(mContext, RedeemActivity.class);
            intent.putExtra("count",bean.count);
            intent.putExtra("productId",bean.orderId);
            startActivity(intent);
        }

    }


    @Override
    public void fillView(String content) {
        if (content != null && !content.isEmpty()){
            Gson gson = new Gson();
            bean = gson.fromJson(content, InvestDetailBean.class);
            if (bean != null){
                tvEarning.setText("" + bean.profit);
                tvShare.setText("" + bean.count);
                tvAllEarning.setText("" + bean.totalProfit);
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
}
