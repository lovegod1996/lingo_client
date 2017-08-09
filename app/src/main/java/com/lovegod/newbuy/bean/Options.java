package com.lovegod.newbuy.bean;

/**
 * Created by ywx on 2017/8/9.
 * 用来标记一些选项是否被选中
 */

public class Options {
    private String text;
    private boolean isChoose;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
