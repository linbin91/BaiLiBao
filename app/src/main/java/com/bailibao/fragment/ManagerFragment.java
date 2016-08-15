package com.bailibao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.Activity.ProductDetailActivity;
import com.bailibao.Activity.WebViewActivity;
import com.bailibao.R;
import com.bailibao.base.BaseFragment;
import com.bailibao.bean.product.ProductBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.DataUtil;
import com.bailibao.util.UrlParse;
import com.bailibao.view.myadapter.CommonAdapter;
import com.bailibao.view.myadapter.ViewHolder;
import com.bailibao.view.pullview.PullToRefreshBase;
import com.bailibao.view.pullview.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by lin on 2016/4/2.
 */
public class ManagerFragment extends BaseFragment implements IGetDataView{

    private  static  ManagerFragment mInstance;
    private PullToRefreshListView mListView;
    private BaseAdapter mAdapter;
    private List<ProductBean.ProductItem> mDatas;
    private Animation mAnimation;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvHelp;

    private final String regularPeriod = "【定期】";
    private final String activityPeriod = "【活期】";

    //页数
    private  int mPage = 1;

    public  enum Status{
        NO_PULL,
        PULL_FROM_START,
        PULL_FROM_END;
    }

    public Status mStatus =  Status.NO_PULL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mView = View.inflate(mContext, R.layout.fragment_product,null);
        findView();
        initData();
    }

    /**
     * 获取数据或者请求数据
     */
    private void initData() {
        ViewPresenter presenter = new ViewPresenter(this,getActivity());
        String url = HttpURLData.APPFUN_PRODUCT_ITEMS;
        UrlParse parse = new UrlParse(url);
        parse.putValue("pageSize",10);
        parse.putValue("pageNo",mPage);
        presenter.getNetData(parse.toString());
    }

    private void setView() {


        mListView.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<ProductBean.ProductItem>(mContext,mDatas,R.layout.product_item) {

            @Override
            public void convert(ViewHolder helper, ProductBean.ProductItem item) {
                helper.setText(R.id.plan_name,item.name);
                if (item.type == 1){
                    helper.setText(R.id.plan_active, activityPeriod);
                }else{
                    helper.setText(R.id.plan_active, regularPeriod);
                }
                helper.setRatingBar(R.id.star_level_rb, item.recommend);
                helper.setText(R.id.plan_kgfe_count, item.leftCount + "");
                helper.setText(R.id.plan_year_interest,item.yield + "");
                if (item.tips == null || TextUtils.isEmpty(item.tips)){
                    helper.setViewGone(R.id.ll_tip);
                }else{
                    if (mAnimation == null){
                        mAnimation = AnimationUtils.loadAnimation(mContext,R.anim.product_alarm);
                    }
                    helper.getView(R.id.plan_msg_iv).setAnimation(mAnimation);
                    mAnimation.start();
                    helper.setText(R.id.tv_tip,item.tips);
                }
                if (item.status == 1){
                    helper.setImageResource(R.id.iv_status,R.mipmap.icon_sell_before);
                }else if (item.status == 2){
                    helper.setImageResource(R.id.iv_status,R.mipmap.icon_sell_begin);
                }else{
                    helper.setImageResource(R.id.iv_status,R.mipmap.icon_sellout);
                }
            }
        });

        mListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                String url = HttpURLData.APPFUN_PRODUCT_ITEMS;
                UrlParse parse = new UrlParse(url);
                parse.putValue("id",mDatas.get(position).id);
                intent.putExtra("url",parse.toString());
                startActivity(intent);
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mStatus = Status.PULL_FROM_START;
                mPage = 1;
                setLastUpdateTime();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mStatus = Status.PULL_FROM_END;
                mPage =+ 1;
                initData();
            }
        });
    }

    private void findView() {

        mListView = (PullToRefreshListView) mView.findViewById(R.id.puduct_list);
        mListView.setScrollLoadEnabled(true);
        llLoading = (LinearLayout) mView.findViewById(R.id.loading_layout);
        ivLoading = (ImageView) mView.findViewById(R.id.iv_loading);
        tvHelp = (TextView) mView.findViewById(R.id.title_right);
        tvHelp.setOnClickListener(this);
    }

    public  static  ManagerFragment getInstance(){
        if (mInstance == null){
            mInstance = new ManagerFragment();
        }
        return  mInstance;
    }

    @Override
    public void onDestroy() {
        mInstance = null;
        mContext = null;
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("title","新手帮助");
                intent.putExtra("path",HttpURLData.APPFUN_USER_HELP);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void fillView(String content) {
        if (content != null && !content.isEmpty()){
            Gson gson = new Gson();
            ProductBean product = gson.fromJson(content,ProductBean.class);
            if (product != null && product.resources != null && product.resources.size() != 0){
                boolean isHasMoreData = true;
                if (mStatus == Status.NO_PULL){
                    mDatas = product.resources;
                    isHasMoreData = product.hasNextPage;
                    setView();
                }else if (mStatus == Status.PULL_FROM_END){
                    isHasMoreData = product.hasNextPage;
                    mDatas.addAll(product.resources);
                }else{
                    mDatas.clear();
                    mDatas.addAll(product.resources);
                    isHasMoreData = product.hasNextPage;
                }
                mAdapter.notifyDataSetChanged();
                mListView.onPullDownRefreshComplete();
                mListView.onPullUpRefreshComplete();
                mListView.setHasMoreData(isHasMoreData);
            }
        }



    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (mStatus == Status.NO_PULL){
            llLoading.setVisibility(View.VISIBLE);
            Animation mRotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
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

    private void setLastUpdateTime() {
        String text = DataUtil.formatDateTime(System.currentTimeMillis());
        mListView.setLastUpdatedLabel(text);
    }

}
