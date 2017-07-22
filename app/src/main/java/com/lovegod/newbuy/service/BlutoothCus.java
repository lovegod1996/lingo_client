package com.lovegod.newbuy.service;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 123 on 2017/4/3.
 */

public class BlutoothCus implements Serializable ,Comparable<BlutoothCus>{
    private int bondState;
    private String address;
    private String name;
    private int type;
    private int rssi;
    private Date date;
    private double distance;
    private double x;
    private double y;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull BlutoothCus blutoothCus) {
        if(blutoothCus.getRssi()>this.rssi){
            return 1;
        }else if(blutoothCus.getRssi()>this.rssi){
            return 0;
        }else{
            return -1;
        }
    }
}
