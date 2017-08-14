package com.lovegod.newbuy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.lovegod.newbuy.utils.view.AdapterWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywx on 2017/8/14.
 * 用于给recyclerview添加黏性日期悬浮
 */

public class TimeStickyDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Paint mPaint,textPaint;
    private int lineHeight;
    private AdapterWrapper wrapper;
    private int headerCount;
    private List<String>timeList=new ArrayList<>();
    public TimeStickyDecoration(Context context, List<String>list, AdapterWrapper wrapper){
        mContext=context;
        timeList=list;
        this.wrapper=wrapper;
        headerCount=wrapper.getHeaderCount();
        init();
    }

    public TimeStickyDecoration(Context context, List<String>list){
        mContext=context;
        timeList=list;
        headerCount=0;
        init();
    }

    private void init(){
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#80000000"));

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        lineHeight=35;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos=parent.getChildAdapterPosition(view)+headerCount;
        if (timeList.size()!=0) {
            if (pos == headerCount || isFirst(pos)) {
                outRect.top = lineHeight;
            } else {
                outRect.top = 0;
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        String preTime,currentTime="";
        int itemCount=state.getItemCount();
        int childCount=parent.getChildCount();
        int left=parent.getPaddingLeft();
        int right=parent.getWidth()-parent.getPaddingRight();

        if (timeList.size()!=0) {
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(view)+headerCount;

                preTime = currentTime;
                currentTime = timeList.get(pos);
                if (preTime.equals(currentTime)) {
                    continue;
                }
                if (TextUtils.isEmpty(currentTime)) {
                    continue;
                }
                int viewBottom = view.getBottom();

                float height = Math.max(lineHeight, view.getTop());
                if(pos+1<timeList.size()) {
                    String nextTime=timeList.get(pos+1);
                    if (!currentTime.equals(nextTime)&&viewBottom < height) {
                        height = viewBottom;
                    }
                }
                    int textHeight = (int) (textPaint.descent() - textPaint.ascent());
                    c.drawRect(left, height - lineHeight, right, height, mPaint);
                    c.drawText(currentTime, left, height - lineHeight / 4 , textPaint);
            }
        }
    }

    private boolean isFirst(int pos){
        if(pos==headerCount){
            return true;
        }else {
            String preTime=timeList.get(pos-1);
            String currentTime=timeList.get(pos);
           return !currentTime.equals(preTime);
        }
    }
}
