package com.bailibao.Activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;

/**
 * Created by Administrator on 2016/5/16.
 */
public class WebViewActivity extends BaseActivity {

    private ImageView tvClose;
    private TextView tvTitle;
    private WebView mWebView;


    @Override
    protected void initData() {
        String title = getIntent().getStringExtra("title");
        String path = getIntent().getStringExtra("path");
        tvTitle.setText(title);
        mWebView.loadUrl(path);
    }

    @Override
    protected void setListener() {
        tvClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_webview);
        tvTitle = (TextView) findViewById(R.id.title_content);
        tvClose = (ImageView) findViewById(R.id.title_left);
        mWebView = (WebView) findViewById(R.id.webView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            default:
                break;
        }
    }
}
