package com.bailibao.bean.user;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class UserTransactionBean extends BaseBean {

    public List<TransactionBean> resources;
    public boolean hasNextPage;
    public int totalCount;

    public static class TransactionBean {
        public int type;
        public String name;
        public int payMoney;
        public String tradeDate;
        public int status;
    }
}
