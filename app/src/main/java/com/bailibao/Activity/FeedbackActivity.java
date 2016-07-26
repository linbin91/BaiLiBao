package com.bailibao.Activity;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.base.BaseBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.ResponseData;
import com.bailibao.dialog.LoginDialog;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/16.
 */
public class FeedbackActivity extends BaseActivity implements IGetDataView {

    private ImageView mClose;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private Button btSend;
    private EditText etContact;
    private EditText etContent;

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);
        btSend.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_feedback);
        mClose = (ImageView) findViewById(R.id.title_left);
        llLoading = (LinearLayout) findViewById(R.id.loading_layout);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        btSend = (Button) findViewById(R.id.umeng_fb_send);
        etContact = (EditText) findViewById(R.id.et_contact);
        etContent = (EditText) findViewById(R.id.umeng_fb_reply_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.umeng_fb_send:
                sendFeedback();
                break;
            default:
                break;
        }
    }

    /**
     * 发送反馈的意见
     */
    private void sendFeedback() {
        boolean isLogin = PreferencesUtils.getBoolean(mContext, ConfigsetData.CONFIG_KEY_LOGIN);
        if (!isLogin){
            LoginDialog dialog = new LoginDialog();
            dialog.show(getSupportFragmentManager(),"");
        }else{
            String contact = etContact.getText().toString().trim();
            if (contact == null || TextUtils.isEmpty(contact)){
                Toast.makeText(mContext,"请输入您的联系方式",Toast.LENGTH_SHORT).show();
                return;
            }
            String content = etContent.getText().toString().trim();
            if (content == null || TextUtils.isEmpty(content)){
                Toast.makeText(mContext,"请输入您想要说的",Toast.LENGTH_SHORT).show();
                return;
            }
            String auth = PreferencesUtils.getString(mContext,ConfigsetData.CONFIG_KEY_AUTH);
            String url = HttpURLData.APPFUN_FEEDBACK;
            UrlParse parse = new UrlParse(url);
            parse.putValue("contactWay",contact);
            parse.putValue("content",content);
            ViewPresenter presenter = new ViewPresenter(this);
            presenter.postNetDataWithAuth(parse.toString(),auth);
        }
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            BaseBean bean = gson.fromJson(content,BaseBean.class);
            if (bean.respCode == ResponseData.RESP_CODE_OK){
                finish();
            }
        }
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress() {
        llLoading.setVisibility(View.VISIBLE);
        Animation mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        llLoading.setVisibility(View.GONE);
    }
}
