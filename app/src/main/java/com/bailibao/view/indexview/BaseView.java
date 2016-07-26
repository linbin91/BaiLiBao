package com.bailibao.view.indexview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2016/4/23.
 */
public abstract class BaseView implements View.OnClickListener{
    protected View mView;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public BaseView(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initView();
        setListener();
    }

    public View getView(){
        return  mView;
    }
    public abstract void initView() ;
    public abstract void setListener();
}
