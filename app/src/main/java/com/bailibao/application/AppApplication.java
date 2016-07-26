package com.bailibao.application;

import android.app.Application;
import android.content.Context;

import com.bailibao.data.SystemVal;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Administrator on 2016/4/12.
 */
public class AppApplication  extends Application{
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        SystemVal.init(getApplicationContext());

        context = getApplicationContext();
        initImageLoader(getApplicationContext());

    }

    private void initImageLoader(Context applicationContext) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


}
