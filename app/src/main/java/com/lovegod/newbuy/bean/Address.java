package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Created by ywx on 2017/7/18.
 */

public class Address implements Serializable{
    private int said;
    private int uid;
    private String name;
    private int sex;
    private String phone;
    private String address;
    private int statue;
    private String zip;
    private boolean isUnfold;

    public boolean isUnfold() {
        return isUnfold;
    }

    public void setUnfold(boolean unfold) {
        isUnfold = unfold;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getSaid() {
        return said;
    }

    public void setSaid(int said) {
        this.said = said;
    }
}
