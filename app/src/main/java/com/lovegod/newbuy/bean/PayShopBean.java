package com.lovegod.newbuy.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/20.
 * 用于在结算界面将不同店铺信息展示的bean
 */

public class PayShopBean {
    private int sid;
    private String shopname;
    public List<ShopCartBean> goodList=new ArrayList<>();

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public List<ShopCartBean> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<ShopCartBean> goodList) {
        this.goodList = goodList;
    }
}
