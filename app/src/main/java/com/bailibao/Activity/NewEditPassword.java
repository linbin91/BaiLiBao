package com.bailibao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
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
import com.bailibao.bean.user.UserExistenceBean;
import com.bailibao.bean.user.UserVerifyCodeBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.ResponseData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.Base64Util;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/23.
 * 点击等多，然后修改密码
 */
public class NewEditPassword extends BaseActivity implements IGetDataView{

    private ImageView mClose;
    private TextView mNextStep;
    private TextView tvPhoneNum;
    private TextView etVerifyCode;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private EditText etCode;
    private TextView tvTitle;

    //0 : 获取验证码  1： 下一步 2 ： 判断手机号码注册过了
    private int mType = 0;
    private String mPhoneNum;
    private ViewPresenter mPresenter;
    private int mTimeout = 0;

    private String mCheckCode;

    //如果等于2 ，说明是来源是交易密码
    private int mSource = 0;

    //用于倒计时显示tvGetCode.setText(mTimeout +"s后重发");
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mTimeout > 0){
                mTimeout -= 1;
                if(mTimeout == 0){
                    etVerifyCode.setClickable(true);
                    etVerifyCode.setText("重新获取");
                }else{
                    etVerifyCode.setText(mTimeout +"s后重发");
                    handler.postDelayed(runnable,1000);
                }
            }
        }
    };

    private Handler handler = new Handler();

    @Override
    protected void initData() {
        mPhoneNum = getIntent().getStringExtra("phoneNum");
        if (mPhoneNum != null){
            String showNum = mPhoneNum.substring(0,3)+"****"+mPhoneNum.substring(7,11);
            tvPhoneNum.setText(showNum);
        }

        mPresenter = new ViewPresenter(this);
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mNextStep.setOnClickListener(this);
        etVerifyCode.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        mSource = getIntent().getIntExtra("source",0);
        setContentView(R.layout.activity_edit_password);
        mClose = (ImageView) findViewById(R.id.title_left);
        tvTitle = (TextView) findViewById(R.id.title_content);
        if (mSource == 2){
            tvTitle.setText("修改交易密码");
        }
        mNextStep = (TextView) findViewById(R.id.new_user_verificaiton_submit);
        tvPhoneNum = (TextView) findViewById(R.id.new_user_verification_phone);
        etVerifyCode = (TextView) findViewById(R.id.new_user_verificaiton_getcode);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        etCode = (EditText) findViewById(R.id.new_user_verificaiton_writecode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.new_user_verificaiton_submit:
                doNextStepAction();
                break;
            case R.id.new_user_verificaiton_getcode:
                doGetCodeAction();
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void doGetCodeAction() {

        mType = 2;
        //先判断手机号码是否注册过，然后在调用获取短信的接口
        String url = HttpURLData.APPFUN_USER_EXISTENCE;
        UrlParse parse = new UrlParse(url);
        parse.putValue("phone",mPhoneNum);
        if (mSource == 2){
            parse.putValue("api","forgetTradePassword");
        }else{
            parse.putValue("api","forgetPassword");
        }
        mPresenter.getNetData(parse.toString());
    }

    /**
     * 进入下一步的操作
     */
    private void doNextStepAction() {
        //收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etVerifyCode.getWindowToken(), 0); //强制隐藏键盘

        String verifyCode = etCode.getText().toString().trim();
        if(verifyCode == null || verifyCode.isEmpty()){
            Toast.makeText(mContext,"请输入验证码",Toast.LENGTH_SHORT).show();
        }else {
            mType = 1;
            String url = HttpURLData.APPFUN_USER_CHECKCODE;
            UrlParse parse = new UrlParse(url);
            parse.putValue("phone",mPhoneNum);
            mCheckCode = Base64Util.encodeAndMD5(verifyCode);
            parse.putValue("checkcode",mCheckCode);
            parse.putValue("api","forgetPassword");
            mPresenter.postNetData(parse.toString());
        }
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            if (mType == 2){
                UserExistenceBean bean = gson.fromJson(content,UserExistenceBean.class);
                if (bean != null){
                    if (bean.existence == ResponseData.USER_NO_EXIST && bean.respCode == ResponseData.RESP_CODE_OK){
                        Toast.makeText(mContext,"此号还没注册，请先去注册",Toast.LENGTH_SHORT).show();
                    }else if (bean.existence == ResponseData.USER_IS_EXIST && bean.respCode == ResponseData.RESP_CODE_OK){
                        //用户已经注册了，然后去获取验证码
                        mType = 0;
                        String url = HttpURLData.APPFUN_USER_CHECKCODE;
                        UrlParse parse = new UrlParse(url);
                        parse.putValue("phone", mPhoneNum);
                        parse.putValue("api", "forgetPassword");
                        mPresenter.getNetData(parse.toString());
                    }
                }
            }else if (mType == 0){
                //获取验证码
                UserVerifyCodeBean bean = gson.fromJson(content,UserVerifyCodeBean.class);
                if (bean.respCode == 0){
                    if (bean.timeout != 0){
                        etVerifyCode.setClickable(false);
                        mTimeout = bean.timeout;
                        handler.post(runnable);
                    }
                }
            }else{
                //下一步
                BaseBean bean = gson.fromJson(content,BaseBean.class);
                if (bean != null){
                    if (bean.respCode == 0){
                        Intent intent = new Intent(mContext,EditLoginPassworActivity.class);
                        intent.putExtra("phone", mPhoneNum);
                        intent.putExtra("checkcode",mCheckCode);
                        if (mSource == 2){
                            intent.putExtra("soure",2);
                        }
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(mContext,bean.respMsg,Toast.LENGTH_SHORT).show();
                    }
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
}
