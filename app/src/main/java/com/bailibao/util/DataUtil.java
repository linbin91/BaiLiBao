package com.bailibao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/12.
 */
public class DataUtil {
    public  static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }
}
