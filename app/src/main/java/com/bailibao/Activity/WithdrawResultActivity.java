package com.bailibao.Activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.module.view.IGetDataView;

/**
 * Created by Administrator on 2016/4/20.
 * 提现状态的界面
 */
public class WithdrawResultActivity extends BaseActivity implements IGetDataView {
    private TextView mComplete;
    private TextView tvResult;
    private TextView tvSeeAccont;
    @Override
    protected void initData() {
        int redeemMoney = getIntent().getIntExtra("money",0) / 100;
        String str = "<font color=#ff71706e>提现金额为</font>"
                + "<font color= #48c262>"+redeemMoney+"</font>"
                +"<font color= #ff71706e>元</font>";
        tvResult.setText(Html.fromHtml(str));


    }

    @Override
    protected void setListener() {
        mComplete.setOnClickListener(this);
        tvSeeAccont.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_redemption_result);
        mComplete = (TextView) findViewById(R.id.title_right);
        tvResult = (TextView) findViewById(R.id.redemption_result);
        tvSeeAccont = (TextView) findViewById(R.id.tv_see_account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:
//                Intent intent = new Intent(this,UserBalanceActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.tv_see_account:
                Intent intent1 = new Intent(this,MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("index",3);
                startActivity(intent1);
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
