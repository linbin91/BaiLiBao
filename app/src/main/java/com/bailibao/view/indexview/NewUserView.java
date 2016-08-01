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
import com.bailibao.bean.user.IndexUnloginBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.ResponseData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/23.
 */
public class NewUserView extends BaseView implements IGetDataView{

    private TextView mNewUserBuy;
    private OnBuyCallBack mListener;

    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvYield;
    private TextView tvIndroduct;
    private IndexUnloginBean bean;

    public NewUserView(Context context) {
        super(context);
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        String url = HttpURLData.APPFUN_INDEX_UNLOGIN;
        ViewPresenter presenter = new ViewPresenter(this);
        presenter.getNetData(url);
    }

    @Override
    public void initView() {
        mView = mInflater.inflate(R.layout.fragment_new_user,null);
        mNewUserBuy = (TextView) mView.findViewById(R.id.new_user_buy);
        llLoading = (LinearLayout) mView.findViewById(R.id.loading_layout);
        ivLoading = (ImageView) mView.findViewById(R.id.iv_loading);
        tvYield = (TextView) mView.findViewById(R.id.huoqiuplans_fragment_multiple);
        tvIndroduct = (TextView) mView.findViewById(R.id.tv_indrodct);

        getData();
    }

    @Override
    public void setListener() {
        mNewUserBuy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_user_buy:
                doBuyAction();
                break;
            default:
                break;
        }
    }

    private void doBuyAction() {
        if (mListener != null){
            mListener.buyAction(bean.id);
        }

//        Intent intent = new Intent(mContext, ProductBuyActivity.class);
//        mContext.startActivity(intent);
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            bean = gson.fromJson(content, IndexUnloginBean.class);
            if (bean != null && bean.respCode == ResponseData.RESP_CODE_OK){
                tvYield.setText(bean.yield + "%");
                tvIndroduct.setText(bean.introduce);
            }

        }
    }


    @Override
    public void toast(String msg) {
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        llLoading.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        llLoading.setVisibility(View.GONE);
    }



    public interface  OnBuyCallBack{
        public void  buyAction(int id);
    }

    public void setListener(OnBuyCallBack listener){
        mListener = listener;
    }
}
