package com.bailibao.bean;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by lin on 2016/4/10.
 */
public class AdBean extends BaseBean{

    public List<AdItem> resources;
    public int totalCount;
    public boolean hasNextPage;

    public static  class  AdItem{
        public int id;
        public String title;
        public  String linkUrl;
        public  String imageUrl;
        public  int jump;
    }
}
