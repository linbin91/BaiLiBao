package com.bailibao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bailibao.R;
import com.bailibao.data.HttpURLData;

/**
 * Created by Administrator on 2016/7/10.
 */
public class FuiouWebviewActivity  extends AppCompatActivity {

    private  WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuiou_webview);
        initView();
        initData();
    }

    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        String postData = intent.getStringExtra(HttpURLData.INTENT_API_DATA_KEY_DATA);
        webView.loadData(postData, "text/html", "UTF-8");
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview);
    }
}
