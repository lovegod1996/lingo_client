/*
 *
 *  *
 *  *  *
 *  *  *  * ===================================
 *  *  *  * Copyright (c) 2016.
 *  *  *  * 作者：安卓猴
 *  *  *  * 微博：@安卓猴
 *  *  *  * 博客：http://sunjiajia.com
 *  *  *  * Github：https://github.com/opengit
 *  *  *  *
 *  *  *  * 注意**：如果您使用或者修改该代码，请务必保留此版权信息。
 *  *  *  * ===================================
 *  *  *
 *  *  *
 *  *
 *
 */

package com.lovegod.newbuy.view.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovegod.newbuy.R;


/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

  public ImageView good_imageview;
  public TextView good_textview;
  public TextView money_textview;
  public MyRecyclerViewHolder(View itemView) {
    super(itemView);
    good_imageview = (ImageView) itemView.findViewById(R.id.good_imageview);
    good_textview = (TextView) itemView.findViewById(R.id.good_textview);
    money_textview = (TextView) itemView.findViewById(R.id.money_textview);
  }
}
