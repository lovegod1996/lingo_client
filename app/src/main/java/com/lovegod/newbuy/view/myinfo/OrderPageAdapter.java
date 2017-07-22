package com.lovegod.newbuy.view.myinfo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/22.
 */

public class OrderPageAdapter extends RecyclerView.Adapter<OrderPageAdapter.ViewHolder> {
    private Context mContext;
    private List<Order>orderList=new ArrayList<>();
    public OrderPageAdapter(Context context, List<Order>orders){
        mContext=context;
        orderList=orders;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.order_viewpager_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order=orderList.get(position);
        //不同状态显示不同布局
        if(order.getStatue()==0){
            holder.state.setText("等待买家付款");
            holder.rightButton.setText("付款");
            holder.midRightButton.setText("取消订单");
            holder.midLeftButton.setVisibility(View.GONE);
            holder.leftButton.setVisibility(View.GONE);
        }else if (order.getStatue()==1){
            holder.state.setText("等待卖家发货");
            holder.rightButton.setVisibility(View.GONE);
            holder.midRightButton.setText("");
        }else if (order.getStatue()==2){
            holder.state.setText("等待买家收货");
            holder.rightButton.setText("确认收货");
            holder.midRightButton.setText("查看物流");
            holder.midLeftButton.setVisibility(View.GONE);
            holder.leftButton.setVisibility(View.GONE);
        }else if (order.getStatue()==3){
            holder.state.setText("交易成功");
            holder.rightButton.setVisibility(View.GONE);
            holder.midRightButton.setText("追加评价");
            holder.midLeftButton.setText("查看物流");
            holder.leftButton.setText("删除订单");
        }

        //动态增加商品
        for(Order.OrderGoods goods:order.getOrderGoodsList()){
            LinearLayout item=new LinearLayout(mContext);
            LayoutInflater.from(mContext).inflate(R.layout.pay_good_item,item);
            ImageView goodsPic= (ImageView) item.findViewById(R.id.pay_good_item_pic);
            TextView goodName=(TextView)item.findViewById(R.id.pay_good_item_name);
            TextView goodInfo=(TextView)item.findViewById(R.id.pay_good_item_info);
            TextView goodPrice=(TextView)item.findViewById(R.id.pay_good_item_price);
            TextView goodNum=(TextView)item.findViewById(R.id.pay_good_item_num);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName,state,goodsNum,price;
        LinearLayout goodsLayout;
        Button rightButton,midRightButton,midLeftButton,leftButton;
        public ViewHolder(View itemView) {
            super(itemView);
            shopName=(TextView)itemView.findViewById(R.id.order_viewpager_item_shopname);
            state=(TextView)itemView.findViewById(R.id.order_viewpager_item_statuetext);
            goodsNum=(TextView)itemView.findViewById(R.id.order_viewpager_item_count);
            price=(TextView)itemView.findViewById(R.id.order_viewpager_item_price);
            goodsLayout=(LinearLayout)itemView.findViewById(R.id.order_viewpager_item_goodslayout);
            rightButton=(Button)itemView.findViewById(R.id.order_viewpager_right_button);
            midRightButton=(Button)itemView.findViewById(R.id.order_viewpager_midright_button);
            midLeftButton=(Button)itemView.findViewById(R.id.order_viewpager_midleft_button);
            leftButton=(Button)itemView.findViewById(R.id.order_viewpager_left_button);
        }
    }
}
