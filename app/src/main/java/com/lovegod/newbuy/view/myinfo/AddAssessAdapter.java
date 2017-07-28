package com.lovegod.newbuy.view.myinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/26.
 */

public class AddAssessAdapter extends RecyclerView.Adapter<AddAssessAdapter.ViewHolder> {
    private Context mContext;
    private List<Order.OrderGoods>orderGoodsList=new ArrayList<>();

    public AddAssessAdapter(Context context,List<Order.OrderGoods>orderGoodses){
        mContext=context;
        orderGoodsList=orderGoodses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.add_assess_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order.OrderGoods orderGoods=orderGoodsList.get(position);
        Glide.with(mContext).load(orderGoods.getLogo()).into(holder.headImage);
        holder.nameText.setText(orderGoods.getGoodsname());
        holder.paramText.setText(orderGoods.getParam());
        NetWorks.getOrderById(orderGoods.getOid(), new BaseObserver<Order>(mContext) {
            @Override
            public void onHandleSuccess(Order order) {
                holder.timeText.setText("成交时间: "+order.getDealtime());
            }

            @Override
            public void onHandleError(Order order) {

            }
        });

        holder.commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,PublishAssessActivity.class);
                intent.putExtra("assess_goods",orderGoods);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderGoodsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headImage;
        TextView nameText,paramText,timeText;
        Button commitButton;
        public ViewHolder(View itemView) {
            super(itemView);
            headImage=(ImageView)itemView.findViewById(R.id.add_assess_item_image);
            nameText=(TextView)itemView.findViewById(R.id.add_assess_item_name);
            paramText=(TextView)itemView.findViewById(R.id.add_assess_item_param);
            timeText=(TextView)itemView.findViewById(R.id.add_assess_item_dealtime);
            commitButton=(Button)itemView.findViewById(R.id.add_assess_item_button);
        }
    }

}
