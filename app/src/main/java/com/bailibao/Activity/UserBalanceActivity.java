package com.bailibao.Activity;

import android.content.Intent;
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
import com.bailibao.bean.user.UserCountBean;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/26.
 * 账户余额界面
 */
public class UserBalanceActivity extends BaseActivity implements IGetDataView {

    @InjectView(R.id.balance_tv_recharge)
    TextView balanceTvRecharge;
    @InjectView(R.id.balance_tv_postal)
    TextView balanceTvPostal;
    private ImageView mClose;
    private LinearLayout mBalanceOperate;
    private LinearLayout mBalanceTime;
    private List<InvestProductBean.ProductItem> mOperateList;
    private List<InvestProductBean.ProductItem> mTimeList;
    private PullToRefreshListView mListView;

    private int mSelectTime;
    private int mSelectOperate;
    private BaseAdapter mAdapter;
    private Status mStatus = Status.NO_PULL;
    private String mUrl = HttpURLData.APPFUN_ACCOUNT_PROFIT;
    private String mAuth;
    private int mType = 0;
    private int mPage = 1;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private ImageView mOperateIv;
    private ImageView mTimeIv;
    private List<UserCountBean.CountItem> mListItems = new ArrayList<>();
    private TextView tvTime;
    private TextView tvOperate;
    private TextView tvMoney;
    private int type;
    private ViewPresenter mPresenter;
    @Override
    protected void initData() {

        tvMoney.setText(getIntent().getFloatExtra("money", 0) + "");
        if (mTimeList == null) {
            mTimeList = new ArrayList<>();
            mTimeList.add(new InvestProductBean.ProductItem(0, "全部时间"));
            mTimeList.add(new InvestProductBean.ProductItem(1, "近一个月"));
            mTimeList.add(new InvestProductBean.ProductItem(3, "近三个月"));
            mTimeList.add(new InvestProductBean.ProductItem(6, "近半年"));
            mTimeList.add(new InvestProductBean.ProductItem(12, "近一年"));
        }
        if (mOperateList == null) {
            mOperateList = new ArrayList<>();
            mOperateList.add(new InvestProductBean.ProductItem(0, "全部操作"));
            mOperateList.add(new InvestProductBean.ProductItem(1, "充值"));
            mOperateList.add(new InvestProductBean.ProductItem(2, "提现"));
        }

        mListView.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<UserCountBean.CountItem>(mContext, mListItems, R.layout.total_income_item) {
            @Override
            public void convert(ViewHolder helper, UserCountBean.CountItem item) {
                if (item.type == 1) {
                    helper.setText(R.id.product_name, "充值");
                } else {
                    helper.setText(R.id.product_name, "提现");
                }
                helper.setText(R.id.product_time, item.tradeDate);
                helper.setText(R.id.tv_income, (double) item.payMoney / 100 + "");
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mStatus = Status.PULL_FROM_START;
                setLastUpdateTime();
                getNewData(mSelectOperate, mSelectTime);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage += 1;
                mStatus = Status.PULL_FROM_END;
                getNewData(mSelectOperate, mSelectTime);
            }
        });

        //界面数据的请求
        mUrl = HttpURLData.APPFUN_USER_ACCOUT;
        mAuth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize", 10);
        parse.putValue("pageNo", 1);
        ViewPresenter presenterProfit = new ViewPresenter(this);
        presenterProfit.getNetDataWithAuth(parse.toString(), mAuth);
        type = 1;
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mBalanceOperate.setOnClickListener(this);
        mBalanceTime.setOnClickListener(this);
        balanceTvRecharge.setOnClickListener(this);
        balanceTvPostal.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_user_balance);
        mClose = (ImageView) findViewById(R.id.title_left);
        mBalanceOperate = (LinearLayout) findViewById(R.id.balance_ll_operate);
        mBalanceTime = (LinearLayout) findViewById(R.id.balance_ll_time);
        mListView = (PullToRefreshListView) findViewById(R.id.balance_list);
        mListView.setScrollLoadEnabled(true);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        mOperateIv = (ImageView) findViewById(R.id.balance_iv_oprate);
        mTimeIv = (ImageView) findViewById(R.id.balance_iv_time);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvOperate = (TextView) findViewById(R.id.tv_operate);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        ButterKnife.inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.balance_ll_operate:
                showCatePopWindow(mOperateList);
                break;
            case R.id.balance_ll_time:
                showTimePopWindow(mTimeList);
                break;
            case R.id.balance_tv_recharge:
                doRechargeAction();
                break;
            case R.id.balance_tv_postal:
                doPostalAction();
                break;
            default:
                break;
        }
    }

    /**
     *提现
     */
    private void doPostalAction() {
        Intent intent = new Intent(this,WithdrawActivity.class);
        intent.putExtra("account",tvMoney.getText());
        startActivity(intent);
    }


    /**
     * 充值界面
     */
    private void doRechargeAction() {
        Intent intent = new Intent(this,RechargeActivity.class);
        startActivity(intent);
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)) {
            if (type == 1) {
                Gson gson = new Gson();

                UserCountBean bean = gson.fromJson(content, UserCountBean.class);
                if (bean != null && bean.resources != null && bean.resources.size() != 0) {
                    boolean hasMoreData = bean.hasNextPage;
                    if (mStatus == Status.PULL_FROM_END) {
                        mListItems.addAll(bean.resources);
                    } else {
                        mListItems.clear();
                        mListItems.addAll(bean.resources);
                    }
                    mListView.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mListView.onPullUpRefreshComplete();
                    mListView.onPullDownRefreshComplete();
                    mListView.setHasMoreData(hasMoreData);
                } else {
                    mListView.setVisibility(View.INVISIBLE);
                    TextView emptyView = (TextView) findViewById(R.id.empty);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        }


    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (mStatus == Status.NO_PULL || mStatus == Status.REFRESH) {

            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
            ivLoading.startAnimation(mRotateAnim);

        }
    }

    @Override
    public void hideProgress() {
        if (mStatus == Status.NO_PULL || mStatus == Status.REFRESH) {
            ivLoading.clearAnimation();
            llLoading.setVisibility(View.GONE);

        }

    }

    /**
     * 时间pop
     *
     * @param timeList
     */
    private void showTimePopWindow(final List<InvestProductBean.ProductItem> timeList) {
        RefreshListviewPop pop = new RefreshListviewPop(mContext, timeList, mSelectTime, new RefreshListviewPop.OnPopClickListener() {
            @Override
            public void selectItem(int product) {
                mSelectTime = product;
                mStatus = Status.REFRESH;
                for (int i = 0; i < mTimeList.size(); i++) {
                    InvestProductBean.ProductItem item = mTimeList.get(i);
                    if (item.productId == mSelectTime) {
                        tvTime.setText(item.name);
                        break;
                    }
                }

                getNewData(mSelectOperate, mSelectTime);
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

        pop.showAsDropDown(mBalanceOperate);
    }


    /**
     * 操作pop
     *
     * @param mCate
     */

    private void showCatePopWindow(List<InvestProductBean.ProductItem> mCate) {
        RefreshListviewPop pop = new RefreshListviewPop(mContext, mOperateList, mSelectOperate, new RefreshListviewPop.OnPopClickListener() {
            @Override
            public void selectItem(int product) {
                mSelectOperate = product;
                mStatus = Status.REFRESH;
                tvOperate.setText(mOperateList.get(mSelectOperate).name);
                getNewData(mSelectOperate, mSelectTime);
            }

            @Override
            public void dismiss() {
                mOperateIv.setImageResource(R.mipmap.arrow_down);
            }

            @Override
            public void showPop() {
                mOperateIv.setImageResource(R.mipmap.arrow_up);
            }
        });

        pop.showAsDropDown(mBalanceOperate);
    }

    private void setLastUpdateTime() {
        String text = DataUtil.formatDateTime(System.currentTimeMillis());
        mListView.setLastUpdatedLabel(text);
    }

    /**
     * 点击popitem 时重新请求数据
     *
     * @param mSelectCate
     * @param mSelectTime
     */
    private void getNewData(int mSelectCate, int mSelectTime) {
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize", 10);
        parse.putValue("pageNo", 1);
        if (mSelectCate != 0) {
            parse.putValue("optType", mSelectCate);
        }
        if (mSelectTime != 0) {
            parse.putValue("timeType", mSelectTime);
        }
        ViewPresenter presenterProfit = new ViewPresenter(this);
        presenterProfit.getNetDataWithAuth(parse.toString(), mAuth);
        type = 1;
    }

}
