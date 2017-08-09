package com.lovegod.newbuy.view.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovegod.newbuy.R;

/**
 * 通用列表项的自定义布局
 * Created by ywx on 2017/7/12.
 */

public class ItemImageLayout extends LinearLayout {
    private String rightText,leftText;
    private Drawable leftSrc;
    private TextView leftTextView,rightTextView;
    private ImageView leftImageView;
    public ItemImageLayout(Context context) {
       this(context,null);
    }

    public ItemImageLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ItemImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.itemlayout_image_item,this);
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.ItemImageLayout);
        rightText=ta.getString(R.styleable.ItemImageLayout_right_text);
        leftText=ta.getString(R.styleable.ItemImageLayout_left_text);
        leftSrc=ta.getDrawable(R.styleable.ItemImageLayout_left_image_src);
        ta.recycle();
        leftTextView=(TextView)findViewById(R.id.imageitem_left_text);
        rightTextView=(TextView)findViewById(R.id.imageitem_right_text);
        leftImageView=(ImageView)findViewById(R.id.imageitem_left_image);
        leftImageView.setImageDrawable(leftSrc);
        rightTextView.setText(rightText);
        leftTextView.setText(leftText);
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public Drawable getLeftSrc() {
        return leftSrc;
    }

    public void setLeftSrc(Drawable leftSrc) {
        this.leftSrc = leftSrc;
    }
}
