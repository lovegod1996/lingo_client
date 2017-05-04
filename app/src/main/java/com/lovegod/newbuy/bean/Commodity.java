package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Author: lovegod
 * Created by 123 on 2017/4/5.
 */
public class Commodity implements Serializable{
    private int cid;
    private int sid;
    private int salesvolu;
    private Double price;
    private String category;
    private String productname;
    private String logo;
    private String bluetoothmac;
    private double x;
    private  double y;

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

    public int getSalesvolu() {
        return salesvolu;
    }

    public void setSalesvolu(int salesvolu) {
        this.salesvolu = salesvolu;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBluetoothmac() {
        return bluetoothmac;
    }

    public void setBluetoothmac(String bluetoothmac) {
        this.bluetoothmac = bluetoothmac;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "cid=" + cid +
                ", sid=" + sid +
                ", salesvolu=" + salesvolu +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", productname='" + productname + '\'' +
                ", logo='" + logo + '\'' +
                ", bluetoothmac='" + bluetoothmac + '\'' +
                '}';
    }
}
