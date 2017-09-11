package com.lovegod.newbuy.view.goods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ywx.lib.StarRating;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Compare;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by yue on 2017/9/9.
 */

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.ViewHolder> {
    private List<Compare>compares;
    private Context mContext;


    public CompareAdapter(Context context,List<Compare>compares){
        this.compares=compares;
        mContext=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.compare_listview_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Compare compare=compares.get(position);
        Glide.with(mContext).load(compare.getPic()).into(holder.pic);
        holder.rating.setCurrentStarCount((int) compare.getRate());
        holder.name.setText(compare.getShopName());

        //保留一位小数
        DecimalFormat df=new DecimalFormat("#.0");
        holder.price.setText(df.format(compare.getPrice())+"");
        holder.sale.setText("商品销量:"+compare.getSale());
    }

    @Override
    public int getItemCount() {
        return compares.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        StarRating rating;
        TextView name,price,sale;
        public ViewHolder(View itemView) {
            super(itemView);
            pic=(ImageView)itemView.findViewById(R.id.compare_shop_image);
            rating=(StarRating)itemView.findViewById(R.id.ratingbar);
            name=(TextView)itemView.findViewById(R.id.compare_shopname);
            price=(TextView)itemView.findViewById(R.id.compare_money);
            sale=(TextView)itemView.findViewById(R.id.compare_sale);
        }
    }
}
