package com.bailibao.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

import com.bailibao.R;
import com.bailibao.bean.AdBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.SystemVal;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.view.myadapter.AdImageAdapter;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lin on 2016/4/10.
 *
 * 用于实现广告滚动效果 ViewFlipperBusiness
 */
public class AdViewFlipperView implements IGetDataView {
    private ViewPager mAdViewPager;
    private AdImageAdapter mAdapter;
    private Context mContext;
    private ListView mListView;

    private List<AdBean.AdItem> mAdList = new LinkedList<>();
    private int totalPage = 1;
    // 广告地址
    public String url = "";
    private LinearLayout mHeadView;
    private SquarePageIndicator mIndicator;
    protected int viewPagerOrder = 1;
    private static final int TIME_SPAN = 5000;
    private static final int INIT_POS = 1000;
    private FrameLayout mAdview;
    private boolean isAutoFlow = true;
    private Handler mHanlder = new Handler();

    private Handler handler = new Handler();
    private Runnable mSetPagerItemRunnable;

    public AdViewFlipperView(Context context, String url) {
        this.mContext = context;
        this.url = url;
    }

    public View getHeadView() {

        mHeadView = (LinearLayout) View.inflate(mContext, R.layout.ad_layout, null);

        mAdview = (FrameLayout) mHeadView.findViewById(R.id.adview);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAdview.getLayoutParams();
        lp.height = SystemVal.appHeadBannerHeight;
        mAdview.setLayoutParams(lp);

        mAdViewPager = (ViewPager) mHeadView.findViewById(R.id.ad_pager);
        try {
            // View
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            Scroller mScroller = new FixedSpeedScroller(mAdViewPager.getContext(), new AccelerateInterpolator());
            mField.set(mAdViewPager, mScroller);
            Field flingField = ViewPager.class.getDeclaredField("mMinimumVelocity");
            flingField.setAccessible(true);
            ViewConfiguration configuration = ViewConfiguration.get(mContext);
            int mMinimumVelocity = configuration.getScaledMinimumFlingVelocity() / 2;
            flingField.setInt(mAdViewPager, mMinimumVelocity);
        } catch (Exception e) {

        }
        mIndicator = (SquarePageIndicator) mHeadView.findViewById(R.id.ad_indicator);
        mIndicator.setSnap(true);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mPos = INIT_POS * mAdList.size();

            @Override
            public void onPageSelected(int arg0) {
                if (mPos > arg0) {
                    viewPagerOrder = -1;
                } else {
                    viewPagerOrder = 1;
                }
                mPos = arg0;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (!isAutoFlow) {
                            startAutoFlowTimer();
                        }
                        break;

                    case ViewPager.SCROLL_STATE_DRAGGING:
                        stopAutoFlowTimer();
                        break;

                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;

                    default:
                        if (!isAutoFlow) {
                            startAutoFlowTimer();
                        }
                        break;
                }
            }
        });
        mAdapter = new AdImageAdapter(mContext,mAdList);
        initData();

        return mHeadView;
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

    private void notifyChange() {
        // totalPage = (mAdList.size() + 2 - 1) / 2;
        if (mAdapter == null || mAdViewPager == null || mAdList == null || mIndicator == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
        totalPage = mAdList.size();
        mAdViewPager.setAdapter(mAdapter);

        if (mAdview != null) {
            mAdview.setBackgroundResource(R.drawable.transparent);
        }
        if (mAdapter.getCount() > 1) {
            mAdViewPager.setCurrentItem(INIT_POS * mAdList.size());
            mIndicator.setVisibility(View.VISIBLE);
            mIndicator.setTotalPage(totalPage);
            mIndicator.setViewPager(mAdViewPager, 0);
            startAutoFlowTimer();
        } else {
            mIndicator.setVisibility(View.GONE);
        }

    }

    /**
     * 请求图片的url 与act
     */
    private void initData() {
        if (!TextUtils.isEmpty(url)) {
            //请求广告
            ViewPresenter presenter = new ViewPresenter(this);
            presenter.getNetData(HttpURLData.APPFUN_GET_AD);
        }
        mAdview.setVisibility(View.VISIBLE);
    }

    /**
     * 更新界面
     * @param adList
     */
    private void updateAdView(List<AdBean.AdItem> adList) {
        // 发生异常
        if (adList == null) {
            return;
        }
        notifyChange();
    }


    // 通过延迟消息实现自动播放，使用时通过调用该方法来启动自动播放功能
    public void startAutoFlowTimer() {
        if (isAutoFlow) {
            stopAutoFlowTimer();
        }
        isAutoFlow = true;
        if (handler == null) {
            handler = new Handler();
        }
        if (mSetPagerItemRunnable == null) {
            mSetPagerItemRunnable = new Runnable() {
                public void run() {
                    if (mAdViewPager != null && handler != null) {
                        mAdViewPager.setCurrentItem(mAdViewPager.getCurrentItem() + viewPagerOrder);
                        handler.postDelayed(mSetPagerItemRunnable, TIME_SPAN);
                    }
                }
            };
        }
        handler.removeCallbacks(mSetPagerItemRunnable);
        handler.postDelayed(mSetPagerItemRunnable, TIME_SPAN);
    }

    // 停止自动播放
    public void stopAutoFlowTimer() {
        isAutoFlow = false;
        if (handler != null) {
            handler.removeCallbacks(mSetPagerItemRunnable);
        }
    }



    // 内存回收
    public void clear() {
        stopAutoFlowTimer();
        if (mAdViewPager != null) {
            mAdViewPager.removeAllViews();
            mAdViewPager = null;
            mAdList.clear();
        }
        mAdapter = null;
        mAdList = null;
    }

    public void updateAd(String adUrl) {
        url = adUrl;
        initData();
    }

    public AdViewFlipperView getParent() {
        return this;
    }


    @Override
    public void fillView(String content) {
        mAdview.setVisibility(View.VISIBLE);
        if (content != null && !content.isEmpty()){
            Gson gson = new Gson();
            AdBean bean = gson.fromJson(content,AdBean.class);
            if (bean != null && bean.resources != null && bean.resources.size() > 0){
                mAdList.addAll(bean.resources);
                updateAdView(mAdList);
            }
        }
    }
}
