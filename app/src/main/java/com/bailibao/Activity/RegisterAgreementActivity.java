package com.bailibao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.module.view.IGetDataView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/7/30.
 */
public class RegisterAgreementActivity extends BaseActivity implements IGetDataView {


    @InjectView(R.id.title_left)
    ImageView titleLeft;

    @InjectView(R.id.safe_intruction_next_btn)
    TextView safeIntructionNextBtn;
    @InjectView(R.id.safe_intruction_ctv)
    CheckBox safeIntructionCtv;
    @InjectView(R.id.safe_intruction_agree_tv)
    TextView safeIntructionAgreeTv;
    @InjectView(R.id.iv_loading)
    ImageView ivLoading;
    @InjectView(R.id.loading_layout)
    LinearLayout loadingLayout;
    @InjectView(R.id.webView)
    WebView webView;

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        titleLeft.setOnClickListener(this);
        safeIntructionNextBtn.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.register_agreement);
        ButterKnife.inject(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("http://139.196.173.191:42000/blb-api/user/agreement.jsp");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.safe_intruction_next_btn:
                doNextAction();
                break;
            default:
                break;
        }
    }

    /**
     * 点击下一步的操作
     */
    private void doNextAction() {
        if (safeIntructionCtv.isChecked()) {
            Intent intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(mContext, "请同意协议后再进行注册", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void fillView(String content) {

    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        loadingLayout.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
