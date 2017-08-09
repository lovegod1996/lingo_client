package com.lovegod.newbuy.view.carts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.PayShopBean;
import com.lovegod.newbuy.bean.ShopCartBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class SubmitOrderAdapter extends RecyclerView.Adapter<SubmitOrderAdapter.ViewHolder> {
    private List<PayShopBean> dataList=new ArrayList<>();
    private Context mContext;

    public SubmitOrderAdapter(Context context,List<PayShopBean> list){
        mContext=context;
        dataList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.submit_shop_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PayShopBean payShopBean=dataList.get(position);

        //计算每个买的商店的总价
        double totalPrice=0F;
        int goodsNum=0;
        //找出该店家的所有商品
        for(ShopCartBean shopCartBean:payShopBean.goodList){
            totalPrice+=shopCartBean.getPrice()*shopCartBean.getAmount()*1.0F;
            goodsNum+=shopCartBean.getAmount();
            LinearLayout item=new LinearLayout(mContext);
            LayoutInflater.from(mContext).inflate(R.layout.pay_good_item,item);
            ImageView goodsPic= (ImageView) item.findViewById(R.id.pay_good_item_pic);
            TextView goodName=(TextView)item.findViewById(R.id.pay_good_item_name);
            TextView goodInfo=(TextView)item.findViewById(R.id.pay_good_item_info);
            TextView goodPrice=(TextView)item.findViewById(R.id.pay_good_item_price);
            TextView goodNum=(TextView)item.findViewById(R.id.pay_good_item_num);
            goodNum.setText("×"+shopCartBean.getAmount());
            goodPrice.setText("¥"+shopCartBean.getPrice());
            goodInfo.setText(shopCartBean.getCommodity_select());
            goodName.setText(shopCartBean.getCommodity_name());
            Glide.with(mContext).load(shopCartBean.getCommodity_pic()).into(goodsPic);
            holder.shopListLayout.addView(item);
        }
        holder.shopName.setText(payShopBean.getShopname());
        holder.goodNum.setText("共"+goodsNum+"件商品");
        //保留一位小数
        DecimalFormat df=new DecimalFormat("######0.0");
        holder.price.setText("¥"+df.format(totalPrice));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout shopListLayout;
        TextView shopName,goodNum,price;
        EditText messageEdit;
        public ViewHolder(View itemView) {
            super(itemView);
            shopListLayout=(LinearLayout)itemView.findViewById(R.id.submit_shop_item_shoplist);
            shopName=(TextView)itemView.findViewById(R.id.submit_shop_item_shopname);
            goodNum=(TextView)itemView.findViewById(R.id.submit_shop_item_num);
            price=(TextView)itemView.findViewById(R.id.submit_shop_item_price);
            messageEdit=(EditText)itemView.findViewById(R.id.submit_shop_item_message_edit);
        }
    }
}
