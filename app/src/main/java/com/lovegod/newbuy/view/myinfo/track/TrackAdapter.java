package com.lovegod.newbuy.view.myinfo.track;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Track;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;


/**
 * Created by ywx on 2017/8/14.
 * 足迹列表的适配器
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private Context mContext;
    private List<Track>trackList=new ArrayList<>();
    private ValueAnimator animator;

    public TrackAdapter(Context context,List<Track>list){
        trackList=list;
        mContext=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.track_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Track track=trackList.get(position);
        Glide.with(mContext).load(track.getLogo()).into(holder.logo);
        holder.name.setText(track.getGoodsname());
        holder.price.setText("¥"+track.getPrice());
        animator=ValueAnimator.ofInt(0,track.getTotal()).setDuration(1400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value= (int) animation.getAnimatedValue();
                holder.focus.setText(value+"");
            }
        });
        animator.start();

        /**
         * item点击监听，跳转到详情页
         */
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWorks.findCommodity(track.getCid(), new BaseObserver<Commodity>(mContext) {
                    @Override
                    public void onHandleSuccess(Commodity commodity) {
                        Intent intent=new Intent(mContext, GoodActivity.class);
                        intent.putExtra("commodity",commodity);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onHandleError(Commodity commodity) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,focus;
        ImageView logo;
        RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            layout=(RelativeLayout)itemView.findViewById(R.id.track_item_layout);
            name= (TextView) itemView.findViewById(R.id.track_item_name);
            price=(TextView)itemView.findViewById(R.id.track_item_price);
            focus=(TextView)itemView.findViewById(R.id.track_item_focus);
            logo=(ImageView)itemView.findViewById(R.id.track_item_image);
        }
    }
}
