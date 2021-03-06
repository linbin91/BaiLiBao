package com.bailibao.Activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.product.ProductDetailBean;
import com.bailibao.bean.product.ProductProfitBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.dialog.LoginDialog;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品详请界面
 *
 *
 */
public class ProductDetailActivity extends BaseActivity implements IGetDataView {

    private ImageView mClose;
    private TextView mTvProductBuy;
    private LinearLayout mLlProductIntroduce;
    private TextView mProductProgress;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvTitle;
    private String mUrl;
    private TextView tvBank;
    private TextView tvYield;
    private TextView tvAlarm;
    private TextView tvLeft;
    private TextView tvName;
    private TextView tvCount;
    private RelativeLayout rlAlarm;
    private RelativeLayout rlProfit;
    private List<ProductProfitBean.ProfitItem> mRrofitDatas = null;
    private  Animation mAnimation;
    private int mScrollCount = 0;

    private int mType = 0;
    private int mId = 0;

    private String mIntroPath;

    //剩余可购买的数量
    private int mLeftCount;
    //距离开售时间
    private long leftTime;

    private LinearLayout llTimeContain;
    private TextView tvTime;

    private SimpleDateFormat mFormat;

    private Handler mHandler = new Handler();

    private boolean canBuy;

    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            Date date = new Date(leftTime);
            String time = mFormat.format(date);
            tvTime.setText(time);
            if (leftTime > 0){
                leftTime -= 1000;
                mHandler.postDelayed(this,1000);
            }else {
                canBuy = true;
                llTimeContain.setVisibility(View.GONE);
                mHandler.removeCallbacks(this);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(timeRunable);
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra("url");

        ViewPresenter presenter = new ViewPresenter(this,this);
        presenter.getNetData(mUrl);

        String profitUrl = HttpURLData.APPFUN_GET_PROFIT;
        ViewPresenter profitpPresenter = new ViewPresenter(this,this);
        profitpPresenter.getNetData(profitUrl);

        mFormat=new SimpleDateFormat("HH:mm:ss");

    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mTvProductBuy.setOnClickListener(this);
        mLlProductIntroduce.setOnClickListener(this);
        mProductProgress.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_product_detail);
        mClose = (ImageView) findViewById(R.id.title_left);
        mTvProductBuy = (TextView) findViewById(R.id.user_buy);
        mLlProductIntroduce = (LinearLayout) findViewById(R.id.product_ll_introduce);
        mProductProgress = (TextView) findViewById(R.id.title_right);
        rlAlarm = (RelativeLayout) findViewById(R.id.rl_alarm);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvTitle = (TextView) findViewById(R.id.title_content);
        tvBank = (TextView) findViewById(R.id.tv_bank);
        tvYield = (TextView) findViewById(R.id.huoqiuplans_fragment_multiple);
        tvAlarm = (TextView) findViewById(R.id.product_tv_alarm);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        rlProfit = (RelativeLayout) findViewById(R.id.rl_profit);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCount = (TextView) findViewById(R.id.tv_count);
        llTimeContain = (LinearLayout) findViewById(R.id.ll_contain);
        tvTime = (TextView) findViewById(R.id.tv_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.user_buy:
                doBuyAction();
                break;
            case R.id.product_ll_introduce:
                doIntroduceAction();
                break;
            case R.id.title_right:
                doProgressAction();
                break;
            default:
                break;
        }
    }

    /**
     * 进入产品的进展页
     */
    private void doProgressAction() {
        Intent intent = new Intent(mContext,ProductProgressActivity.class);
        intent.putExtra("id",mId);
        startActivity(intent);
    }

    /**
     * 进入项目详情页
     */
    private void doIntroduceAction() {
        if (!TextUtils.isEmpty(mIntroPath)){
            Intent intent = new Intent(mContext,WebViewActivity.class);
            intent.putExtra("title","项目介绍");
            intent.putExtra("path",mIntroPath);
            startActivity(intent);
        }
    }

    /**
     * 进入购买的界面
     */
    private void doBuyAction() {
        if (!canBuy){
            Toast.makeText(mContext,"对不起，刚产品还未开始出售，请耐心等待",Toast.LENGTH_SHORT).show();
            return;
        }
        //判断用户登入了没有
        boolean isLogin = PreferencesUtils.getBoolean(mContext, ConfigsetData.CONFIG_KEY_LOGIN);
        if (isLogin){
            Intent intent = new Intent(mContext,ProductBuyActivity.class);
            intent.putExtra("productId",mId);
            startActivity(intent);
        }else{
            //弹出登入dialog
            LoginDialog  mDialog = new LoginDialog();
            mDialog.setData(ConfigsetData.LOGIN_TO_PROTOCOL);
            mDialog.show(getSupportFragmentManager(),"");
        }
    }


    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            if (content.contains("totalProfit")){
                ProductProfitBean bean = gson.fromJson(content,ProductProfitBean.class);
                if (bean != null && bean.resources != null && bean.resources.size() != 0){
                    if (mRrofitDatas == null){
                        mRrofitDatas = new ArrayList<>();
                    }
                    mRrofitDatas.addAll(bean.resources);
                    initAnimation();
                    startTipAnimation();
                }
            }else{
                ProductDetailBean bean = gson.fromJson(content,ProductDetailBean.class);
                tvTitle.setText(bean.name);
                if (bean.type == 1){
                    tvBank.setText(bean.currentTimes + "倍银行活期");
                }else{
                    tvBank.setText(bean.currentTimes + "倍银行定期");
                }
                tvYield.setText(bean.yield + "%");
                if (bean.tips == null || TextUtils.isEmpty(bean.tips)){
                    rlAlarm.setVisibility(View.GONE);
                }else{
                    tvAlarm.setText(bean.tips);
                }
                mLeftCount = bean.leftCount;
                tvLeft.setText(bean.leftCount+"");
                mId = bean.id;
                mIntroPath = bean.introPath;
                leftTime = bean.leftTime;
                if (leftTime == 0){
                    llTimeContain.setVisibility(View.GONE);
                }else{
                    canBuy = false;
                    mHandler.post(timeRunable);
                }
            }
        }
    }

    /**
     * 初始化动画
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
                ProductProfitBean.ProfitItem item = mRrofitDatas.get(mScrollCount % (mRrofitDatas.size()));
                if (item.realName != null){
                    tvName.setText(item.realName);
                    tvCount.setText(item.totalProfit + "");
                    rlProfit.startAnimation(mAnimation);
                }
            }
        });
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mType++;
        if (mType == 1){
            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
            ivLoading.startAnimation(mRotateAnim);
        }

    }

    @Override
    public void hideProgress() {
        if (mType == 2){
            ivLoading.clearAnimation();
            llLoading.setVisibility(View.GONE);
            mType = 0;
        }
    }

    /**
     * tip 轮播
     */
    private void startTipAnimation() {
        rlProfit.setAnimation(mAnimation);
        rlProfit.requestLayout();
        mAnimation.start();
    }
}
