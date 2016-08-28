package com.bailibao.Activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.product.ProductBuyResult;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

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
    private TextView tvOrderId;
    private TextView tvBuyCount;
    @Override
    protected void initData() {
        int productId = getIntent().getIntExtra("productId",0);
        int buyCount = getIntent().getIntExtra("buyCount",0);
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        if (auth != null && !TextUtils.isEmpty(auth)) {
            UrlParse parse = new UrlParse(HttpURLData.APPFUN_PRODUCT_PAY);

            parse.putValue("orderId",productId);
            parse.putValue("count", buyCount);
            ViewPresenter presenter = new ViewPresenter(this,this);
            presenter.postNetDataWithAuth(parse.toString(), auth);
        }

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
        tvBuyCount = (TextView) findViewById(R.id.product_buy_count);
        tvOrderId = (TextView) findViewById(R.id.product_tv_oreder_no);
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
        if (content != null && !content.isEmpty()){
            Gson gson = new Gson();
            ProductBuyResult bean = gson.fromJson(content,ProductBuyResult.class);
            changeView(bean);
        }
    }

    private void changeView(ProductBuyResult bean) {
        if (bean != null){
            mIvState.setImageResource(R.mipmap.product_state_sucess);
            mTvState.setText("已抢购成功");
            mTvBuyExplain.setVisibility(View.GONE);
            mllProductSuccess.setVisibility(View.VISIBLE);
            tvBuyCount.setText(bean.count+"");
            tvOrderId.setText(bean.orderNo);
        }
    }

    @Override
    public void toast(String msg) {
        mIvState.setImageResource(R.mipmap.product_state_error);
        mTvState.setText("抢购失败");
        mTvBuyExplain.setText(msg);
        mllProductSuccess.setVisibility(View.GONE);
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        mLoadingIv.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        mLoadingIv.clearAnimation();
        mLoadingLayout.setVisibility(View.GONE);
    }

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
}
