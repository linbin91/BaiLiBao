package com.bailibao.module.presenter;

import com.bailibao.base.BasePresenterImpl;
import com.bailibao.bean.base.BaseBean;
import com.bailibao.module.model.GetNetData;
import com.bailibao.module.model.IGetNetData;
import com.bailibao.module.view.IGetDataView;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by lin on 2016/4/10.
 */
public class ViewPresenter extends BasePresenterImpl<IGetDataView,String>{

    private IGetNetData mTool;
    private String mAuth;

    public ViewPresenter(IGetDataView view) {
        super(view);
        mTool = new GetNetData();
    }

    @Override
    public void onResponse(String response) {
        try {
            Gson gson = new Gson();
            BaseBean bean = gson.fromJson(response,BaseBean.class);
            if (bean.respCode == 0){
                mView.fillView(response);
            }else{
                mView.toast(bean.respMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getNetData(String url){
        mTool.requestNetData(this, url);
    }

    public void postNetData(String url){
        mTool.postNetData(this,url);
    }

    public void getNetDataWithAuth(String url, String auth){
        mTool.getNetDataWithAuth(this,url,auth);
    }

    public void postNetDataWithAuth(String url, String auth){
        mTool.postNetDataWithAuth(this,url,auth);
    }
}
