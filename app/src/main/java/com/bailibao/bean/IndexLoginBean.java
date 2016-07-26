package com.bailibao.bean;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class IndexLoginBean extends BaseBean {
    public int lastProfit;
    public int rank;
    public List<IndexProductItem> indexProducts;

    public  static class IndexProductItem{
        public int id;
        public int yield;
        public String name;
    }


}
