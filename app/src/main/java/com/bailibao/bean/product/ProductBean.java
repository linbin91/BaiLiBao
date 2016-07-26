package com.bailibao.bean.product;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ProductBean extends BaseBean {

    public boolean hasNextPage;
    public int totalCount;
    public List<ProductItem> resources;


    public  static class ProductItem{
        public int id;
        public int status;
        public int yield;
        public String name;
        public String tips;
        public int type;
        public int recommend;
        public int leftCount;
    }
}
