package com.bailibao.module.presenter;

import android.content.Context;
import android.content.Intent;

import com.bailibao.Activity.LoginActivity;
import com.bailibao.base.BasePresenterImpl;
import com.bailibao.bean.base.BaseBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.module.model.GetNetData;
import com.bailibao.module.model.IGetNetData;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by lin on 2016/4/10.
 */
public class ViewPresenter extends BasePresenterImpl<IGetDataView,String>{

    private IGetNetData mTool;
    private String mAuth;

    public ViewPresenter(IGetDataView view, Context context) {
        super(view,context);
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
                if (bean.respCode == 114 && bean.respMsg.equals("对不起，您的账号可能在其他设备上登陆过，请重新登陆")){
                    //清楚登录的信息
                    clearConfigData();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除登录的信息
     */
    private void clearConfigData() {
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_LOGIN);
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_LOGIN_NUM);
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_HAND_PASSWORD);
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_AUTH);
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_AUTHEN_NAME);
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_USER_UID);
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_USER_TOKEN);
        PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_AUTH);

//        PreferencesUtils.cleaConifg(mContext);
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
