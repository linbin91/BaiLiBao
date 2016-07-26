package com.bailibao.Activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.AccountInvestBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.Status;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.DataUtil;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.bailibao.view.myadapter.CommonAdapter;
import com.bailibao.view.myadapter.ViewHolder;
import com.bailibao.view.pullview.PullToRefreshBase;
import com.bailibao.view.pullview.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 * 在投资金Activity
 */
public class InvestmentActivity extends BaseActivity implements IGetDataView {

    private ImageView mClose;
    private PullToRefreshListView mListView;
    private List<AccountInvestBean.InvestItem> mDatas = new ArrayList<>();
    private BaseAdapter mAdapter;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private String mUrl = HttpURLData.APPFUN_ACCOUNT_INVEST;
    private ViewPresenter mPresenter;
    private String mAuth;
    private int mPages = 1;
    private Status mStatus = Status.NO_PULL;

    @Override
    protected void initData() {

        mListView.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<AccountInvestBean.InvestItem>(mContext,mDatas,R.layout.investment_item) {

            @Override
            public void convert(ViewHolder helper, AccountInvestBean.InvestItem item) {
                helper.setText(R.id.tv_name,item.name);
                if (item.project == 0){
                    helper.setViewGone(R.id.tv_process);
                }
                helper.setText(R.id.tv_share,item.count + "");
                if (item.type == 1){
                    helper.setText(R.id.tv_yield,"昨日收益");

                }else{
                    helper.setText(R.id.tv_yield,"预期收益");
                }
                helper.setText(R.id.tv_yield_count, (double) item.profit /100+ "");
            }
        });

        mListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDatas.get(position).type == 1) {
                    Intent intent = new Intent(mContext, BuyDetailActivity.class);
                    intent.putExtra("id",mDatas.get(position).id);
                    intent.putExtra("name",mDatas.get(position).name);
                    startActivity(intent);
                }

            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPages = 1;
                mStatus = Status.PULL_FROM_START;
                setLastUpdateTime();
                getInvestment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPages += 1;
                mStatus = Status.PULL_FROM_END;
                getInvestment();
            }
        });
        mAuth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);

        getInvestment();
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_investment);
        mListView = (PullToRefreshListView) findViewById(R.id.inverstment_list);
        mListView.setScrollLoadEnabled(true);
        mClose = (ImageView) findViewById(R.id.title_left);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
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
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            AccountInvestBean bean = gson.fromJson(content,AccountInvestBean.class);
            if (bean != null && bean.resources != null && bean.resources.size() != 0) {
                boolean hasMoreData = bean.hasNextPage;;
                if (mStatus == Status.NO_PULL){
                    mDatas.addAll(bean.resources);

                }else if (mStatus == Status.PULL_FROM_END){
                    mDatas.addAll(bean.resources);
                }else{
                    mDatas.clear();
                    mDatas.addAll(bean.resources);
                }
                mAdapter.notifyDataSetChanged();
                mListView.onPullUpRefreshComplete();
                mListView.onPullDownRefreshComplete();
                mListView.setHasMoreData(hasMoreData);
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (mStatus == Status.NO_PULL){
            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
            ivLoading.startAnimation(mRotateAnim);
        }
    }

    @Override
    public void hideProgress() {
        if (mStatus == Status.NO_PULL){
            ivLoading.clearAnimation();
            llLoading.setVisibility(View.GONE);
        }
    }

    private void  getInvestment(){
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize",10);
        parse.putValue("pageNo",mPages);

        if (mPresenter == null){
            mPresenter = new ViewPresenter(this);
        }
        mPresenter.getNetDataWithAuth(parse.toString(),mAuth);
    }

    private void setLastUpdateTime() {
        String text = DataUtil.formatDateTime(System.currentTimeMillis());
        mListView.setLastUpdatedLabel(text);
    }
}
