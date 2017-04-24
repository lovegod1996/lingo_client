package com.lovegod.newbuy.bean;

import java.io.Serializable;

/**
 * Created by 123 on 2017/4/14.
 */

public class Shop implements Serializable{

    /**
     * code : 0
     * data : [{"latitude":65.4512,"logo":"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png","longgitude":34.87954,"saddress":"河南省郑州市龙湖镇商业街3号","sid":1,"slevel":4.5,"sname":"家用电器城","stel":"15537593903"}]
     * message : success
     */

        /**
         * latitude : 65.4512
         * logo : https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png
         * longgitude : 34.87954
         * saddress : 河南省郑州市龙湖镇商业街3号
         * sid : 1
         * slevel : 4.5
         * sname : 家用电器城
         * stel : 15537593903
         */

        private double latitude;
        private String logo;
        private double longgitude;
        private String saddress;
        private int sid;
        private double slevel;
        private String sname;
        private String stel;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public double getLonggitude() {
            return longgitude;
        }

        public void setLonggitude(double longgitude) {
            this.longgitude = longgitude;
        }

        public String getSaddress() {
            return saddress;
        }

        public void setSaddress(String saddress) {
            this.saddress = saddress;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public double getSlevel() {
            return slevel;
        }

        public void setSlevel(double slevel) {
            this.slevel = slevel;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getStel() {
            return stel;
        }

        public void setStel(String stel) {
            this.stel = stel;
        }

    @Override
    public String toString() {
        return "Shop{" +
                "latitude=" + latitude +
                ", logo='" + logo + '\'' +
                ", longgitude=" + longgitude +
                ", saddress='" + saddress + '\'' +
                ", sid=" + sid +
                ", slevel=" + slevel +
                ", sname='" + sname + '\'' +
                ", stel='" + stel + '\'' +
                '}';
    }
}
