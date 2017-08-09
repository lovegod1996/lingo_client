package com.lovegod.newbuy.view.life;

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
import com.lovegod.newbuy.bean.Location;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.utils.distance.DistanceUtil;
import com.lovegod.newbuy.utils.system.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/9.
 */

public class NearbyShopAdapter extends RecyclerView.Adapter<NearbyShopAdapter.ViewHolder> {
    private List<Shop>shopList=new ArrayList<>();
    private Context mContext;

    public NearbyShopAdapter(List<Shop>shops,Context context){
        shopList=shops;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.suggest_shop_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shop shop=shopList.get(position);
        Glide.with(mContext)
                .load(shop.getLogo())
                .error(R.mipmap.shop_bg_1)
                .fitCenter()
                //  .placeholder(R.mipmap.shop_bg_1)
                .into(holder.imageView);

        holder.name.setText(shop.getShopname());
        holder.instruction.setText(shop.getSaddress());
        holder.sale.setText(shop.getSalesvo()+"已卖");
        //获取用户位置
        Location location= (Location) SpUtils.getObject(mContext,"location");
        if(location!=null) {
            //获取距离
            int dis= (int) DistanceUtil.LantitudeLongitudeDist(Double.parseDouble(location.getLon()), Double.parseDouble(location.getLat()), shop.getLonggitude(), shop.getLatitude());
            if(dis>=1000) {
                holder.distance.setText(dis / 1000f + "km");
            }else {
                holder.distance.setText(dis+ "m");
            }
        }
        holder.description.setText(shop.getSubscrib());
        holder.type.setText(shop.getType());
        holder.scope.setText(shop.getScope()+" |");
        holder.starRating.setCurrentStarCount((int)shop.getSlevel());
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView instruction;
        TextView sale;
        TextView distance;
        TextView description;
        TextView type;
        TextView scope;
        StarRating starRating;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.imageview1);
            name= (TextView) itemView.findViewById(R.id.shop_name);
            instruction= (TextView) itemView.findViewById(R.id.shop_instruction);
            sale=(TextView)itemView.findViewById(R.id.shop_sale);
            distance=(TextView)itemView.findViewById(R.id.shop_distance);
            type=(TextView)itemView.findViewById(R.id.shop_type);
            description=(TextView)itemView.findViewById(R.id.shop_description);
            starRating=(StarRating)itemView.findViewById(R.id.shop_star);
            scope=(TextView)itemView.findViewById(R.id.shop_scope);
        }
    }
}
