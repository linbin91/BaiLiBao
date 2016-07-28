package com.bailibao.Activity;

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
import com.bailibao.bean.product.ProgressDetailBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 项目进展详情列表
 */
public class ProgressDetailActivity extends BaseActivity implements IGetDataView {

    @InjectView(R.id.title_left)
    ImageView titleLeft;
    @InjectView(R.id.title_content)
    TextView titleContent;
    @InjectView(R.id.authen_rl_tilte)
    RelativeLayout authenRlTilte;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.content)
    TextView tvContent;
    @InjectView(R.id.iv_loading)
    ImageView ivLoading;
    @InjectView(R.id.loading_layout)
    LinearLayout loadingLayout;
    private ViewPresenter mPresenter;

    @Override
    protected void initData() {
        int id = getIntent().getIntExtra("id",0);
        mPresenter = new ViewPresenter(this);
        request(id);
    }

    private void request(int id) {
        String url = HttpURLData.APPFUN_PRODUCT_PROGRESS;
        UrlParse parse = new UrlParse(url);
        parse.putValue("id",id);
        mPresenter.getNetData(parse.toString());
    }

    @Override
    protected void setListener() {
        titleLeft.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_progress_detail);
        ButterKnife.inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void fillView(String content){
        if (content != null && !content.isEmpty()){
            Gson gson = new Gson();
            ProgressDetailBean bean = gson.fromJson(content,ProgressDetailBean.class);
            if (bean != null){
                tvTitle.setText(bean.title);
                tvTime.setText(bean.createdDate);
                tvContent.setText(bean.content);
            }
        }

    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        loadingLayout.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        loadingLayout.setVisibility(View.GONE);
    }
}
