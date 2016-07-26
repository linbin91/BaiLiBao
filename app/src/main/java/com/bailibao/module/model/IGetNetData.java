package com.bailibao.module.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * Created by lin on 2016/4/4.
 */
public interface IGetNetData<T> {
    public  void  requestNetData(Callback<T> callback, String url);
    public  void  postNetData(Callback<T> callback, String url);
    public  void getNetDataWithAuth(Callback<T> callback, String url,String auth);
    public  void postNetDataWithAuth(Callback<T> callback, String url,String auth);
}
