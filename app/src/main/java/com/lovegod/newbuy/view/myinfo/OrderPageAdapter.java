package com.lovegod.newbuy.view.myinfo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.view.carts.OrderInfoActivty;
import com.lovegod.newbuy.view.carts.PayChooseActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/22.
 * 订单详情的列表适配器
 */

public class OrderPageAdapter extends RecyclerView.Adapter<OrderPageAdapter.ViewHolder> {
    private Context mContext;
    private List<Order>orderList=new ArrayList<>();
    private Activity mActivity;
    public OrderPageAdapter(Context context, List<Order>orders,Activity activity){
        mContext=context;
        orderList=orders;
        mActivity=activity;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.order_viewpager_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Order order=orderList.get(position);

        holder.goodsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, OrderInfoActivty.class);
                intent.putExtra("order_info",order);
                mContext.startActivity(intent);
            }
        });

        //动态增加商品布局
        queryGoodInfo(holder,order);
        //保留一位小数
        final DecimalFormat df=new DecimalFormat("######0.0");
        holder.price.setText("¥"+df.format(order.getTotalprice()));
        holder.goodsNum.setText("共"+order.getCount()+"件商品");

        queryShop(holder,order);

        //不同状态显示不同布局
        if(order.getStatue()==0){
            holder.state.setText("等待买家付款");
            holder.payButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.noteButton.setVisibility(View.GONE);
            holder.queryButton.setVisibility(View.GONE);
            holder.addAssessButton.setVisibility(View.GONE);
            holder.sureButton.setVisibility(View.GONE);
        }else if (order.getStatue()==1){
            holder.state.setText("等待卖家发货");
            holder.payButton.setVisibility(View.GONE);
            holder.queryButton.setVisibility(View.GONE);
            holder.addAssessButton.setVisibility(View.GONE);
            holder.sureButton.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.noteButton.setVisibility(View.VISIBLE);
        }else if (order.getStatue()==2){
            holder.state.setText("等待买家收货");
            holder.sureButton.setVisibility(View.VISIBLE);
            holder.queryButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(View.GONE);
            holder.noteButton.setVisibility(View.GONE);
            holder.addAssessButton.setVisibility(View.GONE);
            holder.payButton.setVisibility(View.GONE);
        }else if (order.getStatue()==3){
            holder.state.setText("交易成功");
            holder.cancelButton.setVisibility(View.GONE);
            holder.noteButton.setVisibility(View.GONE);
            holder.sureButton.setVisibility(View.GONE);
            holder.payButton.setVisibility(View.GONE);
            holder.addAssessButton.setVisibility(View.GONE);
            holder.queryButton.setVisibility(View.VISIBLE);
        }


        /**
         * 付款按钮监听
         */
        holder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> orderList=new ArrayList<>();
                orderList.add(order.getOid()+"");
                Intent intent=new Intent(mContext, PayChooseActivity.class);
                intent.putStringArrayListExtra("order_data",orderList);
                mContext.startActivity(intent);
                mActivity.finish();
            }
        });

        /**
         * 取消订单按钮监听
         */
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建对话框再度确认
                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext).setTitle("确认").setMessage("确定取消该订单吗?");
                Dialog dialog;
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetWorks.cancelOrder(order.getOid(), new BaseObserver<Order>(mContext) {
                            @Override
                            public void onHandleSuccess(Order order) {
                                //删除成功就删除掉该位置的数据并通知适配器数据改变
                                orderList.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onHandleError(Order order) {

                            }
                        });
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog=builder.create();
                dialog.show();
            }
        });

        /**
         * 确认收货的监听
         */
        holder.sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext).setTitle("确认").setMessage("请确定收到货后再确认收货以免钱货两空。");
                Dialog dialog;
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetWorks.confirmTheGoods(order.getOid(), new BaseObserver<Order>(mContext) {
                            @Override
                            public void onHandleSuccess(Order order) {
                                order.setStatue(3);
                                orderList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(mContext,"收货成功，别忘了去评价你买到的宝贝哦",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onHandleError(Order order) {
                            }
                        });
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog=builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName,state,goodsNum,price;
        LinearLayout goodsLayout;
        Button addAssessButton,queryButton,sureButton,cancelButton,noteButton,payButton;
        public ViewHolder(View itemView) {
            super(itemView);
            shopName=(TextView)itemView.findViewById(R.id.order_viewpager_item_shopname);
            state=(TextView)itemView.findViewById(R.id.order_viewpager_item_statuetext);
            goodsNum=(TextView)itemView.findViewById(R.id.order_viewpager_item_count);
            price=(TextView)itemView.findViewById(R.id.order_viewpager_item_price);
            goodsLayout=(LinearLayout)itemView.findViewById(R.id.order_viewpager_item_goodslayout);
            addAssessButton=(Button)itemView.findViewById(R.id.order_viewpager_add_assess_button);
            queryButton=(Button)itemView.findViewById(R.id.order_viewpager_query_button);
            sureButton=(Button)itemView.findViewById(R.id.order_viewpager_sure_button);
            cancelButton=(Button)itemView.findViewById(R.id.order_viewpager_cancel_button);
            noteButton=(Button)itemView.findViewById(R.id.order_viewpager_note_button);
            payButton=(Button)itemView.findViewById(R.id.order_viewpager_pay_button);
        }
    }

    /**
     * 获取订单中每个商品的信息添加到布局
     * @param holder
     * @param order
     */
    private void queryGoodInfo(final ViewHolder holder, final Order order){
                holder.goodsLayout.removeAllViews();
                for(final Order.OrderGoods goods:order.getOrderGoodsList()){
                    LinearLayout item = new LinearLayout(mContext);
                    LayoutInflater.from(mContext).inflate(R.layout.pay_good_item, item);
                    ImageView goodPic = (ImageView) item.findViewById(R.id.pay_good_item_pic);
                    TextView goodName = (TextView) item.findViewById(R.id.pay_good_item_name);
                    TextView goodInfo = (TextView) item.findViewById(R.id.pay_good_item_info);
                    TextView goodPrice = (TextView) item.findViewById(R.id.pay_good_item_price);
                    TextView goodNum = (TextView) item.findViewById(R.id.pay_good_item_num);
                    Glide.with(mContext).load(goods.getLogo()).into(goodPic);
                    goodName.setText(goods.getGoodsname());
                    goodInfo.setText(goods.getParam());
                    goodPrice.setText("¥"+goods.getTotalprice()*1.0F/goods.getCount());
                    goodNum.setText("×"+goods.getCount());
                    holder.goodsLayout.addView(item);
                }
    }
     private void queryShop(final ViewHolder holder, Order order){
         NetWorks.getIDshop(order.getSid(), new BaseObserver<Shop>(mContext) {
             @Override
             public void onHandleSuccess(Shop shop) {
                 holder.shopName.setText(shop.getShopname());
             }

             @Override
             public void onHandleError(Shop shop) {

             }
         });
     }
}
