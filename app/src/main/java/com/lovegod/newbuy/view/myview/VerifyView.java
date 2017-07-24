package com.lovegod.newbuy.view.myview;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lovegod.newbuy.R;

import java.util.Random;

/**
 * Created by ywx on 2017/7/10.
 * 自定义验证码绘制view
 * 宽度过大可能会造成部分字母显示不全
 */

public class VerifyView extends View implements View.OnClickListener{
    //用语随机生成的所有数字以及大小写字母
    private static final String initNumber="0123456789abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //保存每一次生成的验证码
    private StringBuilder verifyArray=new StringBuilder();
    private int offset=20;
    //绘制的画笔
    private Paint textPaint;
    private Random mRandom=new Random();
    private float verifyNumberSize;
    //默认宽度和高度，如果指定wrap_content就是默认的
    private int defaultWidth=260,defaultHeight=120;
    //最后测量的宽高，如果wrap_content就是使用默认宽高
    private int realHeight,realWidth;
    //画布随机旋转的角度
    private float rotateAngle=5;
    //每一个字母可以占用的位置大小
    private int eachStarX;
    public VerifyView(Context context) {
       this(context,null);
    }

    public VerifyView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public VerifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.VerifyView);
        //获取指定的字母大小，默认为70
        verifyNumberSize=ta.getFloat(R.styleable.VerifyView_verifynumber_size,70);
        ta.recycle();
        init();
    }
    //初始化各个参数
    private void init() {
        textPaint=new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(verifyNumberSize);
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode==MeasureSpec.AT_MOST) {
            widthSize=defaultWidth;
        }
        if(heightMode==MeasureSpec.AT_MOST){
            heightSize=defaultHeight;
        }
        realHeight=heightSize;
        realWidth=widthSize;
        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawNumberText(canvas);
    }
    //绘制随机干扰点
    private void drawRandomCircle(Canvas canvas) {
        for(int i=0;i<10;i++)
        {
            int x= (int) (Math.random()*realWidth);
            int y= (int) (Math.random()*realHeight);
            canvas.drawCircle(x,y,2,textPaint);
        }
    }
    //绘制验证码字母
    private void drawNumberText(Canvas canvas) {
        verifyArray.delete(0,verifyArray.length());
        eachStarX=realWidth/4;
        for(int i=0;i<4;i++)
        {
            textPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20,
                    mRandom.nextInt(200) + 20);
            drawRandomCircle(canvas);
            canvas.save();
            if((int)(Math.random()+0.5)==0) {
                canvas.rotate(rotateAngle);
            }else{
                canvas.rotate(-rotateAngle);
            }
            String temp=initNumber.charAt((int)(Math.random()*60+0.5))+"";
            verifyArray.append(temp);
            canvas.drawText(temp,5+i*eachStarX,realHeight/2+offset,textPaint);
            canvas.restore();
        }
        textPaint.setStrokeWidth(3);
        canvas.drawLine(0,realHeight/2,3*eachStarX+50,realHeight/2,textPaint);
    }
    @Override
    public void onClick(View v) {
        invalidate();
    }
    //获取验证码的值
    public String getVerifyArray() {
        return verifyArray.toString();
    }
}
