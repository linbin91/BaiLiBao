package com.bailibao.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/4/22.
 */
public class SysHelpFun {

    private static int VERSION_CODE;
    private static String VERSION_NAME;

    /**
     * 获取当前版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        if (VERSION_CODE <= 0) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = null;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                VERSION_CODE = pi.versionCode;
            } catch (Exception e) {
            }
        }

        return VERSION_CODE;
    }


    public static String getAppVersionName(Context context) {
        if (VERSION_CODE <= 0) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = null;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                VERSION_NAME = pi.versionName;
            } catch (Exception e) {
            }
        }

        return VERSION_NAME;
    }

}
