package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Created by ywx on 2017/8/3.
 * 问题关注的bean类
 */

public class FavouriteQuest implements Serializable{
    private int qid;
    private int uid;
    private String looktime;
    private int qaid;
    private String username;
    private String userlogo;
    private String questname;
    private String goodsname;
    private String goodslogo;

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodslogo() {
        return goodslogo;
    }

    public void setGoodslogo(String goodslogo) {
        this.goodslogo = goodslogo;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLooktime() {
        return looktime;
    }

    public void setLooktime(String looktime) {
        this.looktime = looktime;
    }

    public int getQaid() {
        return qaid;
    }

    public void setQaid(int qaid) {
        this.qaid = qaid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserlogo() {
        return userlogo;
    }

    public void setUserlogo(String userlogo) {
        this.userlogo = userlogo;
    }

    public String getQuestname() {
        return questname;
    }

    public void setQuestname(String questname) {
        this.questname = questname;
    }
}
