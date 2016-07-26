package com.bailibao.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lin on 2016/4/2.
 *打印log 的工具
 */
public class LogUtil {
    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;
    public static final int NO_LOG = 5;

    private static int LOG_LEVEL = NO_LOG;
    private static boolean logFileEnable = false;

    private static LogUtil instance = null;

    static {
        try {
            // 通过SD卡配置文件启用日志
            if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
                String path = Environment.getExternalStorageDirectory() + File.separator + "log91.properties";
                if ((new File(path).exists())) {
                    InputStream inputStream = new FileInputStream(path);
                    Properties p = new Properties();
                    p.load(inputStream);
                    if ("true".equals(p.getProperty("logcat"))) {
                        // 开启logcat
                        LOG_LEVEL = VERBOSE;
                    }
                    if ("true".equals(p.getProperty("filelog"))) {
                        // 开启文件日志
                        logFileEnable = true;
                    }
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static LogUtil getInstance() {
        if (instance == null) {
            instance = new LogUtil();
        }
        return instance;
    }

    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":" + st.getLineNumber() + " " + st.getMethodName()
                    + " ]";
        }
        return null;
    }

    // verbose
    public static void v(String tag, String msg) {
        if (VERBOSE < LOG_LEVEL)
            return;
        Log.v(tag, msg);
    }

    public static void v(String msg) {
        v(getInstance().getFunctionName(), msg);
    }

    // debug
    public static void d(String tag, String msg) {
        if (DEBUG < LOG_LEVEL)
            return;
        Log.d(tag, msg);
    }

    public static void d(String msg) {
        d(getInstance().getFunctionName(), msg);
    }

    // info
    public static void i(String tag, String msg) {
        if (INFO < LOG_LEVEL)
            return;
        Log.i(tag, msg);
    }

    public static void i(String msg) {
        i(getInstance().getFunctionName(), msg);
    }

    // warning
    public static void w(String tag, String msg) {
        if (WARNING < LOG_LEVEL)
            return;
        Log.w(tag, msg);
    }

    public static void w(String msg) {
        w(getInstance().getFunctionName(), msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (WARNING < LOG_LEVEL)
            return;
        Log.w(tag, msg, tr);
    }

    // error
    public static void e(String tag, String msg) {
        if (ERROR < LOG_LEVEL)
            return;
        Log.e(tag, msg);
    }

    public static void e(String msg) {
        e(getInstance().getFunctionName(), msg);
    }
}
