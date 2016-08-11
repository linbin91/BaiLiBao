package com.bailibao.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class ProductBuyProtocol extends BaseActivity implements IGetDataView {

    @InjectView(R.id.title_left)
    ImageView titleLeft;
    @InjectView(R.id.about_rl_tilte)
    RelativeLayout aboutRlTilte;
    @InjectView(R.id.safety_title)
    RelativeLayout safetyTitle;
    @InjectView(R.id.safe_intruction_next_btn)
    TextView safeIntructionNextBtn;
    @InjectView(R.id.safe_intruction_ctv)
    CheckBox safeIntructionCtv;
    @InjectView(R.id.iv_loading)
    ImageView ivLoading;
    @InjectView(R.id.loading_layout)
    LinearLayout loadingLayout;
    @InjectView(R.id.myWebview)
    WebView mWebView;
    private int productId;
    private String path;
    private int buyCount;

    @Override
    protected void initData() {
        productId = getIntent().getIntExtra("productId", 0);
        path = getIntent().getStringExtra("path");
        buyCount = getIntent().getIntExtra("buyCount",0);
//        ViewPresenter presenter = new ViewPresenter(this);
//        UrlParse parse = new UrlParse(HttpURLData.APPFUN_PRODUCT_AGREEMENT);
//        parse.putValue("id", productId);
//        presenter.getNetData(parse.toString());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(path);
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
    }

    @Override
    protected void setListener() {
        titleLeft.setOnClickListener(this);
        safeIntructionNextBtn.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_safety_intruction);
        ButterKnife.inject(this);
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

            Intent intent = new Intent(this,ProductBuyState.class);
            intent.putExtra("productId",productId);
            intent.putExtra("buyCount",buyCount);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(mContext, "请同意协议后再进行购买", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void fillView(String content) {
//        if (content != null && !content.isEmpty()) {
//            tvContent.setText(content);
//        }
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

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.inject(this);
//    }
}
