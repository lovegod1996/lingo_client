package com.lovegod.newbuy.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by ywx on 2017/7/18.
 */

public class District implements IPickerViewData{
    private int did;
    private int cid;
    private String districtname;

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    @Override
    public String getPickerViewText() {
        return districtname;
    }
}
