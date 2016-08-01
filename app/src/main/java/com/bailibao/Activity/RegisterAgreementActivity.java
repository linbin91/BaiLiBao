package com.bailibao.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.data.HttpURLData;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.UrlParse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/7/30.
 */
public class RegisterAgreementActivity extends BaseActivity implements IGetDataView {

    @InjectView(R.id.title_left)
    ImageView titleLeft;
    @InjectView(R.id.title_content)
    TextView titleContent;
    @InjectView(R.id.about_rl_tilte)
    RelativeLayout aboutRlTilte;
    @InjectView(R.id.safety_title)
    RelativeLayout safetyTitle;
    @InjectView(R.id.content)
    TextView tvContent;
    @InjectView(R.id.safe_intruction_next_btn)
    TextView safeIntructionNextBtn;
    @InjectView(R.id.safe_intruction_ctv)
    CheckBox safeIntructionCtv;
    @InjectView(R.id.safe_intruction_agree_tv)
    TextView safeIntructionAgreeTv;
    @InjectView(R.id.iv_loading)
    ImageView ivLoading;
    @InjectView(R.id.loading_layout)
    LinearLayout loadingLayout;

    @Override
    protected void initData() {

        ViewPresenter presenter = new ViewPresenter(this);
        UrlParse parse = new UrlParse(HttpURLData.APPFUN_USER_AGREEMENT);
        presenter.getNetData(parse.toString());
        titleContent.setText("注册说明");
    }

    @Override
    protected void setListener() {
        titleLeft.setOnClickListener(this);
        safeIntructionNextBtn.setOnClickListener(this);
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_safety_intruction);
        ButterKnife.inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.safe_intruction_next_btn:
                doNextAction();
                break;
            default:
                break;
        }
    }

    /**
     * 点击下一步的操作
     */
    private void doNextAction() {
        if (safeIntructionCtv.isChecked()){
            Intent intent = new Intent(mContext,RegisterActivity.class);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(mContext,"请同意协议后再进行注册",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void fillView(String content) {
        if (content != null && !content.isEmpty()){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> map = objectMapper.readValue(content, Map.class);
                tvContent.setText(map.get("content"));
            } catch (IOException e) {
                e.printStackTrace();
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
        Animation mRotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        ivLoading.startAnimation(mRotateAnim);
    }

    @Override
    public void hideProgress() {
        ivLoading.clearAnimation();
        loadingLayout.setVisibility(View.GONE);
    }
}
