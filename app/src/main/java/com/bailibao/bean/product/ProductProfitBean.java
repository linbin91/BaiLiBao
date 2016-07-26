package com.bailibao.bean.product;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class ProductProfitBean extends BaseBean {

    public List<ProfitItem> resources;
    public int totalCount;
    public static class  ProfitItem{
        public int totalProfit;
        public String realName;
    }
}
