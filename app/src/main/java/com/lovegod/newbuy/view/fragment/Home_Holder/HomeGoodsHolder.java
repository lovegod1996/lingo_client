package com.lovegod.newbuy.view.fragment.Home_Holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovegod.newbuy.R;

/**
 * Created by ywx on 2017/8/18.
 */

public class HomeGoodsHolder extends RecyclerView.ViewHolder {
    public ImageView pic;
    public TextView name,price,sale;
    public CardView layout;
    public HomeGoodsHolder(View itemView) {
        super(itemView);
        pic=(ImageView)itemView.findViewById(R.id.home_goods_pic);
        name=(TextView)itemView.findViewById(R.id.home_goods_name);
        price=(TextView)itemView.findViewById(R.id.home_goods_price);
        sale=(TextView)itemView.findViewById(R.id.home_goods_sale);
        layout=(CardView) itemView.findViewById(R.id.home_goods_layout);
    }
}
