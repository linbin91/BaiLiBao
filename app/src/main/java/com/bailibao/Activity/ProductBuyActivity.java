package com.bailibao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.product.ProductBuyBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.model.GetNetData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.StringUtil;
import com.bailibao.util.UrlParse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ProductBuyActivity extends BaseActivity implements IGetDataView {

    @InjectView(R.id.tv_lock)
    TextView tvLock;
    @InjectView(R.id.tv_limit)
    TextView tvLimit;
    @InjectView(R.id.tv_charge)
    TextView tvCharge;
    @InjectView(R.id.tv_max_buy)
    TextView tvMaxBuy;
    @InjectView(R.id.et_buy_count)
    EditText etBuyCount;
    private TextView mProductBuy;
    private ImageView mClose;
    private TextView mTitleRight;
    private TextView tvLeftCount;
    private TextView tvLeftMoney;
    private ViewPresenter mPresenter;
    private int type;
    private int mProductId;

    @Override
    protected void initData() {

        mPresenter = new ViewPresenter(this, this);
        int productId = getIntent().getIntExtra("productId", 0);

        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        if (auth != null && !TextUtils.isEmpty(auth)) {
            UrlParse parse = new UrlParse(HttpURLData.APPFUN_PRODUCT_BUY);
            parse.putValue("productId", productId);
//            mPresenter.postNetDataWithAuth(parse.toString(), auth);
//            type = 1;

            String urlBuy = parse.toString();

            GetNetData tool = new GetNetData();
            tool.postNetDataWithAuth(new Callback<String>() {
                @Override
                public String parseNetworkResponse(Response response) throws Exception {
                    String content = response.body().string();
                    return content;
                }

                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    ProductBuyBean bean = gson.fromJson(response, ProductBuyBean.class);
                    if (bean != null && bean.respCode == 0) {
                        tvLock.setText(bean.lockDays + "");
                        tvLimit.setText(bean.toplimit + "");
                        tvLeftCount.setText(bean.leftCount + "");
                        tvMaxBuy.setText(bean.maxBuyCount + "");
                        tvLeftMoney.setText(bean.balance + "");
                        etBuyCount.setHint("每份价格元" + bean.price + "元");
                        mProductId = bean.orderId;
                        mAgreementPath = bean.agreementPath;
                    }else{
                        finish();
                        ProductBuyActivity.this.toast(bean.respMsg);
                    }
                }
            },urlBuy,auth);
        }
    }

    @Override
    protected void setListener() {
        mProductBuy.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mTitleRight.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_product_buy);
        mProductBuy = (TextView) findViewById(R.id.product_tv_buy);
        mClose = (ImageView) findViewById(R.id.title_left);
        mTitleRight = (TextView) findViewById(R.id.title_right);
        tvLeftCount = (TextView) findViewById(R.id.tv_product_letfcount);
        //当前余额
        tvLeftMoney = (TextView) findViewById(R.id.tv_left_money);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_tv_buy:
                doBuyAction();
                break;
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                doRechargeAction();
                break;
            default:
                break;
        }
    }

    /**
     * 执行充值的操作
     */
    private void doRechargeAction() {
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        if (auth != null && !TextUtils.isEmpty(auth)) {
            UrlParse parse = new UrlParse(HttpURLData.APPFUN_MONEY_PAY);
            parse.putValue("amt", 1000);
            if (mPresenter == null) {
                mPresenter = new ViewPresenter(this, this);
            }
            mPresenter.getNetDataWithAuth(parse.toString(), auth);
            type = 2;
        }
    }


    /**
     * 点击购买的按钮
     */
    private void doBuyAction() {
        if (mErrMessage != null && TextUtils.isEmpty(mErrMessage)) {
            String buyCount = etBuyCount.getText().toString().trim();
            if (buyCount != null && !buyCount.isEmpty()) {
//            Intent intent = new Intent(this,ProductBuyState.class);
//            intent.putExtra("productId",productId);
//            intent.putExtra("buyCount",Integer.parseInt(buyCount));
//            startActivity(intent);
                Intent intent = new Intent(this, ProductBuyProtocol.class);
                intent.putExtra("productId", mProductId);
                intent.putExtra("buyCount", Integer.parseInt(buyCount));
                intent.putExtra("path", mAgreementPath);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "请输入购买的份额", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, mErrMessage, Toast.LENGTH_SHORT).show();
        }

    }

    private String mAgreementPath;

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)) {
            if (type == 2) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> map = null;
                try {
                    map = objectMapper.readValue(content, Map.class);
                    gotoWebView(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (type == 1) {
                Gson gson = new Gson();
                ProductBuyBean bean = gson.fromJson(content, ProductBuyBean.class);
                if (bean != null) {
                    tvLock.setText(bean.lockDays + "");
                    tvLimit.setText(bean.toplimit + "");
                    tvLeftCount.setText(bean.leftCount + "");
                    tvMaxBuy.setText(bean.maxBuyCount + "");
                    tvLeftMoney.setText(bean.balance + "");
                    etBuyCount.setHint("每份价格元" + bean.price + "元");
                    mProductId = bean.orderId;
                    mAgreementPath = bean.agreementPath;
                }
            }
//            else if (type == 3){
//                Gson gson = new Gson();
//                ProductBuyResultBean result = gson.fromJson(content,ProductBuyResultBean.class);
//                if (result != null){
//                    Toast.makeText(this,result.orderNo,Toast.LENGTH_SHORT).show();
//                }
//            }

        }
    }



    private void gotoWebView(Map<String, String> map) {
        if (map != null) {
            String postData = StringUtil.makePostHTML(HttpURLData.JZH_API_APP_500001_URL, map);
            if (postData != null) {
                Intent intent = new Intent(this, FuiouWebviewActivity.class);
                intent.putExtra(HttpURLData.INTENT_API_DATA_KEY_DATA, postData);
                startActivity(intent);
            }
        }
    }

    private String mErrMessage;

    @Override
    public void toast(String msg) {
        mErrMessage = msg;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
