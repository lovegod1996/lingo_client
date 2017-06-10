package com.lovegod.newbuy.bean;

/**
 * *****************************************
 * Created by thinking on 2017/5/24.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class SortFrist {

    private int cgid;//类别id
    private int cid;//店铺id（用于定制分类）
    private String big;//大分类
    private String small;//小分类
    private String logo;//类别logo
    private String secend;//类别小小

    public String getSecend() {
        return secend;
    }

    public void setSecend(String secend) {
        this.secend = secend;
    }


    public int getCgid() {
        return cgid;
    }

    public void setCgid(int cgid) {
        this.cgid = cgid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }





}
