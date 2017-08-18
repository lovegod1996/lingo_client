package com.lovegod.newbuy.view.myview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lovegod.newbuy.R;

/**
 * Created by ywx on 2017/8/18.
 * 下拉刷新布局
 */

public class RefreshLayout extends LinearLayout {
    //刷新头部以及加载底部
    private View mHeader;
    private int lastY;
    private ValueAnimator valueAnimator;
    private int effectiveOffset;
    private TextView headerText;
    private OnRefreshListener onRefreshListener;
    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeader= LayoutInflater.from(context).inflate(R.layout.header,null);
        headerText= (TextView) mHeader.findViewById(R.id.header_text);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mHeader.setLayoutParams(layoutParams);
        addView(mHeader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量子控件
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View child=getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        effectiveOffset=headerText.getMeasuredHeight()+headerText.getPaddingBottom()*2;
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View child=getChildAt(i);
            if(child.getVisibility()==GONE){
                continue;
            }
            if(child==mHeader) {
                child.layout(0, -child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY= (int) (event.getY()-lastY);
                //下拉操作
                if(moveY>0) {
                    if(Math.abs(getScrollY())<mHeader.getMeasuredHeight()/3*2) {
                        headerText.setText("下拉刷新");
                        scrollBy(0, -moveY);
                        //下拉超过一定范围，进行刷新操作
                        if (Math.abs(getScrollY()) >= effectiveOffset) {
                            headerText.setText("释放刷新");
                        }
                    }
                }
                //上拉操作
                else if(moveY<0){
                    if(getScrollY()<-10){
                        scrollBy(0, -moveY);
                    }
                }
                lastY= (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(getScrollY())>=effectiveOffset&&getScrollY()<0) {
                    startScroll(getScrollY(),-effectiveOffset);
                    headerText.setText("刷新中...");
                    if (onRefreshListener != null) {
                        onRefreshListener.onRefresh();
                    }
                }else if(Math.abs(getScrollY()) < effectiveOffset&&getScrollY()<0){
                    startScroll(getScrollY(),0);
                }else if(getScrollY()>0){
                    startScroll(getScrollY(),0);
                }
                break;

        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept=false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY= (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY= (int) (ev.getY()-lastY);
                //下拉操作
                if(moveY>0){
                    View child=getChildAt(0);
                    if(child instanceof RecyclerView) {
                        RecyclerView adapterChild = (RecyclerView) child;
                            if(adapterChild.getChildAt(0)!=null&&adapterChild.getChildAt(0).getTop()==0) {
                                intercept = true;
                        }
                    }
                }
                lastY= (int) ev.getY();
                break;
        }
        return intercept;
    }

    /**
     * 开启平滑滚动
     * @param startY 开始Y坐标位置
     * @param endY 结束Y坐标位置
     */
    private void startScroll(int startY,int endY){
        valueAnimator=ValueAnimator.ofInt(startY,endY).setDuration(150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value= (int) valueAnimator.getAnimatedValue();
                scrollTo(0,value);
            }
        });
        valueAnimator.start();
    }

    public void refreshDone(){
        startScroll(getScrollY(),0);
        headerText.setText("下拉刷新");
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}