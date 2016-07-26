package com.bailibao.bean.product;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ProductProgressBean extends BaseBean{

    public int totalCount;
    public boolean hasNextPage;
    public List<ProgressItem> resources;

    public static class ProgressItem {
        public int id;
        public String title;
        public String imageUrl;
        public String content;
    }
}
