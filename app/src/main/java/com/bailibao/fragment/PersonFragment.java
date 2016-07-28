package com.bailibao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.Activity.FuiouWebviewActivity;
import com.bailibao.Activity.InvestmentActivity;
import com.bailibao.Activity.MainActivity;
import com.bailibao.Activity.MyBankCardActivity;
import com.bailibao.Activity.RechargeActivity;
import com.bailibao.Activity.TotalIncomeActivity;
import com.bailibao.Activity.TransactionRecordActivity;
import com.bailibao.Activity.UserBalanceActivity;
import com.bailibao.R;
import com.bailibao.base.BaseFragment;
import com.bailibao.bean.PersonAccountBean;
import com.bailibao.bean.user.BankCardBean;
import com.bailibao.data.ConfigsetData;
import com.bailibao.data.HttpURLData;
import com.bailibao.dialog.LoginDialog;
import com.bailibao.module.presenter.ViewPresenter;
import com.bailibao.module.view.IGetDataView;
import com.bailibao.util.PreferencesUtils;
import com.bailibao.util.StringUtil;
import com.bailibao.util.UrlParse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lin on 2016/4/2.
 */
public class PersonFragment extends BaseFragment implements IGetDataView{

    private static PersonFragment mInstance;
    private RelativeLayout mIncomeHave;
    private RelativeLayout mIncomeTotal;
    private RelativeLayout mRlBalance;
    private RelativeLayout mRlRecord;
    private RelativeLayout mRlBankCard;
    private LinearLayout llLoading;
    private ImageView ivLoading;
    private TextView tvYesterdayIncoming;
    private TextView tvTotalIncoming;
    private TextView tvInvestingMoney;
    private TextView tvCount;
    private TextView tvRight;

    private boolean hasGetNetData = false;
    private float mCount;
    private ViewPresenter mPresenter;
    private  int type =1 ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mView = View.inflate(mContext, R.layout.fragment_person, null);
        findView();
        setView();
        setListener();

    }

    /**
     * 如果没有登入，则必须要显示login的dilog
     */

    private void getData() {
        boolean isLogin = PreferencesUtils.getBoolean(mContext, ConfigsetData.CONFIG_KEY_LOGIN);
        if (isLogin){
            //请求网络数据
            if (!hasGetNetData){
                hasGetNetData = true;
                String url = HttpURLData.APPFUN_PERSON_ACCOUNT;
                if (mPresenter == null){
                    mPresenter = new ViewPresenter(this);
                }
                mPresenter = new ViewPresenter(this);
                String auth = PreferencesUtils.getString(mContext,ConfigsetData.CONFIG_KEY_AUTH);
                mPresenter.getNetDataWithAuth(url,auth);
                type = 1;
            }
        }else{
            LoginDialog dilog = new LoginDialog();
            dilog.setListener(new LoginDialog.OnClickCallBack() {
                @Override
                public void doCancelAction() {
                    //点击取消，然后发个广播出去，跳转到首页去
                    Intent intent = new Intent(MainActivity.ACTION_TO_INDEX);
                    mContext.sendBroadcast(intent);
                }
            });
            dilog.show(getFragmentManager(),"");
        }
    }

    private void setListener() {
        mIncomeHave.setOnClickListener(this);
        mIncomeTotal.setOnClickListener(this);
        mRlBalance.setOnClickListener(this);
        mRlRecord.setOnClickListener(this);
        mRlBankCard.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    private void setView() {


    }

    private void findView() {
        mIncomeHave = (RelativeLayout) mView.findViewById(R.id.rel_income_have);
        mIncomeTotal= (RelativeLayout) mView.findViewById(R.id.rel_income_total);
        mRlBalance = (RelativeLayout) mView.findViewById(R.id.rel_count);
        mRlRecord = (RelativeLayout) mView.findViewById(R.id.rel_order);
        mRlBankCard = (RelativeLayout) mView.findViewById(R.id.rel_bank);
        tvYesterdayIncoming = (TextView) mView.findViewById(R.id.fragment_iv_incoming);
        tvTotalIncoming = (TextView) mView.findViewById(R.id.tv_income_total_pre);
        tvInvestingMoney = (TextView) mView.findViewById(R.id.tv_income_have_pre);
        tvCount = (TextView) mView.findViewById(R.id.tv_count);
        llLoading = (LinearLayout) mView.findViewById(R.id.loading_layout);
        ivLoading = (ImageView) mView.findViewById(R.id.iv_loading);
        tvRight = (TextView) mView.findViewById(R.id.title_right);
     }

    public static PersonFragment getInstance() {
        if (mInstance == null) {
            mInstance = new PersonFragment();
        }

        return mInstance;
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
            case R.id.rel_income_have:
                toInvestmentActivity();
                break;
            case R.id.rel_income_total:
                ToTotalIncomeActivity();
                break;
            case R.id.rel_count:
                doUserBalance();
                break;
            case R.id.rel_order:
                doTransactionRecord();
                break;
            case R.id.rel_bank:
                doMyBankCard();
                break;
            case R.id.title_right:
                doRecharge();
            default:
                break;
        }
    }

    /**
     * 进入充值的界面
     */
    private void doRecharge() {
        ///

        String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
        mPresenter.getNetDataWithAuth(HttpURLData.APPFUN_BANK_CARD, auth);
        type = 3;
    }

    /**
     * 进入我的银行卡界面
     */
    private void doMyBankCard() {
        Intent intent = new Intent(mContext, MyBankCardActivity.class);
        startActivity(intent);
    }

    /***
     * 进入交易记录界面
     */
    private void doTransactionRecord() {
        Intent intent = new Intent(mContext, TransactionRecordActivity.class);
        startActivity(intent);
    }

    /**
     * 进入账户余额界面
     */
    private void doUserBalance() {
        Intent intent = new Intent(mContext, UserBalanceActivity.class);
        intent.putExtra("money",mCount);
        startActivity(intent);
    }


    /**
     * 累计收益
     */
    private void ToTotalIncomeActivity() {
        Intent intent = new Intent(mContext, TotalIncomeActivity.class);
        startActivity(intent);
    }

    /**
     * 在投资金界面
     */
    private void toInvestmentActivity() {
        Intent intent = new Intent(mContext, InvestmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    @Override
    public void fillView(String content) {
        if (content != null && !TextUtils.isEmpty(content)){
            Gson gson = new Gson();
            if (type == 1){
                showPersonAcountView(content, gson);
            }else if (type == 2){

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        Map<String,String> map = objectMapper.readValue(content,Map.class);
                        gotoWebView(map);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }else if (type == 3){

                BankCardBean bean = gson.fromJson(content, BankCardBean.class);
                if (bean != null && bean.resources != null && bean.resources.size() != 0) {
                    //跳转到充值
                    Intent intent = new Intent(getActivity(), RechargeActivity.class);
                    startActivity(intent);
                }else{
                    //跳转到注册页面
                    String auth = PreferencesUtils.getString(mContext, ConfigsetData.CONFIG_KEY_AUTH);
                    if (auth != null && !TextUtils.isEmpty(auth)) {
                        UrlParse parse = new UrlParse(HttpURLData.APPFUN_MONEY_REGISTER);

                        if (mPresenter == null) {
                            mPresenter = new ViewPresenter(this);
                        }
                        mPresenter.getNetDataWithAuth(parse.toString(), auth);

                        type = 2;
                    }
                }
            }
        }
    }


    /**
     * 跳转到富友的h5
     *
     * @param map
     */
    private void gotoWebView(Map map) {
        if (map != null){
            String postData = StringUtil.makePostHTML(HttpURLData.JZH_API_APP_WEB_REG_URL,map);
            if (postData != null){
                Intent intent = new Intent(getActivity(),FuiouWebviewActivity.class);
                intent.putExtra(HttpURLData.INTENT_API_DATA_KEY_DATA, postData);
                startActivity(intent);
            }
        }
    }

    /*
    * 填充个人账户的界面
     */
    private void showPersonAcountView(String content, Gson gson) {
        PersonAccountBean bean = gson.fromJson(content, PersonAccountBean.class);
        if (bean != null){
            tvYesterdayIncoming.setText((float)bean.lastProfit / 100 + "");
            tvTotalIncoming.setText((float)bean.totalProfit / 100 + "");
            tvInvestingMoney.setText((float)bean.investingMoney / 100 + "");
            tvCount.setText((double)bean.balance/ 100 + "");
            mCount = (float)bean.balance/ 100;
            PreferencesUtils.putFloat(mContext, ConfigsetData.CONFIG_KEY_LEFT_MONEY,(float)bean.balance/ 100);
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
}
