package com.bailibao.Activity;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.InvestProductBean;
import com.bailibao.bean.TotalIncomingBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.Status;
import com.bailibao.module.model.GetNetData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.popupwindow.RefreshListviewPop;
import com.bailibao.util.DataUtil;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.bailibao.view.myadapter.CommonAdapter;
import com.bailibao.view.myadapter.ViewHolder;
import com.bailibao.view.pullview.PullToRefreshBase;
import com.bailibao.view.pullview.PullToRefreshListView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/4/20.
 *
 * 个人账户 ——————》 累计收益
 *
 * tip : 避免改动量太大，就将在原有的基础上增加逻辑了
 */
public class TotalIncomeActivity extends BaseActivity implements IGetDataView{

    private LinearLayout mIncomingCateLl;
    private LinearLayout mIncomingTimeLl;
    private List<InvestProductBean.ProductItem> mCateList = new ArrayList<>();
    private List<InvestProductBean.ProductItem> mTimeList;
    private ImageView mCateIv;
    private ImageView mTimeIv;
    private View mViewShape;
    private View mViewLine;
    private ImageView mClose;
    private PullToRefreshListView mListView;
    private BaseAdapter mAdapter;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvCate;
    private TextView tvTime;

    private TextView tvNoData;
    private List<TotalIncomingBean.IncomingItem> mListItems = new ArrayList<>();

    //上次选中的种类
    private int mSelectCate = 0;
    //上次选中的时间
    private int mSelectTime = 0;

    private int mPage = 1;

    private Status mStatus = Status.NO_PULL;
    private String mUrl = HttpURLData.APPFUN_ACCOUNT_PROFIT;
    private String mAuth;

    @Override
    protected void initData() {

        if (mTimeList == null){
            mTimeList = new ArrayList<>();
            mTimeList.add(new InvestProductBean.ProductItem(0,"全部时间"));
            mTimeList.add(new InvestProductBean.ProductItem(1,"近一个月"));
            mTimeList.add(new InvestProductBean.ProductItem(3,"近三个月"));
            mTimeList.add(new InvestProductBean.ProductItem(6,"近半年"));
            mTimeList.add(new InvestProductBean.ProductItem(12,"近一年"));
        }

        mListView.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<TotalIncomingBean.IncomingItem>(mContext, mListItems, R.layout.total_income_item) {
            @Override
            public void convert(ViewHolder helper, TotalIncomingBean.IncomingItem item) {
                helper.setText(R.id.product_name, item.name);
                helper.setText(R.id.product_time, item.createdDate);
                helper.setText(R.id.tv_income,((double)item.profit/100)+ "");
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mStatus = Status.PULL_FROM_START;
                setLastUpdateTime();
                getNewData(mSelectCate,mSelectTime);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage += 1;
                mStatus = Status.PULL_FROM_END;
                getNewData(mSelectCate,mSelectTime);
            }
        });

        //请求产品的列表
        String urlProduct = HttpURLData.APPFUN_PRODUCT_SIMPLE;

        GetNetData tool = new GetNetData();
        tool.requestNetData(new Callback<String>() {
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

                InvestProductBean bean = gson.fromJson(response, InvestProductBean.class);
                if (bean != null && bean.resources != null && bean.resources.size() != 0) {
                    mCateList.clear();
                    mCateList.add(new InvestProductBean.ProductItem(0, "全部产品"));
                    mCateList.addAll(bean.resources);
                }
            }
        }, urlProduct);
        //界面数据的请求
        mUrl = HttpURLData.APPFUN_ACCOUNT_PROFIT;
        mAuth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize",10);
        parse.putValue("pageNo",1);
        ViewPresenter presenterProfit = new ViewPresenter(this,this);
        presenterProfit.getNetDataWithAuth(parse.toString(),mAuth);
    }

    @Override
    protected void setListener() {
        mIncomingCateLl.setOnClickListener(this);
        mIncomingTimeLl.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_total_income);
        mIncomingCateLl = (LinearLayout) findViewById(R.id.income_ll_cate);
        mIncomingTimeLl = (LinearLayout) findViewById(R.id.income_ll_time);
        mCateIv = (ImageView) findViewById(R.id.incoming_tv_cate);
        mTimeIv = (ImageView) findViewById(R.id.income_iv_time);
        mViewShape = findViewById(R.id.view_shape);
        mViewLine = findViewById(R.id.view_line);
        mClose = (ImageView) findViewById(R.id.title_left);
        mListView = (PullToRefreshListView) findViewById(R.id.inverstment_list);
        mListView.setScrollLoadEnabled(true);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvCate = (TextView) findViewById(R.id.tv_cate);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvNoData = (TextView) findViewById(R.id.tv_nodata);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.income_ll_cate:
                showCatePopWindow(mCateList);
                break;
            case R.id.income_ll_time:
                showTimePopWindow(mTimeList);
                break;
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                break;
            default:
                break;
        }
    }

    /**
     * 时间pop
     * @param timeList
     */
    private void showTimePopWindow(final List<InvestProductBean.ProductItem> timeList) {
        RefreshListviewPop pop = new RefreshListviewPop(mContext, timeList, mSelectTime, new RefreshListviewPop.OnPopClickListener() {
            @Override
            public void selectItem(int product) {
                mSelectTime = product;
                mStatus = Status.REFRESH;
                for (int i = 0; i < mTimeList.size(); i++){
                    InvestProductBean.ProductItem item = mTimeList.get(i);
                    if (item.productId == mSelectTime){
                        tvTime.setText(item.name);
                        break;
                    }
                }
                mPage = 1;
                getNewData(mSelectCate,mSelectTime);

            }

            @Override
            public void dismiss() {
                mTimeIv.setImageResource(R.mipmap.arrow_down);
            }

            @Override
            public void showPop() {
                mTimeIv.setImageResource(R.mipmap.arrow_up);
            }
        });

        pop.showAsDropDown(mViewLine);
    }

    /**
     * 点击popitem 时重新请求数据
     * @param mSelectCate
     * @param mSelectTime
     */
    private void getNewData(int mSelectCate, int mSelectTime) {
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize",10);
        parse.putValue("pageNo",mPage);
        if (mSelectCate != 0){
            parse.putValue("productId",mSelectCate);
        }
        if (mSelectTime != 0){
            parse.putValue("timeType",mSelectTime);
        }
        ViewPresenter presenterProfit = new ViewPresenter(this,this);
        presenterProfit.getNetDataWithAuth(parse.toString(),mAuth);
    }


    /**
     * 产品pop
     * @param mCate
     */
    private void showCatePopWindow(List<InvestProductBean.ProductItem> mCate) {
        RefreshListviewPop pop = new RefreshListviewPop(mContext, mCateList, mSelectCate, new RefreshListviewPop.OnPopClickListener() {
            @Override
            public void selectItem(int product) {
                mSelectCate = product;
                mStatus = Status.REFRESH;
                tvCate.setText(mCateList.get(mSelectCate).name);
                mPage = 1;
                getNewData(mSelectCate,mSelectTime);

            }

            @Override
            public void dismiss() {
                mCateIv.setImageResource(R.mipmap.arrow_down);
            }

            @Override
            public void showPop() {
                mCateIv.setImageResource(R.mipmap.arrow_up);
            }
        });

        pop.showAsDropDown(mViewLine);
    }

    /**
     * 其实这里还是分开请求，是最好的
     * @param content
     */
    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();

            TotalIncomingBean bean = gson.fromJson(content, TotalIncomingBean.class);
            if (bean != null && bean.resources != null && bean.resources.size() != 0) {
                boolean hasMoreData = bean.hasNextPage;
                if (mStatus == Status.PULL_FROM_END) {
                    mListItems.addAll(bean.resources);
                } else {
                    mListItems.clear();
                    mListItems.addAll(bean.resources);
                }
                mAdapter.notifyDataSetChanged();
                mListView.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                mListView.onPullUpRefreshComplete();
                mListView.onPullDownRefreshComplete();
                mListView.setHasMoreData(hasMoreData);
            } else {
                if (mPage == 1) {
                    mListView.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isFirst = true;
    @Override
    public void showProgress() {
//        if (mStatus == Status.NO_PULL){
//            if (mType == 0){
//                llLoading.setVisibility(View.VISIBLE);
//                Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
//                ivLoading.startAnimation(mRotateAnim);
//
//            }
//            mType ++;
//        }else if (mStatus == Status.REFRESH){
//            llLoading.setVisibility(View.VISIBLE);
//            Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
//            ivLoading.startAnimation(mRotateAnim);
//        }
        if (isFirst){
            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
            ivLoading.startAnimation(mRotateAnim);
        }
    }

    @Override
    public void hideProgress() {
//        if (mStatus == Status.NO_PULL){
//            if (mType == 2){
//                ivLoading.clearAnimation();
//                llLoading.setVisibility(View.GONE);
//                mType = 0;
//            }
//        }else if (mStatus == Status.REFRESH){
//            ivLoading.clearAnimation();
//            llLoading.setVisibility(View.GONE);
//        }
        if (isFirst){
            isFirst = false;
            ivLoading.clearAnimation();
            llLoading.setVisibility(View.GONE);
        }
    }

    private void setLastUpdateTime() {
        String text = DataUtil.formatDateTime(System.currentTimeMillis());
        mListView.setLastUpdatedLabel(text);
    }
}
