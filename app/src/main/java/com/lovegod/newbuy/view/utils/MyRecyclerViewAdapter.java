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
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.bean.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lovegod.newbuy.R.id.shop;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> implements View.OnClickListener {

  public Context mContext;
  public List<String> mDatas;
  List<Map<String, Object>> listItems;
  public LayoutInflater mLayoutInflater;

  private List<Commodity> goodd;
  private OnItemClickListener mOnItemClickListener=null;


  @Override
  public void onClick(View v) {
if(mOnItemClickListener!=null){
  mOnItemClickListener.onItemClick(v,(int)v.getTag());
}
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

/*  int[] shop_images = {R.mipmap.tv1, R.mipmap.tv2, R.mipmap.tv3, R.mipmap.tv4, R.mipmap.tv5, R.mipmap.tv3,R.mipmap.tv4, R.mipmap.tv5, R.mipmap.tu6};
  String[] shop_name={"百业家电专营店","思迪电器专营店","中佳电器专营店","华强官方旗舰店","粤城电器","杭越电器专营店","中佳电器专营店","华强官方旗舰店","粤城电器"};
  int[] moneys={3900,3990,4099,4199,4199,4399,4199,4199,4399};*/

  public MyRecyclerViewAdapter(Context mContext, List<Commodity> goodd) {
    this.mContext = mContext;
    this.mLayoutInflater = LayoutInflater.from(mContext);
    this.goodd = goodd;
  }


 /**
   * 创建ViewHolder
   */
  @Override public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
    MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
    mView.setOnClickListener(this);
    return mViewHolder;
  }

  /**
   * 绑定ViewHoler，给item中的控件设置数据
   */
  @Override public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
    if (mOnItemClickListener != null) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          int position=holder.getLayoutPosition();
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

    //使用Glide加载图片
    Glide.with(mContext)
            .load(goodd.get(position).getLogo())
            .error(R.mipmap.tu1)
            .fitCenter()
            .into(holder.good_imageview);
    holder.good_textview.setText(goodd.get(position).getProductname());
    holder.money_textview.setText(String.valueOf(goodd.get(position).getPrice()));

    //holder.good_textview.setText(mDatas.get(position));
  }

  @Override
  public int getItemCount() {
       return  goodd.size();
  }

}
  /*  listItems = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < shop_images.length; i++) {
      Map<String, Object> listItem = new HashMap<String, Object>();
      listItem.put("goodImage",shop_images[i]);
      listItem.put("goodName",shop_name[i]);
      listItem.put("goodMoneys",moneys[i]);
      listItems.add(listItem);*/


  /*public MyRecyclerViewAdapter(Context mContext) {
    this.mContext = mContext;
    this.mLayoutInflater=LayoutInflater.from(mContext);
   // mLayoutInflater = LayoutInflater.from(mContext);


     listItems = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < shop_images.length; i++) {
      Map<String, Object> listItem = new HashMap<String, Object>();
      listItem.put("goodImage",shop_images[i]);
      listItem.put("goodName",shop_name[i]);
      listItem.put("goodMoneys",moneys[i]);
      listItems.add(listItem);
    }*/

/*    mDatas = new ArrayList<>();
    for (int i =0; i <shop_images.length; i++) {
      mDatas.add();
    }
  }*/
/*
  *//**
   * 创建ViewHolder
   *//*
  @Override public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
    MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
    return mViewHolder;
  }

  *//**
   * 绑定ViewHoler，给item中的控件设置数据
   *//*
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

    int a= (int) listItems.get(position).get("goodImage");
    holder.good_imageview.setImageResource(a);
    holder.good_textview.setText((String)listItems.get(position).get("goodName"));
    holder.money_textview.setText(String.valueOf(listItems.get(position).get("goodMoneys")));

    //holder.good_textview.setText(mDatas.get(position));
  }

  @Override public int getItemCount() {
return  listItems.size();
   // return mDatas.size();
  }*/

