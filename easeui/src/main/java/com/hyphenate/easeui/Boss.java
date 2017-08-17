package com.hyphenate.easeui;

import java.io.Serializable;

/**
 * Created by ywx on 2017/8/17.
 */

public class Boss implements Serializable{
    private int bid;
    private int sid;
    private String boss_name;
    private String boss_pass;
    private String boss_pic;
    private String boss_nick;
    private String boss_phone;
    private String boss_id;

    public String getBoss_id() {
        return boss_id;
    }

    public void setBoss_id(String boss_id) {
        this.boss_id = boss_id;
    }

    public int getBid() {

        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getBoss_name() {
        return boss_name;
    }

    public void setBoss_name(String boss_name) {
        this.boss_name = boss_name;
    }

    public String getBoss_pass() {
        return boss_pass;
    }

    public void setBoss_pass(String boss_pass) {
        this.boss_pass = boss_pass;
    }

    public String getBoss_pic() {
        return boss_pic;
    }

    public void setBoss_pic(String boss_pic) {
        this.boss_pic = boss_pic;
    }

    public String getBoss_nick() {
        return boss_nick;
    }

    public void setBoss_nick(String boss_nick) {
        this.boss_nick = boss_nick;
    }

    public String getBoss_phone() {
        return boss_phone;
    }

    public void setBoss_phone(String boss_phone) {
        this.boss_phone = boss_phone;
    }
}
