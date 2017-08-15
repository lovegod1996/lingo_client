package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Created by ywx on 2017/8/12.
 * 足迹的bean类
 */

public class Track implements Serializable{
    private int tid;
    private int uid;
    private int cid;
    private int assess;
    private int detail;
    private int question;
    private int attention;
    private int cart;
    private int buy;
    private int userassess;
    private int total;
    private String cordtime;
    private String goodsname;
    private String logo;
    private String price;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getAssess() {
        return assess;
    }

    public void setAssess(int assess) {
        this.assess = assess;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getCart() {
        return cart;
    }

    public void setCart(int cart) {
        this.cart = cart;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getUserassess() {
        return userassess;
    }

    public void setUserassess(int userassess) {
        this.userassess = userassess;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCordtime() {
        return cordtime;
    }

    public void setCordtime(String cordtime) {
        this.cordtime = cordtime;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
