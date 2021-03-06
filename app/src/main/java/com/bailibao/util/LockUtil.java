package com.bailibao.util;


import android.content.Context;
import android.text.TextUtils;

import com.bailibao.data.ConfigsetData;
import com.bailibao.entity.Point;


/**
 * 手势密码工具类
 * Created by apple on 4/11/15.
 */
public class LockUtil {

    /**
     * 1=30度 2=45度 4=60度
     * @return
     */
    public static float switchDegrees(float x, float y) {
        return (float) MathUtil.pointTotoDegrees(x, y);
    }

    /**
     * 获取角度
     * @param a
     * @param b
     * @return
     */
    public static float getDegrees(Point a, Point b) {
        float ax = a.x;// a.index % 3;
        float ay = a.y;// a.index / 3;
        float bx = b.x;// b.index % 3;
        float by = b.y;// b.index / 3;
        float degrees = 0;
        if (bx == ax) // y轴相等 90度或270
        {
            if (by > ay) // 在y轴的下边 90
            {
                degrees = 90;
            } else if (by < ay) // 在y轴的上边 270
            {
                degrees = 270;
            }
        } else if (by == ay) // y轴相等 0度或180
        {
            if (bx > ax) // 在y轴的下边 90
            {
                degrees = 0;
            } else if (bx < ax) // 在y轴的上边 270
            {
                degrees = 180;
            }
        } else {
            if (bx > ax) // 在y轴的右边 270~90
            {
                if (by > ay) // 在y轴的下边 0 - 90
                {
                    degrees = 0;
                    degrees = degrees+ switchDegrees(Math.abs(by - ay), Math.abs(bx - ax));
                } else if (by < ay) // 在y轴的上边 270~0
                {
                    degrees = 360;
                    degrees = degrees- switchDegrees(Math.abs(by - ay), Math.abs(bx - ax));
                }

            } else if (bx < ax) // 在y轴的左边 90~270
            {
                if (by > ay) // 在y轴的下边 180 ~ 270
                {
                    degrees = 90;
                    degrees = degrees+ switchDegrees(Math.abs(bx - ax),Math.abs(by - ay));
                } else if (by < ay) // 在y轴的上边 90 ~ 180
                {
                    degrees = 270;
                    degrees = degrees- switchDegrees(Math.abs(bx - ax),Math.abs(by - ay));
                }

            }

        }
        return degrees;
    }

    /**
     * 点在圆内
     * @param sx
     * @param sy
     * @param r
     * @param x
     * @param y
     * @return
     */
    public static boolean checkInRound(float sx, float sy, float r, float x,float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }

    /**
     * 清空本地密码
     * @param context
     */
    public static void clearPwd(Context context){
        PreferencesUtils.putString(context,"handpswd","");
    }

    /**
     * 获取本地密码
     * @return
     */
    public static int[] getPwd(Context context){
        String phone = PreferencesUtils.getString(context,ConfigsetData.CONFIG_KEY_LOGIN_NUM);
        String str= PreferencesUtils.getString(context,phone);
        if(str != null){
            String[] s=str.split(",");
            int[] indexs = new int[s.length];
            if(s.length>1){
                for(int i=0;i<s.length;i++){
                    indexs[i]=Integer.valueOf(s[i]);
                }
            }
            return indexs;
        }
        return new int[]{};
    }

    /**
     * 是否开启手势密码
     * true:开启 false:关闭
     */
    public static void setPwdStatus(Context context,boolean flag){
        PreferencesUtils.putBoolean(context, ConfigsetData.CONFIG_KEY_HAND_PASSWORD, flag);
    }

    /**
     * 获取当前是否开启手势密码
     * true:开启 false:关闭
     * @param context
     * @return
     */
    public static boolean getPwdStatus(Context context){
        return PreferencesUtils.getBoolean(context,ConfigsetData.CONFIG_KEY_HAND_PASSWORD,false);
    }

    /**
     * 将密码设置到本地
     * @param context
     * @param mIndexs
     */
    public static void setPwdToDisk(Context context,int[] mIndexs){
        String str="";
        for(int i:mIndexs){
            str+=i+",";
        }
        String phone = PreferencesUtils.getString(context,ConfigsetData.CONFIG_KEY_LOGIN_NUM);
        if (phone != null && !TextUtils.isEmpty(phone)){
            PreferencesUtils.putString(context,phone,str);
        }
    }
}
