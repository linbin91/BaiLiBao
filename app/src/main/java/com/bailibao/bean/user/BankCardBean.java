package com.bailibao.bean.user;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/29.
 */
public class BankCardBean extends BaseBean {
    public int totalCount;
    public boolean hasNextPage;
    public List<CardItem> resources;

    public static class CardItem{
        public String cardNo;
        public String bankName;
    }
}
