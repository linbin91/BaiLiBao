package com.bailibao.Activity;

import android.os.Handler;
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
import com.bailibao.data.ConfigsetData;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.StringUtil;

/**
 * Created by Administrator on 2016/4/23.
 *
 * 实名认证界面
 */
public class AuthenActivity extends BaseActivity implements IGetDataView {

    private TextView mAuthenVerificate;
    private EditText mAuthenName;
    private EditText mAuthenCardId;
    private LinearLayout mLoadingLayout;
    private ImageView mLoadingIv;
    private ImageView mAuthenClose;
    private String mName;


    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        mAuthenVerificate.setOnClickListener(this);
        mAuthenClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_authen);
        mAuthenVerificate = (TextView) findViewById(R.id.authentication_btn_tv);
        mAuthenName = (EditText) findViewById(R.id.authen_realName_et);
        mAuthenCardId = (EditText) findViewById(R.id.authen_idCard_et);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        mLoadingIv = (ImageView) findViewById(R.id.iv_loading);
        mAuthenClose = (ImageView) findViewById(R.id.title_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.authentication_btn_tv:
                doAuthenticateAction();
                break;
            case R.id.title_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 做认证的操作
     */
    private void doAuthenticateAction() {
        mName = mAuthenName.getText().toString().trim();

        if (mName != null){
            boolean isChinese = StringUtil.isChinese(mName);
            if (isChinese){
                String cardId = mAuthenCardId.getText().toString().trim();
                if (cardId != null){
                    boolean isCardId = StringUtil.IDCardValidate(cardId);
                    if (isCardId){
                        checkMyInfomation();
                    }else{
                        Toast.makeText(mContext,"请输入您的正确身份证号",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mAuthenCardId.setText("");
                    Toast.makeText(mContext,"请输入您的身份证号",Toast.LENGTH_SHORT).show();
                }

            }else {
                mAuthenName.setText("");
                Toast.makeText(mContext,"请输入您正确的名字",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(mContext,"请输入您的名字",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 检查输入的名字与身份证是否匹配
     */
    private void checkMyInfomation() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        mLoadingIv.startAnimation(mRotateAnim);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mLoadingIv.clearAnimation();
                mLoadingLayout.setVisibility(View.GONE);
                String phoneNum = PreferencesUtils.getString(mContext.getApplicationContext(),
                        ConfigsetData.CONFIG_KEY_LOGIN_NUM);
                PreferencesUtils.putString(mContext.getApplicationContext(),
                        phoneNum,mName);
                PreferencesUtils.putBoolean(mContext.getApplicationContext(),
                        ConfigsetData.CONFIG_KEY_AUTHEN,true);
                Toast.makeText(mContext,"认证成功",Toast.LENGTH_SHORT).show();
            }
        };
        handler.postDelayed(runnable,2000);
    }

    @Override
    public void fillView(String content) {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
