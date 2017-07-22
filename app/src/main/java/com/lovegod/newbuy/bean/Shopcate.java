package com.lovegod.newbuy.bean;

import java.util.List;

/**
 * Created by 123 on 2017/4/7.
 */

public class Shopcate {

    /**
     * message : success
     * category : ["电视"]
     * status : 0
     */

    private String message;
    private int status;
    private List<String> category;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
