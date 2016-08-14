package com.bailibao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.base.BaseBean;
import com.bailibao.bean.user.UserVerifyCodeBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.Base64Util;
import com.bailibao.util.UrlParse;
import com.bailibao.view.stepview.StepsView;
import com.google.gson.Gson;

/**
 * 注册的界面---》获取手机验证码
 * Created by Administrator on 2016/4/22.
 */
public class RegisterCodeActivity extends BaseActivity implements IGetDataView {

    private StepsView mStepsView;
    private String[] mLables = new String[]{"验证手机号码","设置登录密码","注册成功"};
    private TextView mRegisterNext;
    private ImageView mRegisterClose;
    private TextView tvGetCode;
    private String mPhoneNum;
    private ViewPresenter presenter;
    private EditText etVerifyCode;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    //0 : 获取验证码  1： 下一步
    private int mType = 0;
    private int mTimeout = 0;

    private String mCheckCode;

    //用于倒计时显示tvGetCode.setText(mTimeout +"s后重发");
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mTimeout > 0){
                mTimeout -= 1;
                if(mTimeout == 0){
                    tvGetCode.setClickable(true);
                    tvGetCode.setText("重新获取");
                }else{
                    tvGetCode.setText(mTimeout +"s后重发");
                    handler.postDelayed(runnable,1000);
                }
            }
        }
    };

    private Handler handler = new Handler();

    @Override
    protected void initData() {

        mStepsView.setLabels(mLables);
        mStepsView.setBarColorIndicator(getResources().getColor(R.color.register_line_color));
        mStepsView.setCompletedPosition(0);
        mPhoneNum = getIntent().getStringExtra("phoneNum");
        presenter = new ViewPresenter(this,this);
    }

    @Override
    protected void setListener() {
        mRegisterNext.setOnClickListener(this);
        mRegisterClose.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_register_code);
        mStepsView = (StepsView) findViewById(R.id.register_steps);
        mRegisterNext = (TextView) findViewById(R.id.login_activity_login_tv);
        mRegisterClose = (ImageView) findViewById(R.id.title_left);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        etVerifyCode = (EditText) findViewById(R.id.login_activity_pw_et);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_activity_login_tv:
                nextStep();
                break;
            case R.id.title_left:
                finish();
                break;
            case R.id.tv_get_code:
                getVerifyCode();
                break;
            default:
                break;
        }
    }

    /**
     * 点击下一步的操作
     */
    private void nextStep() {
        //收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etVerifyCode.getWindowToken(), 0); //强制隐藏键盘

        String verifyCode = etVerifyCode.getText().toString().trim();
        if(verifyCode == null || verifyCode.isEmpty()){
            Toast.makeText(mContext,"请输入验证码",Toast.LENGTH_SHORT).show();
        }else {
            mType = 1;
            String url = HttpURLData.APPFUN_USER_CHECKCODE;
            UrlParse parse = new UrlParse(url);
            parse.putValue("phone",mPhoneNum);
            mCheckCode = Base64Util.encodeAndMD5(verifyCode);
            parse.putValue("checkcode",mCheckCode);
            parse.putValue("api","register");
            presenter.postNetData(parse.toString());
        }
    }

    @Override
    public void fillView(String content) {
        Gson gson = new Gson();
        if (mType == 1){
            BaseBean bean = gson.fromJson(content,BaseBean.class);
            if (bean != null){
                if (bean.respCode == 0){
                    Intent intent = new Intent(mContext,RegisterPasswordActivity.class);
                    intent.putExtra("phone", mPhoneNum);
                    intent.putExtra("checkcode",mCheckCode);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(mContext,bean.respMsg,Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            //获取验证码
            UserVerifyCodeBean bean = gson.fromJson(content,UserVerifyCodeBean.class);
            if (bean.respCode == 0){
                if (bean.timeout != 0){
                    tvGetCode.setClickable(false);
                    mTimeout = bean.timeout;
                    handler.post(runnable);
                }
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (mType == 1){
            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
            ivLoading.startAnimation(mRotateAnim);
        }
    }

    @Override
    public void hideProgress() {
        if (mType == 1){
            ivLoading.clearAnimation();
            llLoading.setVisibility(View.GONE);
        }
    }

    //获取短信验证码
    public void getVerifyCode() {
        mType = 0;
        String url = HttpURLData.APPFUN_USER_CHECKCODE;
        UrlParse parse = new UrlParse(url);
        parse.putValue("phone",mPhoneNum);
        parse.putValue("api","register");
        presenter.getNetData(parse.toString());
    }
}
