package com.bailibao.calllback;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lin on 2016/4/4.
 */
public class GetMainInfo extends Callback<String>{
    @Override
    public String parseNetworkResponse(Response response) throws Exception {
        return null;
    }

    @Override
    public void onError(Call call, Exception e) {

    }

    @Override
    public void onResponse(String response) {

    }
}
