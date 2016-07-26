package com.bailibao.bean;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class InvestProductBean extends BaseBean{

    public int totalCount;
    public List<ProductItem> resources;

    public static class ProductItem {
        public ProductItem(){

        }
        public ProductItem(int productId, String name){
            this.name = name;
            this.productId = productId;
        }

        public int productId;
        public String name;
    }
}
