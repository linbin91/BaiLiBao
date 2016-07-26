package com.bailibao.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.base.BaseFragment;
import com.bailibao.fragment.IndexFragment;
import com.bailibao.fragment.ManagerFragment;
import com.bailibao.fragment.MoreFragment;
import com.bailibao.fragment.PersonFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置手势密码页面
 * Created by apple on 4/11/15.
 */
public class MainActivity extends BaseActivity {

    private FrameLayout mContainer;
    private RadioGroup mMainTabs;
    private RadioButton mSearchBtn, mSoftBtn, mGameBtn, mEntertaimentBtn, mMeBtn;
    private BaseFragment mCurrentFragment;
    private Context mContext;
    private Map<Integer, Integer> mCheckIndexMap = new HashMap<Integer, Integer>();
    private long mExitTime;
    public static final String ACTION_TO_INDEX = "to_index";

    @Override
    protected void initData() {
        mContext = this;
        mMainTabs.check(R.id.tab_index);
        registerBoradcastReceiver();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra("index",0);
        if (index == 3){
            mMainTabs.check(R.id.tab_account);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int index = getIntent().getIntExtra("index",0);
        if (index == 3){
            mMainTabs.check(R.id.tab_account);
        }
    }

    protected void setListener() {

        mMainTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_index:
                        showFragment(IndexFragment.getInstance());
                        break;

                    case R.id.tab_product:
                        showFragment(ManagerFragment.getInstance());
                        break;
                    case R.id.tab_account:
                        showFragment(PersonFragment.getInstance());
                        break;

                    case R.id.tab_mymore:
                        showFragment(MoreFragment.getInstance());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    protected void findView() {
        setContentView(R.layout.main_activity);
        mContainer = (FrameLayout) findViewById(R.id.tab_content);
        mMainTabs = (RadioGroup) findViewById(R.id.tabs);
        mSoftBtn = (RadioButton) findViewById(R.id.tab_index);
        mGameBtn = (RadioButton) findViewById(R.id.tab_product);
        mEntertaimentBtn = (RadioButton)findViewById(R.id.tab_account);
        mMeBtn = (RadioButton)findViewById(R.id.tab_mymore);
    }

    /**
     * 显示5个页签中的一个
     *
     * @param fragment
     */
    public void showFragment(BaseFragment fragment) {
        if (mCurrentFragment == fragment) {
            return;
        }
        FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tab_content, fragment);
        ft.addToBackStack(null);
        mCurrentFragment = fragment;
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {//
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_TO_INDEX);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_TO_INDEX)){
                mMainTabs.check(R.id.tab_index);
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}
