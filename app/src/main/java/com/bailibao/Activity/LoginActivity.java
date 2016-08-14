package com.bailibao.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.user.UserRegisterBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.ResponseData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.Base64Util;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.StringUtil;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/13.
 */
public class LoginActivity extends BaseActivity implements IGetDataView{


    private EditText mEditNum;
    private EditText mEditPassword;
    private TextView mLogin;
    private TextView mForgetPassword;
    private ImageView mIvClose;
    private TextView mTvRegister;

    //点击后的操作
    private  int mNextStep;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private int id;
    @Override
    protected void initData() {
        mNextStep = getIntent().getIntExtra("donext",0);
        id = getIntent().getIntExtra("id",0);
    }

    @Override
    protected void setListener() {
        mLogin.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_login);
        mEditNum = (EditText) findViewById(R.id.login_activity_phone_et);
        mEditPassword = (EditText) findViewById(R.id.login_activity_pw_et);
        mLogin = (TextView) findViewById(R.id.login_activity_login_tv);
        mForgetPassword = (TextView) findViewById(R.id.login_activity_fogetpw_tv);
        mIvClose = (ImageView) findViewById(R.id.title_left);
        mTvRegister = (TextView) findViewById(R.id.title_right);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_activity_login_tv:
                //判断下手机号码是否填写正确
                doLoginAction();
                break;
            case R.id.login_activity_fogetpw_tv:
                doGetPassword();
                break;
            case R.id.title_left:
                //去掉已登录状态
                PreferencesUtils.putBoolean(mContext,ConfigsetData.CONFIG_KEY_LOGIN,false);
                finish();
                break;
            case R.id.title_right:
                //跳出用户的协议
                toRegisterActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 登录按钮点击事件
     */
    private void doLoginAction() {
        String phoneNum = mEditNum.getText().toString().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        if (phoneNum == null || password == null || password.isEmpty() || phoneNum.isEmpty()){
            return;
        }else {
            if (StringUtil.isMobileNum(phoneNum)){
                String url = HttpURLData.APPFUN_USER_LOGIN;
                UrlParse parse = new UrlParse(url);
                parse.putValue("phone",phoneNum);
                parse.putValue("password", Base64Util.encodeAndMD5(password));

                ViewPresenter presenter = new ViewPresenter(this,this);
                presenter.postNetData(parse.toString());
            }else{
                Toast.makeText(mContext,"手机号格式不对",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 调转到注册的界面
     */
    private void toRegisterActivity() {

        Intent intent = new Intent(mContext,RegisterAgreementActivity.class);
        startActivity(intent);
    }

    /**
     * 忘记密码 事件
     */
    private void doGetPassword() {
        String phoneNum = mEditNum.getText().toString().toString().trim();
        if (phoneNum == null || phoneNum.isEmpty() || !StringUtil.isMobileNum(phoneNum)){
            Toast.makeText(mContext,"手机号格式不对",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(mContext,NewEditPassword.class);
            intent.putExtra("phoneNum",phoneNum);
            startActivity(intent);
        }
    }

    @Override
    public void fillView(String content) {

        if (content != null) {
            Gson gson = new Gson();
            UserRegisterBean bean = gson.fromJson(content, UserRegisterBean.class);
            if (bean != null) {
                if (bean.respCode == ResponseData.RESP_CODE_OK) {
                    PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_USER_UID, bean.uid);
                    PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_USER_TOKEN, Base64Util.decode(bean.token));
                    PreferencesUtils.putBoolean(mContext, ConfigsetData.CONFIG_KEY_LOGIN,true);
                    PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_LOGIN_NUM, mEditNum.getText().toString().trim());
                    String auth = "uid="+bean.uid+"|"+"token="+Base64Util.decode(bean.token);
                    PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_AUTH,Base64Util.encode(auth));

                    if (mNextStep == ConfigsetData.LOGIN_TO_BUY){
//                        Intent intent = new Intent(mContext,ProductBuyProtocol.class);
//                        intent.putExtra("productId",id);
//                        startActivity(intent);

                        Intent intent = new Intent(mContext,ProductBuyActivity.class);
                        intent.putExtra("productId",id);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        llLoading.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        llLoading.setVisibility(View.GONE);
    }
}
