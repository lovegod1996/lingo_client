package com.lovegod.newbuy.bean;

import android.provider.ContactsContract;
import android.text.format.Time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Timer;

/**
 * *****************************************
 * Created by thinking on 2017/6/13.
 * 创建时间：
 * <p>
 * 描述：评价的数据
 * <p/>
 * <p/>
 * *******************************************
 */

public class Assess implements Serializable {

    /**
     * aid : 4        //评价
     * cid : 1          //商品id
     * uid : 1          //用户id
     * hollrall : wode       总评
     * detail : wode        细评
     * grade : 2.2        星级
     * pics : wode         买家秀图
     */

    private int aid;
    private int cid;
    private int uid;
    private String hollrall;
    private String detail;
    private double grade;
    private String pics;
    private Calendar date;
    private String param;

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }


    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getHollrall() {
        return hollrall;
    }

    public void setHollrall(String hollrall) {
        this.hollrall = hollrall;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }
}
