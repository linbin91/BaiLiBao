package com.bailibao.bean;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class TipBean extends BaseBean {

    public int totalCount;
    public List<TipItem> resources;

    public static  class  TipItem{
        public String phone;
        public int count;
        public String lapsedTime;
        public String name;
    }
}
