package com.lovegod.newbuy.bean;

/**
 * Created by ywx on 2017/8/1.
 * 收藏商品bean类
 */

public class FavouriteGoods {
    private int sid;
    private int cid;
    private int uid;
    private String looktime;
    private int gaid;
    private String goodsName;
    private String goodslogo;
    private String username;
    private String userlogo;
    private boolean isEdit;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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

    public int getGaid() {
        return gaid;
    }

    public void setGaid(int gaid) {
        this.gaid = gaid;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodslogo() {
        return goodslogo;
    }

    public void setGoodslogo(String goodslogo) {
        this.goodslogo = goodslogo;
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
}
