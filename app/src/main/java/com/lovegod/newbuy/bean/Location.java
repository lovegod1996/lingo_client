package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Created by lovegod on 2017/6/10.
 */

public class Location implements Serializable{
    /**
     * errcode : 0
     * lat : 40.00601
     * lon : 116.489031
     * radius : 463
     * address : 北京市朝阳区崔各庄地区溪阳东路;望京东路与溪阳东路路口东122米
     */

    private int errcode;
    private String lat;
    private String lon;
    private String radius;
    private String address;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "errcode=" + errcode +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", radius='" + radius + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
