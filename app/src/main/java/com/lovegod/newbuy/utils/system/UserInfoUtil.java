package com.lovegod.newbuy.utils.system;

import com.lovegod.newbuy.bean.User;

/**
 * 持有用户对象的单例
 * Created by ywx on 2017/7/11.
 */

public class UserInfoUtil {
    private static User user=new User();
    /**
     * @return 返回单例用户对象
     */
    public static User getInstance(){
        return user;
    }
}
