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
import com.bailibao.bean.user.UserTransactionBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.Status;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 *
 * 交易记录界面
 */
public class TransactionRecordActivity extends BaseActivity implements IGetDataView{

    private ImageView mClose;
    private LinearLayout mRecordOperate;
    private LinearLayout mRecordCate;
    private LinearLayout mRecordTime;
    private ImageView mIvOperate;
    private ImageView mIvTime;
    private ImageView mIvCate;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvTime;
    private TextView tvOpreate;
    private TextView tvProduct;

    private List<InvestProductBean.ProductItem> mOperateList;
    private List<InvestProductBean.ProductItem> mTimeList;
    private List<InvestProductBean.ProductItem> mCateList;

    private int mSelectTime ;
    private int mSelectOperate;
    private int mSelectCate;

    private PullToRefreshListView mListView;
    private BaseAdapter mAdapter;

    private int mType = 0;
    private int mPage = 1;

    private Status mStatus = Status.NO_PULL;
    private String mUrl = HttpURLData.APPFUN_USER_TRADE;
    private String mAuth;


    private List<UserTransactionBean.TransactionBean> mListItems = new ArrayList<>();

    @Override
    protected void initData() {
        if (mOperateList == null){
            mOperateList = new ArrayList<>();
            mOperateList.add(new InvestProductBean.ProductItem(0,"全部操作"));
            mOperateList.add(new InvestProductBean.ProductItem(1,"买入"));
            mOperateList.add(new InvestProductBean.ProductItem(2,"赎回"));
        }
        if (mTimeList == null){
            mTimeList = new ArrayList<>();
            mTimeList.add(new InvestProductBean.ProductItem(0,"全部时间"));
            mTimeList.add(new InvestProductBean.ProductItem(1,"近一个月"));
            mTimeList.add(new InvestProductBean.ProductItem(3,"近三个月"));
            mTimeList.add(new InvestProductBean.ProductItem(6,"近半年"));
            mTimeList.add(new InvestProductBean.ProductItem(12,"近一年"));
        }
        if (mCateList == null){
            mCateList = new ArrayList<>();
        }


        mListView.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<UserTransactionBean.TransactionBean>(mContext, mListItems, R.layout.total_income_item) {
            @Override
            public void convert(ViewHolder helper, UserTransactionBean.TransactionBean item) {
                helper.setText(R.id.product_name, item.name);
                helper.setText(R.id.product_time, item.tradeDate);
                if (item.type == 1){
                    //购买
                    helper.setText(R.id.tv_income, "-"+ (double)item.payMoney / 100,R.color.tixian_text);
                }else {
                    helper.setText(R.id.tv_income,"+"+(double)item.payMoney / 100+ "");
                }
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mStatus = Status.PULL_FROM_START;
                setLastUpdateTime();
                getNewData(mSelectCate,mSelectTime,mSelectOperate);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage += 1;
                mStatus = Status.PULL_FROM_END;
                getNewData(mSelectCate,mSelectTime,mSelectOperate);
            }
        });

        //请求产品的列表
        String urlProduct = HttpURLData.APPFUN_PRODUCT_SIMPLE;
        ViewPresenter presenterProduct = new ViewPresenter(this);
        presenterProduct.getNetData(urlProduct);

        //界面数据的请求
        mUrl = HttpURLData.APPFUN_USER_TRADE;
        mAuth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize",10);
        parse.putValue("pageNo",1);
        ViewPresenter presenterProfit = new ViewPresenter(this);
        presenterProfit.getNetDataWithAuth(parse.toString(),mAuth);
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mRecordOperate.setOnClickListener(this);
        mRecordCate.setOnClickListener(this);
        mRecordTime.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_transaction_record);
        mClose = (ImageView) findViewById(R.id.title_left);
        mRecordTime = (LinearLayout) findViewById(R.id.record_ll_time);
        mRecordCate = (LinearLayout) findViewById(R.id.record_ll_cate);
        mRecordOperate = (LinearLayout) findViewById(R.id.record_ll_operate);

        mIvOperate = (ImageView) findViewById(R.id.record_iv_operate);
        mIvTime = (ImageView) findViewById(R.id.record_iv_time);
        mIvCate = (ImageView) findViewById(R.id.record_iv_cate);

        mListView = (PullToRefreshListView) findViewById(R.id.record_list);
        mListView.setScrollLoadEnabled(true);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);

        tvTime = (TextView) findViewById(R.id.tv_time);
        tvOpreate = (TextView) findViewById(R.id.tv_operate);
        tvProduct = (TextView) findViewById(R.id.tv_product);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.record_ll_time:
                showTimePopWindow(mTimeList);
                break;
            case R.id.record_ll_cate:
                showCatePopWindow(mCateList);
                break;
            case R.id.record_ll_operate:
                showOperatePopWindow(mOperateList);
                break;
            default:
                break;
        }
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            if (content.contains("productId")){
                InvestProductBean bean = gson.fromJson(content,InvestProductBean.class);
                if (bean != null && bean.resources!= null && bean.resources.size() != 0){

                    mCateList.clear();
                    mCateList.add(new InvestProductBean.ProductItem(0,"全部产品"));
                    mCateList.addAll(bean.resources);
                }

            }else{
                if (content.contains("tradeDate")){
                    UserTransactionBean bean = gson.fromJson(content,UserTransactionBean.class);
                    if (bean != null && bean.resources != null && bean.resources.size() != 0){
                        boolean hasMoreData = bean.hasNextPage;
                        if (mStatus == Status.PULL_FROM_END){
                            mListItems.addAll(bean.resources);
                        }else{
                            mListItems.clear();
                            mListItems.addAll(bean.resources);
                        }
                        mAdapter.notifyDataSetChanged();
                        mListView.onPullUpRefreshComplete();
                        mListView.onPullDownRefreshComplete();
                        mListView.setHasMoreData(hasMoreData);
                    }
                }
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
            if (mType == 0){
                llLoading.setVisibility(View.VISIBLE);
                Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
                ivLoading.startAnimation(mRotateAnim);

            }
            mType ++;
        }else if (mStatus == Status.REFRESH){
            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
            ivLoading.startAnimation(mRotateAnim);
        }
    }

    @Override
    public void hideProgress() {
        if (mStatus == Status.NO_PULL){
            if (mType == 2){
                ivLoading.clearAnimation();
                llLoading.setVisibility(View.GONE);
                mType = 0;
            }
        }else if (mStatus == Status.REFRESH){
            ivLoading.clearAnimation();
            llLoading.setVisibility(View.GONE);
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

                getNewData(mSelectCate,mSelectTime,mSelectOperate);
            }

            @Override
            public void dismiss() {
                mIvTime.setImageResource(R.mipmap.arrow_down);
            }

            @Override
            public void showPop() {
                mIvTime.setImageResource(R.mipmap.arrow_up);
            }
        });

        pop.showAsDropDown(mRecordTime);
    }

    /**
     * 操作pop
     * @param mCate
     */
    private void showOperatePopWindow(List<InvestProductBean.ProductItem> mCate) {
        RefreshListviewPop pop = new RefreshListviewPop(mContext, mOperateList, mSelectOperate, new RefreshListviewPop.OnPopClickListener() {
            @Override
            public void selectItem(int product) {
                mSelectOperate = product;
                mStatus = Status.REFRESH;
                getNewData(mSelectCate,mSelectTime,mSelectOperate);
                tvOpreate.setText(mOperateList.get(mSelectOperate).name);
            }

            @Override
            public void dismiss() {
                mIvOperate.setImageResource(R.mipmap.arrow_down);
            }

            @Override
            public void showPop() {
                mIvOperate.setImageResource(R.mipmap.arrow_up);
            }
        });

        pop.showAsDropDown(mRecordTime);
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
                tvProduct.setText(mCateList.get(mSelectCate).name);
                getNewData(mSelectCate,mSelectTime,mSelectOperate);
            }

            @Override
            public void dismiss() {
                mIvCate.setImageResource(R.mipmap.arrow_down);
            }

            @Override
            public void showPop() {
                mIvCate.setImageResource(R.mipmap.arrow_up);
            }
        });

        pop.showAsDropDown(mRecordTime);
    }

    private void setLastUpdateTime() {
        String text = DataUtil.formatDateTime(System.currentTimeMillis());
        mListView.setLastUpdatedLabel(text);
    }

    /**
     * 点击popitem 时重新请求数据
     * @param mSelectCate
     * @param mSelectTime
     */
    private void getNewData(int mSelectCate, int mSelectTime, int mSelectOperate) {
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize",10);
        parse.putValue("pageNo",1);
        if (mSelectCate != 0){
            parse.putValue("productId",mSelectCate);
        }
        if (mSelectTime != 0){
            parse.putValue("timeType",mSelectTime);
        }
        if (mSelectOperate != 0){
            parse.putValue("optType",mSelectOperate);
        }
        ViewPresenter presenterProfit = new ViewPresenter(this);
        presenterProfit.getNetDataWithAuth(parse.toString(),mAuth);
    }
}
