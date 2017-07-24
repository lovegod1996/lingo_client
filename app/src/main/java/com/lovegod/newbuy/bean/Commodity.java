package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Author: lovegod
 * Created by 123 on 2017/4/5.
 */
public class Commodity implements Serializable{

    /**
     * cid : 1
     * sid : 1
     * cgid : 1
     * salesvolu : 6200
     * price : 3499.0
     * productname : 三星（SAMSUNG）UA49KUC30SJXXZ 49英寸 曲面 HDR 4K超高清智能电视 黑色
     * logo : https://img11.360buyimg.com/n7/jfs/t2701/117/4343853787/151157/a2a442e9/57b6cf89N535ddd1b.jpg
     * bluetoothmac : fdghjfghj
     * x : 235
     * y : 350
     * detailshow : https://img20.360buyimg.com/vc/jfs/t4684/4/4583595/648025/376dc7d8/58c7df8aN5517c294.jpg；https://img20.360buyimg.com/vc/jfs/t4498/253/8087555/130578/5398ac17/58c7e016Nb4896ed5.jpg;https://img20.360buyimg.com/vc/jfs/t4369/65/1858798668/181091/5babf5f7/58c7e021N0d7073e4.jpg;https://img20.360buyimg.com/vc/jfs/t4432/297/7923558/669455/79cfbf29/58c7e02cNbe2acd5c.jpg;
     * stock : 5600
     */

    private int cid;
    private int sid;
    private int cgid;
    private int salesvolu;
    private double price;
    private String productname;
    private String logo;
    private String bluetoothmac;
    private String headershow;
    private int x;
    private int y;
    private String detailshow;
    private int stock;

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

    public int getCgid() {
        return cgid;
    }

    public void setCgid(int cgid) {
        this.cgid = cgid;
    }

    public int getSalesvolu() {
        return salesvolu;
    }

    public void setSalesvolu(int salesvolu) {
        this.salesvolu = salesvolu;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDetailshow() {
        return detailshow;
    }

    public void setDetailshow(String detailshow) {
        this.detailshow = detailshow;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    public String getHeadershow() {
        return headershow;
    }

    public void setHeadershow(String headershow) {
        this.headershow = headershow;
    }


    @Override
    public String toString() {
        return "Commodity{" +
                "cid=" + cid +
                ", sid=" + sid +
                ", cgid=" + cgid +
                ", salesvolu=" + salesvolu +
                ", price=" + price +
                ", productname='" + productname + '\'' +
                ", logo='" + logo + '\'' +
                ", bluetoothmac='" + bluetoothmac + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", detailshow='" + detailshow + '\'' +
                ", stock=" + stock +
                '}';
    }
}
