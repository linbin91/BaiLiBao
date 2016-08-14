package com.bailibao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.user.BankCardBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.StringUtil;
import com.bailibao.util.UrlParse;
import com.bailibao.view.myadapter.CommonAdapter;
import com.bailibao.view.myadapter.ViewHolder;
import com.bailibao.view.pullview.PullToRefreshListView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MyBankCardActivity extends BaseActivity implements IGetDataView {

    @InjectView(R.id.activity_investment_title)
    RelativeLayout activityInvestmentTitle;
    @InjectView(R.id.ll_no_card)
    LinearLayout llNoCard;
    @InjectView(R.id.inverstment_list)
    PullToRefreshListView inverstmentList;
    @InjectView(R.id.iv_loading)
    ImageView ivLoading;
    @InjectView(R.id.loading_layout)
    LinearLayout loadingLayout;

    TextView Bangding;
    private ImageView mClose;

    private ViewPresenter mPresenter;
    private BaseAdapter mAdapter;
    private List<BankCardBean.CardItem> mDatas = new ArrayList<>();

    private int type;

    @Override
    protected void initData() {
        mPresenter = new ViewPresenter(this,this);
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        mPresenter.getNetDataWithAuth(HttpURLData.APPFUN_BANK_CARD, auth);
        type = 1;
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        Bangding.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_my_bankcard);
        ButterKnife.inject(this);
        mClose = (ImageView) findViewById(R.id.title_left);
        Bangding = (TextView) findViewById(R.id.bangding);
        inverstmentList.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<BankCardBean.CardItem>(mContext, mDatas, R.layout.bank_card_item) {

            @Override
            public void convert(ViewHolder helper, BankCardBean.CardItem item) {
                helper.setText(R.id.tv_bank_detail, item.bankName);
                helper.setText(R.id.tv_num_detail, item.cardNo);
            }
        });

        inverstmentList.setHasMoreData(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.bangding:
                doBingDingAction();
                break;
            default:
                break;
        }

    }

    /*
    *绑定银行卡
     */
    private void doBingDingAction() {
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        if (auth != null && !TextUtils.isEmpty(auth)){
            UrlParse parse = new UrlParse(HttpURLData.APPFUN_MONEY_REGISTER);

            if (mPresenter == null){
                mPresenter = new ViewPresenter(this,this);
            }
            mPresenter.getNetDataWithAuth(parse.toString(),auth);

            type = 2;
        }
    }


    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)) {
            if (type == 1){
                Gson gson = new Gson();
                BankCardBean bean = gson.fromJson(content, BankCardBean.class);
                if (bean != null && bean.resources != null && bean.resources.size() != 0) {
                    mDatas.clear();
                    mDatas.addAll(bean.resources);
                    mAdapter.notifyDataSetChanged();
                } else {
                    inverstmentList.setVisibility(View.GONE);
                    llNoCard.setVisibility(View.VISIBLE);
                }
            }else if (type == 2){

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String,String> map = objectMapper.readValue(content,Map.class);
                    gotoWebView(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 跳转到富友的h5
     *
     * @param map
     */
    private void gotoWebView(Map map) {
        if (map != null){
            String postData = StringUtil.makePostHTML(HttpURLData.JZH_API_APP_WEB_REG_URL,map);
            if (postData != null){
                Intent intent = new Intent(this,FuiouWebviewActivity.class);
                intent.putExtra(HttpURLData.INTENT_API_DATA_KEY_DATA, postData);
                startActivity(intent);
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        loadingLayout.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        loadingLayout.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
