package com.bailibao.view.myadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by lin on 2016/4/10.
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {
    protected List<T> mList;
    protected Context ctx;
    protected LayoutInflater mInflater;
    protected DisplayImageOptions options = null;

    public BasePagerAdapter(Context context, List<T> list) {
        ctx = context;
        mList = list;
        mInflater = LayoutInflater.from(ctx);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public abstract Object instantiateItem(ViewGroup container, int position);

    @Override
    public abstract void destroyItem(ViewGroup container, int position, Object object);
}
