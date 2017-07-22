package com.lovegod.newbuy.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by ywx on 2017/7/18.
 */

public class Province implements IPickerViewData{
    private int pid;
    private String province;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String getPickerViewText() {
        return province;
    }
}
