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
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.product.ProductProgressBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.Status;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.DataUtil;
import com.bailibao.util.UrlParse;
import com.bailibao.view.myadapter.CommonAdapter;
import com.bailibao.view.myadapter.ViewHolder;
import com.bailibao.view.pullview.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品进展详情
 * Created by Administrator on 2016/4/25.
 */
public class ProductProgressActivity extends BaseActivity implements IGetDataView{

    private PullToRefreshListView mListView;
    private ImageView mClose;
    private LinearLayout llLoading;
    private ImageView ivLoading;

    private BaseAdapter mAdapter;
    private List<ProductProgressBean.ProgressItem> mProgressList = new ArrayList<>();
    private int mPage = 1;
    private Status mStatus = Status.NO_PULL;

    @Override
    protected void initData() {

        mListView.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<ProductProgressBean.ProgressItem>(mContext,mProgressList,R.layout.progress_item) {

            @Override
            public void convert(ViewHolder helper, ProductProgressBean.ProgressItem item) {
                helper.setImageByUrl(R.id.icon,item.imageUrl);
                helper.setText(R.id.title,item.title);
                helper.setText(R.id.msg,item.content);
            }
        });

        mListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductProgressActivity.this,WebViewActivity.class);

                intent.putExtra("title",bean.resources.get(position).title);
                intent.putExtra("path",bean.resources.get(position).path);
                startActivity(intent);
            }
        });

//        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                mPage = 1;
//                mStatus = Status.PULL_FROM_START;
//                setLastUpdateTime();
//                getData();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                mPage += 1;
//                mStatus = Status.PULL_FROM_END;
//                getData();
//            }
//        });
        mListView.setHasMoreData(false);
        getData();
    }

    private void getData() {
        int id = getIntent().getIntExtra("id",0);
        String url = HttpURLData.APPFUN_PRODUCT_PROGRESS;
        UrlParse urlParse = new UrlParse(url);
        urlParse.putValue("pageSize",10);
        urlParse.putValue("pageNo",mPage);
        urlParse.putValue("productId",id);
        ViewPresenter presenter = new ViewPresenter(this,this);
        presenter.getNetData(urlParse.toString());
    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_product_progress);
        mClose = (ImageView) findViewById(R.id.title_left);
        mListView = (PullToRefreshListView) findViewById(R.id.progress_list);
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

    private ProductProgressBean bean;
    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)) {
            Gson gson = new Gson();

            bean = gson.fromJson(content, ProductProgressBean.class);
            if (bean != null && bean.resources != null && bean.resources.size() != 0) {
                boolean hasMoreData = bean.hasNextPage;
                if (mStatus == Status.NO_PULL) {
                    mProgressList.addAll(bean.resources);

                } else if (mStatus == Status.PULL_FROM_END) {
                    mProgressList.addAll(bean.resources);
                } else {
                    mProgressList.clear();
                    mProgressList.addAll(bean.resources);
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

    private void setLastUpdateTime() {
        String text = DataUtil.formatDateTime(System.currentTimeMillis());
        mListView.setLastUpdatedLabel(text);
    }
}
