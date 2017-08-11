package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Created by ywx on 2017/8/9.
 * 试用的bean
 */

public class Trial implements Serializable{
    private int aeid;
    private int uid;
    private String username;
    private int cid;
    private int sid;
    private int area;
    private int areatype;
    private double star;
    private String applytime;
    private int applystatue;
    private int managestatue;

    public int getAeid() {
        return aeid;
    }

    public void setAeid(int aeid) {
        this.aeid = aeid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getAreatype() {
        return areatype;
    }

    public void setAreatype(int areatype) {
        this.areatype = areatype;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public int getApplystatue() {
        return applystatue;
    }

    public void setApplystatue(int applystatue) {
        this.applystatue = applystatue;
    }

    public int getManagestatue() {
        return managestatue;
    }

    public void setManagestatue(int managestatue) {
        this.managestatue = managestatue;
    }
}
