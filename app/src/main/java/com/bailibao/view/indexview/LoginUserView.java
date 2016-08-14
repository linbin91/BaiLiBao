package com.bailibao.view.indexview;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.bean.IndexLoginBean;
import com.bailibao.bean.TipBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.view.customeview.DigetFlipper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 */
public class LoginUserView extends BaseView implements IGetDataView{

    private TextView mScollLable;
    private List<TipBean.TipItem> mLableData;
    private int mScrollCount = 0;
    private  Animation mAnimation;

    private DigetFlipper mUpFlipper1;
    private DigetFlipper mUpFlipper2;
    private DigetFlipper mUpFlipper3;
    private DigetFlipper mUpFlipper4;

    private DigetFlipper mDownFlipper1;
    private DigetFlipper mDownFlipper2;
    private DigetFlipper mDownFlipper3;
    private DigetFlipper mDownFlipper4;

    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvIncoming;
    private TextView tvRank;
    private TextView tvTopProduct;
    private TextView tvDownProduct;

    private String mAuth;
    //请求服务端次数，因为要tip接口分开了，然后所需两次数据回来，才消除loading
    private int mGetDataCount = 0;
    private boolean isShowLoading = false;
    private boolean isAnimation = false;

    public LoginUserView(Context context) {
        super(context);
        setData();
        initAnimation();
    }

    /**
     *初始化动画
     */
    private void initAnimation() {
        mAnimation = AnimationUtils.loadAnimation(mContext,R.anim.scroll_lable_anim);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mScrollCount++;
                TipBean.TipItem item = mLableData.get(mScrollCount % (mLableData.size()));
                if (item.phone != null && item.name != null && item.lapsedTime != null){
                    String tip = item.lapsedTime + "前" + item.phone + "购买了" + item.count + "份" + item.name;
                    mScollLable.setText(tip);
                }
            }
        });
    }

    private void setData() {
        mAuth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);

        ViewPresenter mPresenter = new ViewPresenter(this,mContext);
        String indexLogin = HttpURLData.APPFUN_INDEX_LOGIN;
        mPresenter.getNetDataWithAuth(indexLogin,mAuth);

        ViewPresenter tipPresenter = new ViewPresenter(this,mContext);
        String tipUrl = HttpURLData.APPFUN_BUY_TIP;
        tipPresenter.getNetDataWithAuth(tipUrl,mAuth);
    }

    @Override
    public void initView() {
        mView = mInflater.inflate(R.layout.fragment_login_user,null);
        mScollLable = (TextView) mView.findViewById(R.id.fragment_tv_lable);

        mUpFlipper1 = (DigetFlipper) mView.findViewById(R.id.flipper1);
        mUpFlipper2 = (DigetFlipper) mView.findViewById(R.id.flipper2);
        mUpFlipper3 = (DigetFlipper) mView.findViewById(R.id.flipper3);
        mUpFlipper4 = (DigetFlipper) mView.findViewById(R.id.flipper4);

        mDownFlipper1 = (DigetFlipper) mView.findViewById(R.id.small_flipper1);
        mDownFlipper2 = (DigetFlipper) mView.findViewById(R.id.small_flipper2);
        mDownFlipper3 = (DigetFlipper) mView.findViewById(R.id.small_flipper3);
        mDownFlipper4 = (DigetFlipper) mView.findViewById(R.id.small_flipper4);

        llLoading = (LinearLayout) mView.findViewById(R.id.loading_layout);
        ivLoading = (ImageView) mView.findViewById(R.id.iv_loading);
        tvIncoming = (TextView) mView.findViewById(R.id.tv_yesterday_incoming);
        tvRank = (TextView) mView.findViewById(R.id.rank);
        tvTopProduct = (TextView) mView.findViewById(R.id.tv_top_pruduct);
        tvDownProduct = (TextView) mView.findViewById(R.id.tv_down_product);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void fillView(String content) {
        //购买提示广播
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            if (content.contains("lapsedTime")){
                TipBean tips = gson.fromJson(content,TipBean.class);
                if (tips != null && tips.resources != null && tips.resources.size() != 0){
                    if (mLableData == null){
                        mLableData = new ArrayList<>();
                    }
                    mLableData.addAll(tips.resources);
                    isAnimation = true;
                    startTipAnimation();
                }
            }else{
                IndexLoginBean loginBean = gson.fromJson(content,IndexLoginBean.class);
                if (loginBean != null){
                    setIndexView(loginBean);
                }
            }
        }

    }

    /**
     * 获取到数据，设置界面
     * @param loginBean
     */
    private void setIndexView(IndexLoginBean loginBean) {

        tvIncoming.setText("昨日在投收益:" +((float)loginBean.lastProfit / 100)+"元");
        if (loginBean.rank == 0){
            tvRank.setText("暂无排名");
        }else{
            tvRank.setText("排名第"+loginBean.rank);
        }
        if (loginBean.indexProducts != null && loginBean.indexProducts.size() != 0){
            if (loginBean.indexProducts.size() == 1){
                tvTopProduct.setText(loginBean.indexProducts.get(0).name);
            }else if (loginBean.indexProducts.size() == 2){
                tvTopProduct.setText(loginBean.indexProducts.get(0).name);
                tvDownProduct.setText(loginBean.indexProducts.get(1).name);

                mUpFlipper1.setDigit(loginBean.indexProducts.get(0).yield / 10);
                mUpFlipper2.setDigit(loginBean.indexProducts.get(0).yield % 10);
                mDownFlipper1.setDigit(loginBean.indexProducts.get(1).yield / 10);
                mDownFlipper2.setDigit(loginBean.indexProducts.get(1).yield % 10);;
            }
        }


    }

    /**
     * tip 轮播
     */
    public void startTipAnimation() {

        if (isAnimation){
            TipBean.TipItem item = mLableData.get(mScrollCount % (mLableData.size()));
            if (item.phone != null && item.name != null && item.lapsedTime != null) {
                String tip = item.lapsedTime + "前" + item.phone + "购买了" + item.count + "份" + item.name;
                mScollLable.setText(tip);
            }
            mScollLable.setAnimation(mAnimation);
            mScollLable.requestLayout();
            mAnimation.start();
        }

    }

    @Override
    public void toast(String msg) {
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mGetDataCount++;
        if (!isShowLoading){
            isShowLoading = true;
            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
            ivLoading.startAnimation(mRotateAnim);
        }

    }

    @Override
    public void hideProgress() {
        if (mGetDataCount == 2){
            ivLoading.clearAnimation();
            llLoading.setVisibility(View.GONE);
            isShowLoading = false;
        }

    }
}
