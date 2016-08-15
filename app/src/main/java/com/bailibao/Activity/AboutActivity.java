package com.bailibao.Activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.VersionBean;
import com.bailibao.data.HttpURLData;
import com.bailibao.dialog.KefuDialog;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.SysHelpFun;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/4/16.
 */
public class AboutActivity extends BaseActivity implements IGetDataView {

    private LinearLayout mFeedbackLayout;
    private LinearLayout mShareLayout;
    private LinearLayout mKefuLayout;
    private LinearLayout mCheckLayout;
    private LinearLayout mLoadingLayout;
    private ImageView mLoadingIv;
    private TextView mVersionCode;
    private ImageView mClose;
    @Override
    protected void initData() {
        mVersionCode.setText("当前版本" + SysHelpFun.getAppVersionName(mContext.getApplicationContext()));
    }

    @Override
    protected void setListener() {

        mFeedbackLayout.setOnClickListener(this);
        mShareLayout.setOnClickListener(this);
        mKefuLayout.setOnClickListener(this);
        mCheckLayout.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    @Override
    protected void findView() {

        setContentView(R.layout.about);
        mFeedbackLayout = (LinearLayout) findViewById(R.id.about_feedback_ll);
        mShareLayout = (LinearLayout) findViewById(R.id.about_share_ll);
        mKefuLayout = (LinearLayout) findViewById(R.id.about_call_ll);
        mCheckLayout = (LinearLayout) findViewById(R.id.about_checkversion_ll);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        mLoadingIv = (ImageView) findViewById(R.id.iv_loading);
        mVersionCode = (TextView) findViewById(R.id.about_version_code);
        mClose = (ImageView) findViewById(R.id.title_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_feedback_ll:
                toFeedbackActivity();
                break;
            case R.id.about_share_ll:
                shareApp();
                break;
            case R.id.about_call_ll:
                showKefuDialog();
                break;
            case R.id.about_checkversion_ll:
                checkNewVersion();
                break;
            case R.id.title_left:
                finish();
            default:
                break;
        }
    }

    private void checkNewVersion() {
        ViewPresenter presenter = new ViewPresenter(this,this);
        UrlParse parse = new UrlParse(HttpURLData.APPFUN_VERSION_CODE);
        parse.putValue("type","1");
        presenter.getNetData(parse.toString());
    }

    /**
     * 显示客服的dialog
     */
    private void showKefuDialog() {
        KefuDialog dialog = new KefuDialog();
        dialog.show(getSupportFragmentManager(),"kefu");
    }

    private void shareApp() {
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle("百利宝理财app分享");
        oks.setText("百利宝理财app真好用，赚大钱");
        oks.setTitleUrl("www.baidu.com");
        oks.setSite("nihao");
        oks.setUrl("www.baidu.com");
        oks.show(mContext);
    }

    private void toFeedbackActivity() {
        Intent intent = new Intent(mContext,FeedbackActivity.class);
        startActivity(intent);
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            VersionBean bean = gson.fromJson(content,VersionBean.class);
            if (bean != null){
                String version = bean.version;
                String myVersion = SysHelpFun.getAppVersionName(mContext.getApplicationContext());
                try {
                    double dVersion = Double.parseDouble(version);
                    double dMyVersion = Double.parseDouble(myVersion);
                    if (dVersion > dMyVersion){
                        Toast.makeText(this,"快去应用宝下载新的版本吧",Toast.LENGTH_SHORT).show();
                    }else if (dVersion == dMyVersion){
                        Toast.makeText(this,"已经是最新版本了哦",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        mLoadingIv.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        mLoadingLayout.setVisibility(View.GONE);
        mLoadingIv.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(mContext);
    }
}
