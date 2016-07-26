package com.bailibao.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.data.ConfigsetData;
import com.bailibao.util.LockUtil;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.view.CustomLockView;

/**
 * 登陆验证页面
 * Created by apple on 4/11/15.
 */
public class LoginLockActivity extends Activity implements View.OnClickListener{
    private TextView tvWarn;
    private int[] mIndexs;
    Context context;
    private  CustomLockView mLockView;
    private TextView mTvPhone;
    private TextView mTvForgetGesture;
    private TextView mTvLoginOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_loginlock);
        initView();
        setListener();

        mIndexs= LockUtil.getPwd(this);
        //判断当前是否设置过密码，没有设置过，直接跳转到设置手势密码页面
        if(mIndexs.length>1){
            final CustomLockView cl=(CustomLockView)findViewById(R.id.cl);
            cl.setmIndexs(mIndexs);
            cl.setErrorTimes(4);
            cl.setStatus(1);
            cl.setShow(false);
            cl.setOnCompleteListener(new CustomLockView.OnCompleteListener() {
                @Override
                public void onComplete(int[] indexs) {
                    Toast.makeText(LoginLockActivity.this,"正确",Toast.LENGTH_SHORT).show();
                    BaseActivity.isShowGestureLock = false;
                    finish();
                }

                @Override
                public void onError() {
                    if (cl.getErrorTimes() > 0) {
                        tvWarn.setVisibility(View.VISIBLE);
                        tvWarn.setText("密码错误，还可以再输入" + cl.getErrorTimes() + "次");
                        tvWarn.setTextColor(getResources().getColor(R.color.red));
                    }
                    else {
                        PreferencesUtils.putBoolean(context,ConfigsetData.CONFIG_KEY_LOGIN,false);
                        LockUtil.setPwdStatus(context,false);
                        finish();
                    }
                }
            });
        }

        mLockView.setShowToast(false);
    }

    private void setListener() {
        mTvForgetGesture.setOnClickListener(this);
        mTvLoginOther.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        tvWarn= (TextView) findViewById(R.id.login_warn);
        tvWarn.setVisibility(View.INVISIBLE);
        mLockView = (CustomLockView) findViewById(R.id.cl);
        mTvPhone = (TextView) findViewById(R.id.tv_phone_num);

        mTvForgetGesture = (TextView) findViewById(R.id.tv_forget_gesture);
        mTvLoginOther = (TextView) findViewById(R.id.tv_login_other);
        String phoneNum = PreferencesUtils.getString(context, ConfigsetData.CONFIG_KEY_LOGIN_NUM);

        if (phoneNum != null){
            String showNum = phoneNum.substring(0,3)+"****"+phoneNum.substring(7,11);
            mTvPhone.setText(showNum);
        }

    }


    /**
     * 返回的按钮
     */
    @Override
    public void onBackPressed() {
        finish();
        exitApp();
    }

    private void exitApp() {
        Intent intent = new Intent();
        intent.setAction(BaseActivity.FINISH_ACTIVITY);
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_forget_gesture:
                doForgetAction();
                break;
            case R.id.tv_login_other:
                doLoginOtherAction();
                break;
            default:
                break;
        }
    }

    /**
     * 登录其他用户的操作
     */
    private void doLoginOtherAction() {
        LockUtil.setPwdStatus(context,false);
        Intent intent = new Intent(context,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 点击忘记手势的操作
     */
    private void doForgetAction() {
        LockUtil.setPwdStatus(context,false);
        Intent intent = new Intent(context,LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
