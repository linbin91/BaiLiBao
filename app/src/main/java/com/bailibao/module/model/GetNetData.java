package com.bailibao.module.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * Created by lin on 2016/4/4.
 */
public class GetNetData implements IGetNetData<String> {

    //Get请求的方法
    @Override
    public void requestNetData(Callback<String> callback, String url) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    //post请求方法
    @Override
    public void postNetData(Callback<String> callback, String url) {
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);
    }

    //Get + 头部
    @Override
    public void getNetDataWithAuth(Callback<String> callback, String url, String auth) {
        OkHttpUtils.get()
                .addHeader("auth",auth)
                .url(url)
                .build()
                .execute(callback);
    }

    //post + 头部
    @Override
    public void postNetDataWithAuth(Callback<String> callback, String url, String auth) {

        OkHttpUtils.post()
                .addHeader("auth",auth)
                .url(url)
                .build()
                .execute(callback);
    }

}
