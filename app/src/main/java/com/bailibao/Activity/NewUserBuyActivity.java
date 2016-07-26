package com.bailibao.Activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.module.view.IGetDataView;

/**
 * Created by Administrator on 2016/4/15.
 */
public class NewUserBuyActivity extends BaseActivity implements IGetDataView {
    private ImageView mClose;
    private TextView mTopUp;
    private TextView mBuy;
    private EditText mBuyCount;
    private CheckBox mCheckProtocol;

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mTopUp.setOnClickListener(this);
        mBuy.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_new_user_buy);
        mClose = (ImageView) findViewById(R.id.title_right);
        mTopUp = (TextView) findViewById(R.id.title_left);
        mBuy = (TextView) findViewById(R.id.activity_structbuy_btn);
        mBuyCount = (EditText) findViewById(R.id.activity_structbuy_writecount);
        mCheckProtocol = (CheckBox) findViewById(R.id.activity_structbuy_ctv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.title_right:
                finish();
                break;
            case R.id.title_left:
                break;
            case R.id.activity_structbuy_btn:
                doBuyAction();
                break;
            default:
                break;
        }
    }

    private void doBuyAction() {
        String input = mBuyCount.getText().toString().trim();
        if (input == null || input.isEmpty()){
            Toast.makeText(mContext,"请输入购买的份额",Toast.LENGTH_SHORT).show();
        }else if (!mCheckProtocol.isChecked()){
            Toast.makeText(mContext,"请同意协议后再进行购买",Toast.LENGTH_SHORT).show();
        }
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
