package com.bailibao.bean.user;

import com.bailibao.bean.base.BaseBean;

/**
 * Created by Administrator on 2016/5/4.
 *
 *注册的时候判断用户是否存在了
 */
public class UserExistenceBean extends BaseBean{
    //0 表示不存在，即为注册；1表示存在，即已注册
    public int existence;
}
