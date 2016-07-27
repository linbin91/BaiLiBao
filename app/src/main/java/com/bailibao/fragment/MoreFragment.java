package com.bailibao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.Activity.AboutActivity;
import com.bailibao.Activity.AuthenActivity;
import com.bailibao.Activity.NewEditPassword;
import com.bailibao.Activity.NewEditPhone;
import com.bailibao.Activity.SetLockActivity;
import com.bailibao.R;
import com.bailibao.base.BaseFragment;
import com.bailibao.bean.base.BaseBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.ResponseData;
import com.bailibao.dialog.InputPasswordDialog;
import com.bailibao.dialog.LoginDialog;
import com.bailibao.dialog.LoginOutDialog;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.Base64Util;
import com.bailibao.util.LockUtil;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.StringUtil;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

/**
 * Created by lin on 2016/4/2.
 */
public class MoreFragment extends BaseFragment implements IGetDataView{

    private  static  MoreFragment mInstance;
    private LinearLayout mAuthenticationNameLayout;
    private LinearLayout mBangdingphoneLayout;
    private LinearLayout mLoginpasswordLayout;
    private LinearLayout mDealPasswordLayout;
    private LinearLayout mModifyPasswordLayout;
    private LinearLayout mAboutLayout;
    private LinearLayout mLoginoutLayout;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView mPhoneText;
    private TextView mAuthenText;
    private CheckBox mGestureToggle;
//    //用来判断时候登入过了，特别是登入后，回来，账号要显示出来
//    private boolean mIsLogin;
    //用来判断是否开启了手势密码
//    private boolean mIsOpenGesture;

    private Toolbar toolbar;
    private int type;
    private ViewPresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(mContext, R.layout.fragment_more, null);

        findView();
        setListener();

//        mIsLogin = PreferencesUtils.getBoolean(mContext,ConfigsetData.CONFIG_KEY_LOGIN);
//        mIsOpenGesture = PreferencesUtils.getBoolean(mContext,ConfigsetData.CONFIG_KEY_OPEN_GESTURE);
//        if (mIsOpenGesture){
//            mGestureToggle.setBackgroundResource(R.mipmap.toggle_button_on);
//        }

        mPresenter = new ViewPresenter(this);
    }

    private void findView() {
        mAuthenticationNameLayout = (LinearLayout) mView.findViewById(R.id.more_frame_authentication_name_ll);
        mBangdingphoneLayout = (LinearLayout) mView.findViewById(R.id.more_frame_bangding_phone_ll);
        mLoginpasswordLayout = (LinearLayout) mView.findViewById(R.id.more_frame_login_password_ll);
        mDealPasswordLayout = (LinearLayout) mView.findViewById(R.id.more_frame_deal_password_ll);
        mModifyPasswordLayout = (LinearLayout) mView.findViewById(R.id.more_frame_deal_handlepw_ll);
        mAboutLayout = (LinearLayout) mView.findViewById(R.id.more_frame_about_ll);
        mLoginoutLayout = (LinearLayout) mView.findViewById(R.id.more_frame_loginout_ll);
        mPhoneText = (TextView) mView.findViewById(R.id.more_frame_bangding_phone_tv);
        mGestureToggle = (CheckBox) mView.findViewById(R.id.more_frame_toggle_btn);
        mAuthenText = (TextView) mView.findViewById(R.id.more_frame_authentication_name_tv);
        llLoading = (LinearLayout) mView.findViewById(R.id.loading_layout);
        ivLoading = (ImageView) mView.findViewById(R.id.iv_loading);
    }

    public  static  MoreFragment getInstance(){
        if (mInstance == null){
            mInstance = new MoreFragment();
        }

        return  mInstance;
    }

    @Override
    public void onDestroy() {
        mInstance = null;
        super.onDestroy();

    }

    private void setListener() {
        mAuthenticationNameLayout.setOnClickListener(this);
        mBangdingphoneLayout.setOnClickListener(this);
        mLoginpasswordLayout.setOnClickListener(this);
        mDealPasswordLayout.setOnClickListener(this);
        mModifyPasswordLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mGestureToggle.setOnClickListener(this);
        mLoginoutLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                ConfigsetData.CONFIG_KEY_LOGIN);
        switch (v.getId()) {
            case R.id.more_frame_authentication_name_ll:
                doAutherAction(isLogin);
                break;
            case R.id.more_frame_loginout_ll:
                doOutAction();
                break;
            case R.id.more_frame_bangding_phone_ll:
                doBingdingAction(isLogin);
                break;
            case R.id.more_frame_about_ll:
                toAboutActivity();
                break;
            case R.id.more_frame_toggle_btn:
                toSetHandLock(isLogin);
                break;
            case R.id.more_frame_deal_handlepw_ll:
                toModifyLock();
                break;
            case R.id.more_frame_login_password_ll:
                doLoginAction(isLogin);
                break;
            case R.id.more_frame_deal_password_ll:
                doDealAction(isLogin);
                break;
            default:
                break;
        }
    }

    /**
     * 退出的操作
     */
    private void doOutAction() {
        LoginOutDialog dialog = new LoginOutDialog();
        dialog.setListener(new LoginOutDialog.OnClickCallBack() {
            @Override
            public void loginout() {
                doLoginouAction();
            }
        });

        dialog.show(getFragmentManager(),"");
    }


    /**
     * 一系列退出的操作
     */
    private void doLoginouAction() {

        //退出的接口
        String url = HttpURLData.APPFUN_LOGOUT;
        String auth = PreferencesUtils.getString(mContext,ConfigsetData.CONFIG_KEY_AUTH);

        mPresenter.postNetDataWithAuth(url,auth);
        type = 1;
    }

    private void doDealAction(boolean isLogin) {
        if (isLogin) {
            Intent intent = new Intent(mContext, NewEditPassword.class);
            intent.putExtra("source",2);
            String phone = PreferencesUtils.getString(mContext,ConfigsetData.CONFIG_KEY_LOGIN_NUM);
            intent.putExtra("phoneNum",phone);
            startActivity(intent);
        } else {
            LoginDialog dialog = new LoginDialog();
            dialog.show(getFragmentManager(), "login");
        }
    }

    private void doLoginAction(boolean isLogin) {
        if (isLogin) {
            Intent intent = new Intent(mContext, NewEditPassword.class);
            startActivity(intent);
        } else {
            LoginDialog dialog = new LoginDialog();
            dialog.show(getFragmentManager(), "login");
        }
    }

    private void doBingdingAction(boolean isLogin) {
        if (isLogin) {
            Intent intent = new Intent(mContext, NewEditPhone.class);
            startActivity(intent);
        } else {
            LoginDialog dialog = new LoginDialog();
            dialog.show(getFragmentManager(), "login");
        }
    }

    /**
     * 点击实名认证条目
     * @param isLogin
     */
    private void doAutherAction(boolean isLogin) {
        if (isLogin) {
            boolean isAuthen = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                    ConfigsetData.CONFIG_KEY_AUTHEN);
            if (!isAuthen){
                //没有认证，跳转到认证的页面中
                Intent intent = new Intent(mContext, AuthenActivity.class);
                startActivity(intent);
            }
        } else {
            LoginDialog dialog = new LoginDialog();
            dialog.show(getFragmentManager(), "login");
        }
    }

    /**
     * 修改手势密码
     */
    private void toModifyLock() {
        InputPasswordDialog dialog = new InputPasswordDialog();
        dialog.show(getFragmentManager(), "password");

        dialog.setLoginInputListener(new InputPasswordDialog.LoginInputListener() {
            @Override
            public void onLoginDismis() {
                //不需要操作什么
            }

            @Override
            public void checkPasswordRight(String password) {
                requestCheckPassword(password);
            }
        });
    }

    /**
     * 设置手势密码锁
     * @param isLogin
     */
    private void toSetHandLock(boolean isLogin) {
        if (isLogin) {
            final  boolean isOpenGesture = LockUtil.getPwdStatus(mContext.getApplicationContext());
            mGestureToggle.setBackgroundResource(R.mipmap.toggle_button_on);
            InputPasswordDialog dialog = new InputPasswordDialog();
            dialog.show(getFragmentManager(), "password");

            dialog.setLoginInputListener(new InputPasswordDialog.LoginInputListener() {
                @Override
                public void onLoginDismis() {
                    if (isOpenGesture){
                        mGestureToggle.setBackgroundResource(R.mipmap.toggle_button_on);
                    }else {
                        mGestureToggle.setBackgroundResource(R.mipmap.toggle_button_off);
                    }
                }

                @Override
                public void checkPasswordRight(String password) {
                    if (!isOpenGesture){
                        requestCheckPassword(password);
                    }else{
                        //那就关闭掉
                        mGestureToggle.setBackgroundResource(R.mipmap.toggle_button_off);
                        mModifyPasswordLayout.setVisibility(View.GONE);
                        LockUtil.setPwdStatus(mContext.getApplicationContext(),false);
                    }
                }
            });
        } else {
            LoginDialog dialog = new LoginDialog();
            dialog.show(getFragmentManager(), "login");
        }
    }

    private void requestCheckPassword(String password) {
        String pwd = Base64Util.encodeAndMD5(password);
        String url = HttpURLData.APPFUN_PASSWORD_CHECK;
        UrlParse parse = new UrlParse(url);
        parse.putValue("password",pwd);
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        mPresenter.postNetDataWithAuth(parse.toString(),auth);
        type = 2;
    }

    /**
     * 跳转到关于的界面
     */
    private void toAboutActivity() {
        Intent intent = new Intent(mContext, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        boolean mIsLogin = PreferencesUtils.getBoolean(mContext,ConfigsetData.CONFIG_KEY_LOGIN);
        if (mIsLogin){
            String phoneNum = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_LOGIN_NUM);

            if (phoneNum != null){
                String showNum = phoneNum.substring(0,3)+"****"+phoneNum.substring(7,11);
                mPhoneText.setText(showNum);
                mPhoneText.setVisibility(View.VISIBLE);
            }

            String name = PreferencesUtils.getString(mContext, phoneNum);
            if (!StringUtil.isEmpty(name)){
                int lenth= name.length() -1;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < lenth; i++){
                    builder.append("*");
                }
                String showName = name.substring(0,1) + builder.toString();
                mAuthenText.setText(showName);
                mAuthenText.setVisibility(View.VISIBLE);
            }

            //退出当前账户可见
            mLoginoutLayout.setVisibility(View.VISIBLE);
        }else {
            mLoginoutLayout.setVisibility(View.INVISIBLE);
            mPhoneText.setVisibility(View.INVISIBLE);
            mLoginoutLayout.setVisibility(View.GONE);
        }

        boolean isOpenGesture = LockUtil.getPwdStatus(mContext.getApplicationContext());
        if (isOpenGesture){
            mGestureToggle.setBackgroundResource(R.mipmap.toggle_button_on);
            mModifyPasswordLayout.setVisibility(View.VISIBLE);
        }else{
            mGestureToggle.setBackgroundResource(R.mipmap.toggle_button_off);
            mModifyPasswordLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            if (type == 1){
                clearConfig(content, gson);
            }else if (type == 2){
                //启动手势图密码界面
                BaseBean bean = gson.fromJson(content,BaseBean.class);
                if (bean.respCode == 0){
                    Intent intent = new Intent(getActivity(), SetLockActivity.class);
                    startActivity(intent);
                }

            }

        }
    }

    private void clearConfig(String content, Gson gson) {
        BaseBean bean = gson.fromJson(content,BaseBean.class);
        if (bean != null && bean.respCode == ResponseData.RESP_CODE_OK){
            PreferencesUtils.putBoolean(mContext, ConfigsetData.CONFIG_KEY_LOGIN,false);
            PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_AUTH);
            PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_USER_TOKEN);
            PreferencesUtils.removeConfig(mContext,ConfigsetData.CONFIG_KEY_USER_UID);
            LockUtil.setPwdStatus(mContext,false);
            setData();
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        llLoading.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        llLoading.setVisibility(View.GONE);
    }
}
