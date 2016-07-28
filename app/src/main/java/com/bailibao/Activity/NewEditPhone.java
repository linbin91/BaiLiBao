package com.bailibao.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.module.view.IGetDataView;

/**
 * Created by Administrator on 2016/4/23.
 */
public class NewEditPhone extends BaseActivity implements IGetDataView {

    private ImageView mColse;
    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        mColse.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_edit_phone);
        mColse = (ImageView) findViewById(R.id.title_left);
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
