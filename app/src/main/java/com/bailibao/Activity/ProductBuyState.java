package com.bailibao.Activity;

import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.module.view.IGetDataView;

/**
 * Created by Administrator on 2016/4/25.
 *
 * 购买产品的状态
 */
public class ProductBuyState extends BaseActivity implements IGetDataView{

    private ImageView mClose;
    private TextView mTvContent;
    private LinearLayout mLoadingLayout;
    private ImageView mLoadingIv;
    private ImageView mIvState;
    private TextView mTvState;
    private TextView mTvBuyExplain;
    private LinearLayout mllProductSuccess;
    private TextView mTitleRight;

    @Override
    protected void initData() {
//        String[] keywords = new String[] { "完成", "账单" };
//
//        String str = getResources().getString(R.string.product_buy_loading);
//        SpannableStringBuilder builder = new SpannableStringBuilder(getResources().getString(R.string.product_buy_loading));
//        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.fragment_new_user_buy));
//        for (int i = 0; i < keywords.length; i++) {
//         Pattern p = Pattern.compile(keywords[i]);
//           Matcher m = p.matcher(str);
//            while (m.find()) {
//                int start = m.start();
//               int end = m.end();
//
//                builder.setSpan(redSpan, start, end,
//                                      Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                }
//            }
////        builder.setSpan(redSpan,34,38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////        builder.setSpan(redSpan,48,51, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        mTvContent.setText(builder);

        SpannableString ss = new SpannableString(getResources().getString(R.string.product_buy_loading));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 34, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 48, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvContent.setText(ss);
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        mTitleRight.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_buy_state);
        mClose = (ImageView) findViewById(R.id.title_left);
        mTvContent = (TextView) findViewById(R.id.product_tv_content);
        mIvState = (ImageView) findViewById(R.id.state_iv);
        mTvState = (TextView) findViewById(R.id.state_tv);
        mTvBuyExplain = (TextView) findViewById(R.id.product_tv_content);
        mllProductSuccess = (LinearLayout) findViewById(R.id.product_ll_succuss);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        mLoadingIv = (ImageView) findViewById(R.id.iv_loading);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mTitleRight = (TextView) findViewById(R.id.title_right);

        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        mLoadingIv.startAnimation(mRotateAnim);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mLoadingIv.clearAnimation();
                mLoadingLayout.setVisibility(View.GONE);
                mIvState.setImageResource(R.mipmap.product_state_sucess);
                mTvState.setText("已抢购成功");
                mTvBuyExplain.setVisibility(View.GONE);
                mllProductSuccess.setVisibility(View.VISIBLE);

            }
        };
        handler.postDelayed(runnable,2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                finish();
            default:
                break;
        }
    }

    @Override
    public void fillView(String content) {

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
}
