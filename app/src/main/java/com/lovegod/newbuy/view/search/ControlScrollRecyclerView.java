package com.lovegod.newbuy.view.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/7/15.
 */

public class ControlScrollRecyclerView extends RecyclerView {
    private boolean canScroll;
    public ControlScrollRecyclerView(Context context) {
        this(context,null);
    }

    public ControlScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ControlScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(!canScroll){
            return false;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(!canScroll){
            return false;
        }
        return super.onTouchEvent(e);
    }

    public boolean isCanScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
}
