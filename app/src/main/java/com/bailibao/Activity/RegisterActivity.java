package com.bailibao.Activity;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.bailibao.bean.user.UserExistenceBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.ResponseData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.StringUtil;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/22.
 *
 * 点击注册的第一页面
 */
public class RegisterActivity extends BaseActivity implements IGetDataView {

    private TextView mRegisterExplain;
    private TextView mRegister;
    private ImageView mRegisterClose;
    private EditText etRegisterPhone;
    private ViewPresenter mPresenter;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private String mPhoneNum;
    private TextView tvRightTiltle;

    @Override
    protected void initData() {
        SpannableStringBuilder builder = new SpannableStringBuilder("点击上面的注册按钮表示您同意《佰利宝网用户服务协议》");
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.dream_dark_bluecolor));
        builder.setSpan(redSpan,14,26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mRegisterExplain.setText(builder);
        mPresenter = new ViewPresenter(this);

    }

    @Override
    protected void setListener() {
        mRegister.setOnClickListener(this);
        mRegisterClose.setOnClickListener(this);
        tvRightTiltle.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_register);
        mRegisterExplain = (TextView) findViewById(R.id.register_expain);
        mRegister = (TextView) findViewById(R.id.register_activity_register_tv);
        mRegisterClose = (ImageView) findViewById(R.id.title_left);
        etRegisterPhone = (EditText) findViewById(R.id.register_activity_phone_et);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvRightTiltle = (TextView) findViewById(R.id.title_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_activity_register_tv:
                toGetCodeRegister();
                break;
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                toLoginActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 进入登入界面
     */
    private void toLoginActivity() {
        Intent intent = new Intent(mContext,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toGetCodeRegister() {
        //收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        String str = etRegisterPhone.getText().toString().trim();
        if (str == null || str.isEmpty() || !StringUtil.isMobileNum(str)){
            Toast.makeText(mContext,"请填写正确手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        mPhoneNum = str;
        String url = HttpURLData.APPFUN_USER_EXISTENCE;
        UrlParse parse = new UrlParse(url);
        parse.putValue("phone",str);
        mPresenter.getNetData(parse.toString());

    }

    @Override
    public void fillView(String content) {

        Gson gson = new Gson();
        UserExistenceBean bean = gson.fromJson(content,UserExistenceBean.class);
        if (bean != null){
            if (bean.existence == ResponseData.USER_NO_EXIST && bean.respCode == ResponseData.RESP_CODE_OK){
                Intent intent = new Intent(mContext,RegisterCodeActivity.class);
                intent.putExtra("phoneNum",mPhoneNum);
                startActivity(intent);
                finish();
            }else if (bean.existence == ResponseData.USER_IS_EXIST && bean.respCode == ResponseData.RESP_CODE_OK){
                Toast.makeText(mContext,"此号已被注册，请换其他号码",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void toast(String msg) {

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
