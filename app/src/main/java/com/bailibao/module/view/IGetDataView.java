package com.bailibao.module.view;

import com.bailibao.base.BaseView;

import java.io.IOException;

/**
 * Created by Administrator on 2016/4/13.
 */
public interface IGetDataView extends BaseView{
    public  void  fillView(String content) throws IOException;
}
