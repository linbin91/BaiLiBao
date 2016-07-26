package com.bailibao.Activity;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.view.stepview.StepsView;

/**
 * Created by Administrator on 2016/4/22.
 *
 * 完成注册的界面
 */
public class RegisterCompleteActivity extends BaseActivity implements IGetDataView {

    private TextView mTvAuther;
    private StepsView mStepsView;
    private String[] mLables = new String[]{"验证手机号码","设置登录密码","注册成功"};
    private ImageView mRegisterClose;
    private TextView tvAuther;
    private TextView tvComplete;
    @Override
    protected void initData() {
        SpannableStringBuilder builder = new SpannableStringBuilder("您可以继续完成实名认证以提高账户安全性");
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.register_line_color));
        builder.setSpan(redSpan,6,10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAuther.setText(builder);

        mStepsView.setLabels(mLables);
        mStepsView.setBarColorIndicator(getResources().getColor(R.color.register_line_color));
        mStepsView.setCompletedPosition(2);
    }

    @Override
    protected void setListener() {
        mRegisterClose.setOnClickListener(this);
        tvAuther.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_register_complete);
        mTvAuther = (TextView) findViewById(R.id.register_tv_auther);
        mStepsView = (StepsView) findViewById(R.id.register_steps);
        mRegisterClose = (ImageView) findViewById(R.id.title_left);
        tvAuther = (TextView) findViewById(R.id.login_activity_auther_tv);
        tvComplete = (TextView) findViewById(R.id.login_activity_complete_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.login_activity_auther_tv:
                doAutherAction();
                break;
            case R.id.login_activity_complete_tv:
                doCompleteAction();
                break;
            default:
                break;

        }
    }

    /**
     * 完成的界面
     */
    private void doCompleteAction() {
        finish();
    }

    /**
     * 跳转到实名制界面
     */
    private void doAutherAction() {
        Intent intent = new Intent(mContext,AuthenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void fillView(String content) {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
