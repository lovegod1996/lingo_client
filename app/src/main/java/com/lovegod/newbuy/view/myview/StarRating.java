package com.lovegod.newbuy.view.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lovegod.newbuy.R;

/**
 * Created by ywx on 2017/7/27.
 */

public class StarRating extends LinearLayout {
    private int currentCount;
    private boolean canClick;
    private boolean hasAnim;
    private int starCount;
    private float starSize;
    private int starSpacing;
    private Bitmap starEmpty;
    private Bitmap starFill;
    private OnStarChangeListener onStarChangeListener;
    public StarRating(Context context) {
        this(context,null);
    }

    public StarRating(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public StarRating(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.StarRating);
        starSize=ta.getDimension(R.styleable.StarRating_starSize,100f);
        starCount=ta.getInt(R.styleable.StarRating_starCount,5);
        starSpacing=ta.getInteger(R.styleable.StarRating_starSpacing,40);
        hasAnim=ta.getBoolean(R.styleable.StarRating_hasAnim,false);
        canClick=ta.getBoolean(R.styleable.StarRating_clickable,true);
        currentCount=0;
        ta.recycle();

        starEmpty= BitmapFactory.decodeResource(getResources(),R.mipmap.star_empty);
        starFill= BitmapFactory.decodeResource(getResources(),R.mipmap.star_fill);

        for(int i=0;i<starCount;i++){
            ImageView imageView=getStarImage(context);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(canClick) {
                        currentCount=indexOfChild(view)+1;
                        setStar(indexOfChild(view) + 1, hasAnim);
                        if (onStarChangeListener != null) {
                            onStarChangeListener.onStarChange(indexOfChild(view) + 1);
                        }
                    }
                }
            });
            addView(imageView);
        }
    }

    private ImageView getStarImage(Context context){
        ImageView imageView=new ImageView(context);
        LayoutParams params=new LayoutParams(Math.round(starSize),Math.round(starSize));
        imageView.setLayoutParams(params);
        imageView.setPadding(0,0,starSpacing,0);
        imageView.setImageBitmap(starEmpty);
        return imageView;
    }

    private void setStar(int count,boolean anim){
        if(count>starCount){
            int temp;
            temp=count;
            count=starCount;
            starCount=temp;
        }
        for(int i=0;i<count;i++){
            ((ImageView)getChildAt(i)).setImageBitmap(starFill);
            if(i==count-1&&anim) {
                ScaleAnimation sa = new ScaleAnimation(1, 1.05f, 1, 1.05f);
                sa.setDuration(180);
                getChildAt(i).startAnimation(sa);
            }
        }
        for(int i=starCount-1;i>=count;i--){
            ((ImageView)getChildAt(i)).setImageBitmap(starEmpty);
        }
    }

    public interface OnStarChangeListener{
        void onStarChange(int starCount);
    }

    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.onStarChangeListener = onStarChangeListener;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public float getStarSize() {
        return starSize;
    }

    public void setStarSize(float starSize) {
        this.starSize = starSize;
    }

    public void setCurrentStarCount(int count){
        currentCount=count;
        setStar(count,false);
    }

    public boolean isHasAnim() {
        return hasAnim;
    }

    public void setHasAnim(boolean hasAnim) {
        this.hasAnim = hasAnim;
    }

    public int getStarSpacing() {
        return starSpacing;
    }

    public void setStarSpacing(int starSpacing) {
        this.starSpacing = starSpacing;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public int getCurrentCount() {
        return currentCount;
    }
}