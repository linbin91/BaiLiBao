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
import com.bailibao.view.stepview.StepsView;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/22.
 *设置登录密码
 */
public class RegisterPasswordActivity extends BaseActivity implements IGetDataView {

    private StepsView mStepsView;
    private String[] mLables = new String[]{"验证手机号码","设置登录密码","注册成功"};
    private TextView mRegisterNext;
    private ImageView mRegisterClose;
    private EditText etPassword;

    private String mPhoneNum;
    //已经加密的
    private String mCheckCode;

    private LinearLayout llLoading;
    private ImageView ivLoading;


    @Override
    protected void initData() {
        mStepsView.setLabels(mLables);
        mStepsView.setBarColorIndicator(getResources().getColor(R.color.register_line_color));
        mStepsView.setCompletedPosition(1);

        mPhoneNum = getIntent().getStringExtra("phone");
        mCheckCode = getIntent().getStringExtra("checkcode");
    }

    @Override
    protected void setListener() {
        mRegisterNext.setOnClickListener(this);
        mRegisterClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_register_password);
        mStepsView = (StepsView) findViewById(R.id.register_steps);
        mRegisterNext = (TextView) findViewById(R.id.login_activity_login_tv);
        mRegisterClose = (ImageView) findViewById(R.id.title_left);
        etPassword = (EditText) findViewById(R.id.register_activity_pw_et);

        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_activity_login_tv:
                toRegisterComplete();
                break;
            case R.id.title_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到完成注册界面
     */
    private void toRegisterComplete() {
        //判断输入的密码是否正确
        String password = etPassword.getText().toString().trim();
        if (password == null || password.isEmpty() || !StringUtil.isRightPassword(password)){
            Toast.makeText(mContext,"请输入6-30位数字或者字母",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpURLData.APPFUN_USER_REGISTER;
        UrlParse parse = new UrlParse(url);
        parse.putValue("phone",mPhoneNum);
        parse.putValue("password", Base64Util.encodeAndMD5(password));
        parse.putValue("checkcode",mCheckCode);

        ViewPresenter presenter = new ViewPresenter(this,this);
        presenter.postNetData(parse.toString());
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

                    String auth = "uid="+bean.uid+"|"+"token="+Base64Util.decode(bean.token);
                    PreferencesUtils.putString(mContext, ConfigsetData.CONFIG_KEY_AUTH,Base64Util.encode(auth));
                    Intent intent = new Intent(mContext, RegisterCompleteActivity.class);
                    startActivity(intent);
                    finish();
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
