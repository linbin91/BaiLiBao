package com.bailibao.base;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bailibao.Activity.LoginLockActivity;
import com.bailibao.util.LockUtil;

import java.util.List;

/**
 * Created by apple on 4/11/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    protected Context mContext;

    public <K extends View> K getViewById(int id) {
        return (K) getWindow().findViewById(id);
    }

    private  FinishActBroadcast mFinishReceiver;

    public  final static  String FINISH_ACTIVITY = "finish activity";

    public static boolean isShowGestureLock = false;

    public static boolean isFirsrRun = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext=this;
        mFinishReceiver = new FinishActBroadcast();
        startFinishBroadcastReceiver();

        findView();
        setListener();
        initData();

    }

    protected abstract void initData();

    protected abstract void setListener();

    protected abstract void findView();

    @Override
    protected void onStart() {
        super.onStart();
        boolean state = LockUtil.getPwdStatus(mContext);
//        if (isFirsrRun && state){
//            isFirsrRun = false;
//            doSomethingOnScreenOff();
//        }else {
//            if (isShowGestureLock && state){
//                if (isJustSetGestureLock){
//                    isJustSetGestureLock = false;
//                }else{
//                    doSomethingOnScreenOff();
//                }
//            }
//        }
        if (state){
            if (isFirsrRun){
                isFirsrRun = false;
                doSomethingOnScreenOff();
            }else if (isShowGestureLock){
                isShowGestureLock = false;
                doSomethingOnScreenOff();
            }
        }else{
            isFirsrRun = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()){
            isShowGestureLock = true;
        }

    }

    @Override
    protected void onDestroy() {
        if(mFinishReceiver!=null){
            mContext.unregisterReceiver(mFinishReceiver);
        }
        if (!isAppOnForeground()){
            isShowGestureLock = true;
        }
        super.onDestroy();
    }

    /**
     * 打开验证手势
     */
    private void doSomethingOnScreenOff() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), LoginLockActivity.class);
        startActivity(intent);
    }



    /**
     * 启动退出app状态广播接收器
     */
    private void startFinishBroadcastReceiver() {
        try{
            IntentFilter filter = new IntentFilter();
            filter.addAction(FINISH_ACTIVITY);
            mContext.registerReceiver(mFinishReceiver, filter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 自定义的关闭activity的广播
     */
    private  class  FinishActBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FINISH_ACTIVITY)){
                finish();
            }
        }
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
    @Override
    abstract public void onClick(View v);
}
