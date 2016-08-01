package com.bailibao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.Activity.MessageActivity;
import com.bailibao.R;
import com.bailibao.base.BaseFragment;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.dialog.LoginDialog;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.view.AdViewFlipperView;
import com.bailibao.view.indexview.LoginUserView;
import com.bailibao.view.indexview.NewUserView;

/**
 * Created by lin on 2016/4/10.
 */
public class IndexFragment extends BaseFragment implements IGetDataView {


    private static IndexFragment mInstance;
    private ImageView mTitleRight;
    //ad广告的容器
    private LinearLayout mFragmentAd;
    //具体展示的内容
    private LinearLayout mFragmentContent;
    private AdViewFlipperView mAdViewFlipper;
    private TextView mTextBuy;
    private LayoutInflater inflater;

    //没有登入过的购买页面
    private View mNewUserView;
    private NewUserView mNewUser;
    private LoginDialog mDialog;

    //登入过的
    private View mLoginUserView;
    private LoginUserView mLoginUser;

    private LinearLayout llLoading;
    private ImageView ivLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mView = View.inflate(mContext, R.layout.fragment_index, null);
        inflater = LayoutInflater.from(mContext);
        findView();

        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    private void setListener() {
        mTitleRight.setOnClickListener(this);
    }


    private void setView() {

        mFragmentAd.removeAllViews();
        mFragmentContent.removeAllViews();

        mAdViewFlipper = new AdViewFlipperView(mContext, HttpURLData.APPFUN_GET_AD);
        mFragmentAd.addView(mAdViewFlipper.getHeadView());
        boolean isLogin = PreferencesUtils.getBoolean(mContext, ConfigsetData.CONFIG_KEY_LOGIN);
        if (isLogin){
            showloginView();
        }else{
            showUnLoginView();
        }
    }

    /**
     * 显示登录过的view
     */
    private void showloginView() {
        if (mLoginUser == null){
            mLoginUser = new LoginUserView(mContext);
        }
        mLoginUser.startTipAnimation();
        mLoginUserView = mLoginUser.getView();
        mFragmentContent.addView(mLoginUserView);
    }


    /**
     * 显示没有登录的view
     */
    private void showUnLoginView() {
        if (mNewUser == null){
            mNewUser = new NewUserView(mContext);
        }
        mNewUserView = mNewUser.getView();
        mNewUser.setListener(new NewUserView.OnBuyCallBack() {
            @Override
            public void buyAction(int id) {
                if (mDialog == null){
                    mDialog = new LoginDialog();
                }
                mDialog.setData(ConfigsetData.LOGIN_TO_BUY);
                mDialog.setProductId(id);
                mDialog.show(getFragmentManager(),"");
            }
        });

        mFragmentContent.addView(mNewUserView);
    }

    private void findView() {
        mTitleRight = (ImageView) mView.findViewById(R.id.title_right);
        mFragmentAd = (LinearLayout) mView.findViewById(R.id.fragment_ll_ad);
        mFragmentContent = (LinearLayout) mView.findViewById(R.id.fragment_ll_content);
        llLoading = (LinearLayout) mView.findViewById(R.id.loading_layout);
        ivLoading = (ImageView) mView.findViewById(R.id.iv_loading);
    }

    public static IndexFragment getInstance() {
        if (mInstance == null) {
            mInstance = new IndexFragment();
        }

        return mInstance;
    }

    @Override
    public void onDestroy() {
        if (mAdViewFlipper != null) {
            mAdViewFlipper.clear();
            mAdViewFlipper = null;
        }
        mInstance = null;
        mContext = null;
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_right:
                    toMessageActivity();
                    break;
                default:
                    break;
            }
    }

    private void toMessageActivity() {
        //消息需要登入后才可以看到
        boolean isLogin = PreferencesUtils.getBoolean(mContext, ConfigsetData.CONFIG_KEY_LOGIN);
        if (isLogin){
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            startActivity(intent);
        }else{
            if (mDialog == null){
                mDialog = new LoginDialog();
            }
            mDialog.show(getFragmentManager(),"");
        }

    }



    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdViewFlipper != null) {
            mAdViewFlipper.stopAutoFlowTimer();
        }
    }

    @Override
    public void fillView(String content) {

    }
}
