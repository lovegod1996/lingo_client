package com.hyphenate.easeui;

import java.io.Serializable;

/**
 * Created by ywx on 2017/8/17.
 */

public class Data implements Serializable{
    private int type;
    private User user;
    private Boss boss;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }
}
