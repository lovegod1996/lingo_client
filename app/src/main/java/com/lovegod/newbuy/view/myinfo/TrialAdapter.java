package com.lovegod.newbuy.view.myinfo;

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
import com.lovegod.newbuy.bean.Trial;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/10.
 * 体验的列表适配器
 */

public class TrialAdapter extends RecyclerView.Adapter<TrialAdapter.ViewHolder> {
    private List<Trial>trialList=new ArrayList<>();
    private Context mContext;

    public TrialAdapter(Context context,List<Trial>trials){
        mContext=context;
        trialList=trials;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.trial_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
            final Trial trial=trialList.get(position);
            NetWorks.findCommodity(trial.getCid(), new BaseObserver<Commodity>(mContext) {
                @Override
                public void onHandleSuccess(final Commodity commodity) {
                    holder.name.setText("【体验】" + commodity.getProductname());
                    holder.price.setText("¥" + commodity.getPrice());
                    Glide.with(mContext).load(commodity.getLogo()).into(holder.pic);
                    holder.time.setText("申请时间: " + trial.getApplytime());

                    /**
                     * item点击跳转到具体商品
                     */
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(mContext, GoodActivity.class);
                            intent.putExtra("commodity",commodity);
                            mContext.startActivity(intent);
                        }
                    });
                }

                @Override
                public void onHandleError(Commodity commodity) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return trialList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name,price,time,text;
        RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            pic=(ImageView)itemView.findViewById(R.id.trial_item_image);
            name=(TextView)itemView.findViewById(R.id.trial_item_name);
            price=(TextView)itemView.findViewById(R.id.trial_item_price);
            time=(TextView)itemView.findViewById(R.id.trial_item_time);
            text=(TextView)itemView.findViewById(R.id.trial_item_statuetext);
            layout=(RelativeLayout)itemView.findViewById(R.id.trial_item_layout);
        }
    }

}
