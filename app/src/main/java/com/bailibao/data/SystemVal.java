package com.bailibao.data;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by lin on 2016/4/10.
 */
public class SystemVal {
    // 硬广长宽比
    public static final double BANNER_LENGTHWIDTH_SCALE = 2;
    public static int appHeadBannerHeight = 0;
    public static float sysDensity = 0;
    public static int sysDensityDpi = 0;
    public static int sysWidth = 0;
    public static int sysHeight = 0;

    public  static  void init(Context ctx){
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();

        sysDensity = metrics.density;
        sysDensityDpi = metrics.densityDpi;
        sysWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
        sysHeight = Math.max(metrics.widthPixels, metrics.heightPixels);

        appHeadBannerHeight = (int) (sysWidth / BANNER_LENGTHWIDTH_SCALE);
    }
}
