package com.lovegod.newbuy.bean;

import java.util.List;

/**
 * Created by 123 on 2017/4/7.
 */

public class Goods {

    /**
     * commodity : [{"bluetoothmac":"24:4B:03:70:3E:78","category":"电视","cid":1,"logo":"sdfds","price":3500,"productname":"海尔大彩电55寸广角高清","salesvolu":2000,"sid":1}]
     * message : success
     * status : 0
     */

    private String message;
    private int code;
    private List<CommodityBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CommodityBean> getData() {
        return data;
    }

    public void setData(List<CommodityBean> data) {
        this.data = data;
    }

    public static class CommodityBean {
        /**
         * bluetoothmac : 24:4B:03:70:3E:78
         * category : 电视
         * cid : 1
         * logo : sdfds
         * price : 3500.0
         * productname : 海尔大彩电55寸广角高清
         * salesvolu : 2000
         * sid : 1
         */

        private String bluetoothmac;
        private String category;
        private int cid;
        private String logo;
        private double price;
        private String productname;
        private int salesvolu;
        private int sid;

        public String getBluetoothmac() {
            return bluetoothmac;
        }

        public void setBluetoothmac(String bluetoothmac) {
            this.bluetoothmac = bluetoothmac;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
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

        public int getSalesvolu() {
            return salesvolu;
        }

        public void setSalesvolu(int salesvolu) {
            this.salesvolu = salesvolu;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }
    }
}
