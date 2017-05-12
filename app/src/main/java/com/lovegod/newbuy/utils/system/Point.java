package com.lovegod.newbuy.utils.system;

import java.io.Serializable;

/**
 * Created by 123 on 2017/5/1.
 */

public class Point implements Serializable{
    private double x;
    private double y;


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
