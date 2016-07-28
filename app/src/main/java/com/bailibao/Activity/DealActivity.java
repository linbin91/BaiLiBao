package com.bailibao.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.data.ConfigsetData;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;

/**
 * Created by Administrator on 2016/4/23.
 *
 * 交易密码的界面
 */
public class DealActivity extends BaseActivity implements IGetDataView {

    private ImageView mClose;
    private TextView mPhoneNum;

    @Override
    protected void initData() {
        String phoneNum = PreferencesUtils.getString(mContext.getApplicationContext(), ConfigsetData.CONFIG_KEY_LOGIN_NUM);

        if (phoneNum != null){
            String showNum = phoneNum.substring(0,3)+"****"+phoneNum.substring(7,11);
            mPhoneNum.setText(showNum);
        }
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_deal);
        mClose = (ImageView) findViewById(R.id.title_left);
        mPhoneNum = (TextView) findViewById(R.id.new_user_verification_phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            default:
                break;
        }
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
