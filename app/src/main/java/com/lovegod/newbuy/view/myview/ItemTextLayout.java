package com.lovegod.newbuy.view.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovegod.newbuy.R;

/**
 * Created by ywx on 2017/7/12.
 */

public class ItemTextLayout extends LinearLayout {
    private String rightText,leftText;
    private TextView leftTextView,rightTextView;
    private ImageView moreInfoImage;
    private boolean imageVisibility;
    public ItemTextLayout(Context context) {
        this(context,null);
    }

    public ItemTextLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ItemTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.itemlayout_text_item,this);
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.ItemTextLayout);
        rightText=ta.getString(R.styleable.ItemTextLayout_text_right_text);
        leftText=ta.getString(R.styleable.ItemTextLayout_text_left_text);
        imageVisibility=ta.getBoolean(R.styleable.ItemTextLayout_right_image_visibility,true);
        ta.recycle();
        moreInfoImage=(ImageView)findViewById(R.id.textitem_more_info_icon);
        leftTextView=(TextView)findViewById(R.id.textitem_left_text);
        rightTextView=(TextView)findViewById(R.id.textitem_right_text);
        leftTextView.setText(leftText);
        rightTextView.setText(rightText);
        if(!imageVisibility){
            moreInfoImage.setVisibility(GONE);
        }else {
            moreInfoImage.setVisibility(VISIBLE);
        }
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        rightTextView.setText(rightText);
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        leftTextView.setText(leftText);
    }

    public boolean isImageVisibility() {
        return imageVisibility;
    }

    public void setImageVisibility(boolean imageVisibility) {
        this.imageVisibility = imageVisibility;
        if(!imageVisibility){
            moreInfoImage.setVisibility(GONE);
        }else {
            moreInfoImage.setVisibility(VISIBLE);
        }
    }
}
