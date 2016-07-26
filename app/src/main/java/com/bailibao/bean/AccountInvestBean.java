package com.bailibao.bean;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class AccountInvestBean extends BaseBean {


    public int totalCount;
    public boolean hasNextPage;
    public List<InvestItem> resources;

    public static class InvestItem {
        public int id;
        public int profit;
        public int project;
        public int count;
        public String name;
        public int type;
        public int productId;
    }
}
