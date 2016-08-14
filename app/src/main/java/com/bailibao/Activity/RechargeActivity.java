package com.bailibao.Activity;

import android.content.Intent;
import android.text.TextUtils;
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
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.StringUtil;
import com.bailibao.util.UrlParse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/15.
 */
public class RechargeActivity extends BaseActivity implements IGetDataView {

    private ImageView mClose;
    private TextView tvRecharge;
    private ViewPresenter mPresenter;
    private EditText etInputMoney;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    @Override
    protected void initData() {
        mClose.setOnClickListener(this);
        tvRecharge.setOnClickListener(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_recharge);
        mClose = (ImageView) findViewById(R.id.title_right);
        tvRecharge = (TextView) findViewById(R.id.tv_recharge);
        etInputMoney = (EditText) findViewById(R.id.input_money);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:
                finish();
                break;
            case R.id.tv_recharge:
                doRechargeAction();
                break;
            default:
                break;
        }
    }

    /**
     * 进入充值的界面
     */
    private void doRechargeAction() {
        String money = etInputMoney.getText().toString().trim();
        if (money != null && !money.isEmpty()){
            if (mPresenter == null){
                mPresenter = new ViewPresenter(this,this);
            }

            String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
            if (auth != null && !TextUtils.isEmpty(auth)) {
                UrlParse parse = new UrlParse(HttpURLData.APPFUN_MONEY_PAY);
                parse.putValue("amt",Integer.parseInt(money) * 100);
                if (mPresenter == null) {
                    mPresenter = new ViewPresenter(this,this);
                }
                mPresenter.getNetDataWithAuth(parse.toString(), auth);
            }
        }else{
            Toast.makeText(this,"请输入本次充值金额",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void fillView(String content){
        if (content != null){
            ObjectMapper objectMapper = new ObjectMapper();
            try {

                Map<String,String> map = objectMapper.readValue(content,Map.class);

                gotoWebView(map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到富友的h5
     *
     * @param map
     */
    private void gotoWebView(Map map) {
        if (map != null){
            String postData = StringUtil.makePostHTML(HttpURLData.JZH_API_APP_500001_URL,map);
            if (postData != null){
                Intent intent = new Intent(this,FuiouWebviewActivity.class);
                intent.putExtra(HttpURLData.INTENT_API_DATA_KEY_DATA, postData);
                startActivity(intent);
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
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
