package com.lovegod.newbuy.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by ywx on 2017/7/18.
 */

public class City implements IPickerViewData {
    private int cid;
    private int pid;
    private String cityname;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    @Override
    public String getPickerViewText() {
        return cityname;
    }
}
