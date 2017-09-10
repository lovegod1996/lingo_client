package com.lovegod.newbuy.view.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hyphenate.util.DensityUtil;

/**
 * Created by ywx on 2017/8/18.
 */

public class HomeDecoration extends RecyclerView.ItemDecoration {
    private int lineHeight=150;
    private Paint textPaint;
    private Paint bgPaint;
    private Context mContext;

    public HomeDecoration(Context context){
        mContext=context;
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#FE5203"));
        textPaint.setTextSize(DensityUtil.px2dip(mContext,160));

        bgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.parseColor("#EFEEEC"));
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos=parent.getChildAdapterPosition(view);
        if(pos==2||pos==5||pos==6){
            outRect.top=lineHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount=parent.getChildCount();
        for(int i=0;i<childCount;i++){
            View child=parent.getChildAt(i);
            int pos=parent.getChildAdapterPosition(child);
            if(pos==2){
                textPaint.setColor(Color.parseColor("#FE5203"));
                Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
                int height= (int) (fontMetrics.descent-fontMetrics.ascent);
                int width= (int) textPaint.measureText("---    猜你喜欢    ---");
                c.drawRect(0,child.getTop()-lineHeight,parent.getMeasuredWidth(),child.getTop(),bgPaint);
                c.drawText("---    猜你喜欢    ---",parent.getMeasuredWidth()/2-width/2,child.getTop()-height/2,textPaint);
            }else if(pos==5){
                textPaint.setColor(Color.parseColor("#2196F3"));
                Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
                int height= (int) (fontMetrics.descent-fontMetrics.ascent);
                int width= (int) textPaint.measureText("---    更多好货    ---");
                c.drawText("---    更多好货    ---",parent.getMeasuredWidth()/2-width/2,child.getTop()-height/2,textPaint);
            }
        }
    }
}
