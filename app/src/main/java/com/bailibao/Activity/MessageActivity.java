package com.bailibao.Activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.bean.MessageBean;
import com.bailibao.bean.base.BaseBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.data.ResponseData;
import com.bailibao.data.Status;
import com.bailibao.dialog.DeleteMeassgeDialog;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.DataUtil;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.UrlParse;
import com.bailibao.view.myadapter.CommonAdapter;
import com.bailibao.view.myadapter.ViewHolder;
import com.bailibao.view.pullview.PullToRefreshBase;
import com.bailibao.view.pullview.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MessageActivity extends BaseActivity implements IGetDataView {
    private TextView mNoMessage;
    private PullToRefreshListView mMessageContainer;
    private List<MessageBean.MessageItem> mMessageList = new ArrayList<>();
    private BaseAdapter mAdapter;
    private ImageView mClose;
    private DeleteMeassgeDialog dialog;
    private RelativeLayout rlNoMessage;
    private int mPages = 1;
    private Status mStatus = Status.NO_PULL;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private ViewPresenter mPresenter;
    private String mUrl;
    private String mAuth;
    private int type = 0;
    private int mDeletePosition = 0;
    @Override
    protected void initData() {

        dialog = new DeleteMeassgeDialog();

        mMessageContainer.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<MessageBean.MessageItem>(mContext, mMessageList, R.layout.message_item) {

            @Override
            public void convert(ViewHolder helper, MessageBean.MessageItem item) {

                helper.setImageResource(R.id.icon,R.mipmap.message_system);
                helper.setText(R.id.title,item.title);
                helper.setText(R.id.msg,item.content);
                helper.setText(R.id.time, item.createdDate);
                if (item.status != 0){
                    //已读
                    helper.setViewGone(R.id.iv_status);
                }
            }
        });

        mMessageContainer.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("title","消息详情");
                intent.putExtra("path",mMessageList.get(position).path);
                startActivity(intent);
                if (mMessageList.get(position).status == 0){
                    doReadAction(position);
                }



            }
        });

        mMessageContainer.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                dialog.show(getSupportFragmentManager(),"");
                dialog.setListener(new DeleteMeassgeDialog.onClickDeleteCallBack() {
                    @Override
                    public void onDeleteAction() {
                        doDeleteAction(position);
                    }
                });
                return false;
            }
        });

        mMessageContainer.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPages = 1;
                mStatus = Status.PULL_FROM_START;
                setLastUpdateTime();
                getMessage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPages += 1;
                mStatus = Status.PULL_FROM_END;
                getMessage();
            }
        });

        mUrl = HttpURLData.APPFUN_MESSAGE;
        mAuth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);

        getMessage();
    }

    private void doReadAction(int position) {
        mMessageList.get(position).status = 1;
        mAdapter.notifyDataSetChanged();
        String url = HttpURLData.APPFUN_MESSAGE_READ;
        UrlParse parse = new UrlParse(url);
        parse.putValue("id",mMessageList.get(position).id);
        mPresenter.postNetDataWithAuth(parse.toString(),mAuth);
    }

    /**
     * 删除消息的操作
     */
    private void doDeleteAction(int position) {
        mDeletePosition = position;
        ViewPresenter presenter = new ViewPresenter(this,this);
        String url = HttpURLData.APPFUN_DELETE_MESSAGE;
        UrlParse parse = new UrlParse(url);
        parse.putValue("id",mMessageList.get(position).id);
        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        presenter.postNetDataWithAuth(parse.toString(),auth);
        type = 2;
    }
    @Override
    protected void setListener() {
        mClose.setOnClickListener(this);

    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_message);
        mMessageContainer = (PullToRefreshListView) findViewById(R.id.message_list);
        mMessageContainer.setScrollLoadEnabled(true);
        mClose = (ImageView) findViewById(R.id.title_left);
        rlNoMessage = (RelativeLayout) findViewById(R.id.rl_no_message);
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

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            if (type == 1){
                MessageBean messages = gson.fromJson(content, MessageBean.class);
                if (messages != null && messages.resources != null && messages.resources.size() != 0){
                    boolean hasMoreData = messages.hasNextPage;;
                    if (mStatus == Status.NO_PULL){
                        mMessageList.addAll(messages.resources);

                    }else if (mStatus == Status.PULL_FROM_END){
                        mMessageList.addAll(messages.resources);
                    }else{
                        mMessageList.clear();
                        mMessageList.addAll(messages.resources);
                    }
                    mAdapter.notifyDataSetChanged();
                    mMessageContainer.onPullUpRefreshComplete();
                    mMessageContainer.onPullDownRefreshComplete();
                    mMessageContainer.setHasMoreData(hasMoreData);
                }else{
                    rlNoMessage.setVisibility(View.VISIBLE);
                }
            }else{
                BaseBean baseBean = gson.fromJson(content,BaseBean.class);
                if (baseBean != null){
                    if (baseBean.respCode == ResponseData.RESP_CODE_OK){
                        mMessageList.remove(mDeletePosition);
                        if (mMessageList.size() == 0){
                            mMessageContainer.setVisibility(View.GONE);
                            rlNoMessage.setVisibility(View.VISIBLE);
                        }else{
                            mAdapter.notifyDataSetChanged();
                        }
                    }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void  getMessage(){
        UrlParse parse = new UrlParse(mUrl);
        parse.putValue("pageSize",10);
        parse.putValue("pageNo",mPages);

        if (mPresenter == null){
            mPresenter = new ViewPresenter(this,this);
        }
        mPresenter.getNetDataWithAuth(parse.toString(),mAuth);

        type = 1;
    }

    private void setLastUpdateTime() {
        String text = DataUtil.formatDateTime(System.currentTimeMillis());
        mMessageContainer.setLastUpdatedLabel(text);
    }

}
