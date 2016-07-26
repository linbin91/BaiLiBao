package com.bailibao.bean;

import com.bailibao.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class MessageBean extends BaseBean{

    public  int totalCount;
    public  boolean hasNextPage;
    public List<MessageItem> resources;

    public  static  class MessageItem{
        public String content;
        public int id;
        public String title;
        public int status;
        public String createdDate;
        public String path;
    }
}
