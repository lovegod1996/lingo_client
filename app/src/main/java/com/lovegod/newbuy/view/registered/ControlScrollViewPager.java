package com.lovegod.newbuy.view.registered;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ywx on 2017/7/10.
 * 添加属性可以设置ViewPager是否可以左右滑动
 */

public class ControlScrollViewPager extends ViewPager {
    private boolean canScroll;

    public boolean isCanScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public ControlScrollViewPager(Context context) {
        this(context,null);
    }

    public ControlScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!canScroll)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!canScroll)
            return false;
        return super.onTouchEvent(ev);
    }
}
