package com.bailibao.base;

import android.content.Context;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ClassName: BasePresenterBasePresenterImpl<p>
 * Author:oubowu<p>
 * Fuction: 代理的基类实现<p>
 * CreateDate:2016/2/14 1:45<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public abstract  class BasePresenterImpl<T extends  BaseView,V> extends  Callback<String> implements  BasePresenter {

    protected  T mView;
    protected Context mContext;
    protected  BasePresenterImpl(T view,Context context){
        mContext = context;
        mView = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public String parseNetworkResponse(Response response) throws Exception {

        String content = response.body().string();
        return content;
    }

    @Override
    public void onError(Call call, Exception e) {
        mView.hideProgress();
    }

    @Override
    public void onBefore(Request request) {
        super.onBefore(request);
//        mView.toast(request.toString());
        mView.showProgress();
    }

    @Override
    public void onAfter() {
        super.onAfter();
        mView.hideProgress();
    }
    public abstract void onResponse(String response);
}
