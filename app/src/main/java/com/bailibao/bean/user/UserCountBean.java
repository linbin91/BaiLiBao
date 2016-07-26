package com.bailibao.bean.user;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class UserCountBean extends BaseBean {

    public List<CountItem> resources;
    public boolean hasNextPage;
    public int totalCount;

    public static class CountItem {
        public int type;
        public int payMoney;
        public String tradeDate;
        public int status;
    }
}
