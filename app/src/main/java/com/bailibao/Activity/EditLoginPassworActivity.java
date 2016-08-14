package com.bailibao.Activity;

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
 * Created by Administrator on 2016/4/23.
 * 修改登录密码界面
 */
public class EditLoginPassworActivity extends BaseActivity implements IGetDataView{

    private ImageView mClose;
    private TextView mCommit;
    private EditText mFirstPassword;
    private EditText mSecondPassword;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvTitle;

    private String mPhoneNum;
    private String mCheckCode;

    private int mSource;

    @Override
    protected void initData() {
        mPhoneNum = getIntent().getStringExtra("phone");
        mCheckCode = getIntent().getStringExtra("checkcode");
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mCommit.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        mSource = getIntent().getIntExtra("source",0);
        setContentView(R.layout.activity_edit_login_password);
        mClose = (ImageView) findViewById(R.id.title_left);
        tvTitle = (TextView) findViewById(R.id.title_content);
        if (mSource == 2){
            tvTitle.setText("修改交易密码");
        }
        mCommit = (TextView) findViewById(R.id.security_edit_loginpw_rl_tv);
        mFirstPassword = (EditText) findViewById(R.id.security_edit_loginpw_newpw);
        mSecondPassword = (EditText) findViewById(R.id.security_edit_loginpw_confirmpw);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.security_edit_loginpw_rl_tv:
                doCommitAction();
            default:
                break;
        }
    }

    /**
     * 提交先密码的操作
     */
    private void doCommitAction() {

        String firstPassword = mFirstPassword.getText().toString().trim();
        String secondPassword = mSecondPassword.getText().toString().trim();

        if (!StringUtil.isEmpty(firstPassword) && !StringUtil.isEmpty(secondPassword)){
            if (firstPassword.equals(secondPassword)){
                //判断输入的密码是否是6-30位的数字或者字母
                if (!StringUtil.isRightPassword(firstPassword)){
                    Toast.makeText(mContext,"请输入6-30位数字或者字母",Toast.LENGTH_SHORT).show();
                }else{
                    //密码输入的正确就上传服务端
                    String url = null;
                    if (mSource == 2 ){
                        url = HttpURLData.APPFUN_MODIFY_TRADE;
                    }else{
                        url = HttpURLData.APPFUN_USER_FORGETPASSWORD;
                    }

                    UrlParse parse = new UrlParse(url);

                    parse.putValue("password",Base64Util.encodeAndMD5(firstPassword));
                    parse.putValue("checkcode", mCheckCode);

                    ViewPresenter presenter = new ViewPresenter(this,this);
                    if (mSource == 2){
                        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
                        presenter.postNetDataWithAuth(parse.toString(),auth);
                    }else{
                        parse.putValue("phone",mPhoneNum);
                        presenter.postNetData(parse.toString());
                    }
                }
            }else{
                Toast.makeText(mContext,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                mFirstPassword.setText("");
                mSecondPassword.setText("");
            }
        }
    }

    @Override
    public void fillView(String content) {
        if (content != null) {
            Gson gson = new Gson();
            UserRegisterBean bean = gson.fromJson(content, UserRegisterBean.class);
            if (bean != null) {
                if (bean.respCode == ResponseData.RESP_CODE_OK) {
                    if (mSource != 2){
                        PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_USER_UID, bean.uid);
                        PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_USER_TOKEN, Base64Util.decode(bean.token));
                        String auth = "uid="+bean.uid+"|"+"token="+Base64Util.decode(bean.token);
                        PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_AUTH,Base64Util.encode(auth));
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
