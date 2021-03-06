package com.lovegod.newbuy.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/21.
 * 订单bean类
 */

public class Order implements Serializable {
    private int sid;
    private long oid;
    private int said;
    private int uid;
    private int count;
    private double totalprice;
    private String number;
    private int statue;
    private String createtime;
    private double freight;
    private String paytime;
    private String paytype;
    private String shiptime;
    private String dealtime;
    private int openstatue;
    private List<OrderGoods>orderGoodsList=new ArrayList<>();

   public class OrderGoods implements Serializable{
        private int ogid;
       private int oid;
       private int cid;
       private int sid;
       private int count;
       private double totalprice;
       private String param;
       private String goodsname;
       private String logo;

       public String getGoodsname() {
           return goodsname;
       }

       public void setGoodsname(String goodsname) {
           this.goodsname = goodsname;
       }

       public String getLogo() {
           return logo;
       }

       public void setLogo(String logo) {
           this.logo = logo;
       }

       public int getOgid() {
            return ogid;
        }

        public void setOgid(int ogid) {
            this.ogid = ogid;
        }

        public int getOid() {
            return oid;
        }

        public void setOid(int oid) {
            this.oid = oid;
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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(double totalprice) {
            this.totalprice = totalprice;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }
    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public int getSaid() {
        return said;
    }

    public void setSaid(int said) {
        this.said = said;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getShiptime() {
        return shiptime;
    }

    public void setShiptime(String shiptime) {
        this.shiptime = shiptime;
    }

    public String getDealtime() {
        return dealtime;
    }

    public void setDealtime(String dealtime) {
        this.dealtime = dealtime;
    }

    public int getOpenstatue() {
        return openstatue;
    }

    public void setOpenstatue(int openstatue) {
        this.openstatue = openstatue;
    }

    public List<OrderGoods> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<OrderGoods> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
