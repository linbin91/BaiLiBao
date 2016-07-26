package com.bailibao.bean;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class TotalIncomingBean extends BaseBean {

    public int totalCount;
    public boolean hasNextPage;
    public List<IncomingItem> resources;

    public static class IncomingItem {
        public String name;
        public int profit;
        public String createdDate;
    }

}
