package com.lovegod.newbuy.service;

import java.io.Serializable;

/**
 * Author: lovegod
 * Created by 123 on 2017/4/28.
 */
public class Ble implements Serializable{
    private int bid;
    private String macaddress;
    private double x;
    private double y;
    private int sid;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
