package com.lovegod.newbuy.view.fragment.Home_Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.lovegod.newbuy.R;

/**
 * Created by ywx on 2017/8/17.
 * 首页轮播图holder
 */

public class SlideHolder extends RecyclerView.ViewHolder{
    public SliderLayout mSliderLayout;
    public PagerIndicator indicator;
    public SlideHolder(View itemView) {
        super(itemView);
        //容器
        mSliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
        //指示器，那些小点
        indicator = (PagerIndicator) itemView.findViewById(R.id.custom_indicator);
    }
}
