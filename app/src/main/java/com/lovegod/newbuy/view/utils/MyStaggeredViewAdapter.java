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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovegod.newbuy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyStaggeredViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

  public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
  }

  public OnItemClickListener mOnItemClickListener;

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  public Context mContext;
  public List<String> mDatas;
  public List<Integer> mHeights;
  public LayoutInflater mLayoutInflater;

  List<Map<String, Object>> listItems;

  int[] shop_images = {R.mipmap.tv1, R.mipmap.tv2, R.mipmap.tv3, R.mipmap.tv4, R.mipmap.tv5, R.mipmap.tv3,R.mipmap.tv4, R.mipmap.tv5, R.mipmap.tu6};
  String[] shop_name={"百业家电专营店","思迪电器专营店","中佳电器专营店","华强官方旗舰店","粤城电器","杭越电器专营店","中佳电器专营店","华强官方旗舰店","粤城电器"};
  int[] moneys={3900,3990,4099,4199,4199,4399,4199,4199,4399};

  public MyStaggeredViewAdapter(Context mContext) {
    this.mContext = mContext;
    mLayoutInflater = LayoutInflater.from(mContext);
   /* mDatas = new ArrayList<>();
    mHeights = new ArrayList<>();
    for (int i = 'A'; i <= 'z'; i++) {
      mDatas.add((char) i + "");
    }
    for (int i = 0; i < mDatas.size(); i++) {
      mHeights.add((int) (Math.random() * 300) + 200);
    }*/
    listItems = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < shop_images.length; i++) {
      Map<String, Object> listItem = new HashMap<String, Object>();
      listItem.put("goodImage", shop_images[i]);
      listItem.put("goodName", shop_name[i]);
      listItem.put("goodMoneys", moneys[i]);
      listItems.add(listItem);
    }
    mHeights = new ArrayList<>();
    for (int i = 0; i < listItems.size(); i++) {

      mHeights.add((int) (Math.random() * 300) + 200);
    }
  }
  /**
   * 创建ViewHolder
   */
  @Override public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
    MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
    return mViewHolder;
  }

  /**
   * 绑定ViewHoler，给item中的控件设置数据
   */
  @Override public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
    if (mOnItemClickListener != null) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mOnItemClickListener.onItemClick(holder.itemView, position);
        }
      });

      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override public boolean onLongClick(View v) {
          mOnItemClickListener.onItemLongClick(holder.itemView, position);
          return true;
        }
      });
    }

 ViewGroup.LayoutParams mLayoutParams = holder.good_imageview.getLayoutParams();
   // listItems.get(position).get("goodImage");
    mLayoutParams.height = mHeights.get(position);
    holder.good_imageview.setLayoutParams(mLayoutParams);
    holder.good_imageview.setImageResource((int) listItems.get(position).get("goodImage"));
/*    holder.good_textview.setText((String)listItems.get(position).get("goodName"));
    holder.money_textview.setText(String.valueOf(listItems.get(position).get("goodMoneys")));*/

  /*  ViewGroup.LayoutParams mLayoutParams = holder.mTextView.getLayoutParams();
    mLayoutParams.height = mHeights.get(position);
    holder.mTextView.setLayoutParams(mLayoutParams);
    holder.mTextView.setText(mDatas.get(position));*/
  }

  @Override public int getItemCount() {
/*    return mDatas.size();*/
    return listItems.size();
  }
}
