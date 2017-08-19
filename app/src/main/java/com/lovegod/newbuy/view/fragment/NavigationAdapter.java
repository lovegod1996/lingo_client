package com.lovegod.newbuy.view.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ywx on 2017/8/17.
 * 头部导航栏适配器
 */

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {
    private int[] images;
    private Context mContext;

    public NavigationAdapter(Context context,int[] images){
        mContext=context;
        this.images=images;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.home_navigation_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int image=images[position];
        Glide.with(mContext).load(image).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView pic;
        public ViewHolder(View itemView) {
            super(itemView);
            pic=(CircleImageView)itemView.findViewById(R.id.home_navigation_item_pic);
        }
    }
}
