package com.lovegod.newbuy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 123 on 2017/4/14.
 */

public class ShopCus implements Serializable{
    private List<Shop> shopList;

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

}
