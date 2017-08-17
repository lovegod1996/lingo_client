package com.lovegod.newbuy.view.life;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ywx.lib.StarRating;
import com.hyphenate.chat.EMChatRoom;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Location;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.utils.distance.DistanceUtil;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.myview.ItemImageLayout;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/9.
 */

public class NearbyShopAdapter extends RecyclerView.Adapter{
    private List<Shop>shopList=new ArrayList<>();
    private List<EMChatRoom>chatRoomList=new ArrayList<>();
    private LifeGroupAdapter adapter;
    private Context mContext;

    public NearbyShopAdapter(List<Shop>shops,List<EMChatRoom>roomList,Context context){
        shopList=shops;
        mContext=context;
        chatRoomList=roomList;
        adapter=new LifeGroupAdapter(mContext,chatRoomList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case 0:
                view= LayoutInflater.from(mContext).inflate(R.layout.life_group_item,parent,false);
                GroupViewHolder groupViewHolder=new GroupViewHolder(view);
                return groupViewHolder;
            case 1:
                view= LayoutInflater.from(mContext).inflate(R.layout.suggest_shop_list,parent,false);
                ViewHolder viewHolder=new ViewHolder(view);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 0:
                LinearLayoutManager manager=new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                GroupViewHolder groupViewHolder= (GroupViewHolder) holder;
                groupViewHolder.recyclerView.setAdapter(adapter);
                groupViewHolder.recyclerView.setLayoutManager(manager);
                adapter.notifyDataSetChanged();
                break;
            case 1:
                Shop shop=shopList.get(position-1);
                ViewHolder viewHolder= (ViewHolder) holder;
                Glide.with(mContext)
                        .load(shop.getLogo())
                        .error(R.mipmap.shop_bg_1)
                        .fitCenter()
                        //  .placeholder(R.mipmap.shop_bg_1)
                        .into(viewHolder.imageView);

                viewHolder.name.setText(shop.getShopname());
                viewHolder.instruction.setText(shop.getSaddress());
                viewHolder.sale.setText(shop.getSalesvo()+"已卖");
                //获取用户位置
                Location location= (Location) SpUtils.getObject(mContext,"location");
                if(location!=null) {
                    //获取距离
                    int dis= (int) DistanceUtil.LantitudeLongitudeDist(Double.parseDouble(location.getLon()), Double.parseDouble(location.getLat()), shop.getLonggitude(), shop.getLatitude());
                    if(dis>=1000) {
                        viewHolder.distance.setText(dis / 1000f + "km");
                    }else {
                        viewHolder.distance.setText(dis+ "m");
                    }
                }
                viewHolder.description.setText(shop.getSubscrib());
                viewHolder.type.setText(shop.getType());
                viewHolder.scope.setText(shop.getScope()+" |");
                viewHolder.starRating.setCurrentStarCount((int)shop.getSlevel());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return shopList.size()+1;
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

    class GroupViewHolder extends RecyclerView.ViewHolder {
        ItemImageLayout imageLayout;
        RecyclerView recyclerView;
        public GroupViewHolder(View itemView) {
            super(itemView);
            imageLayout=(ItemImageLayout)itemView.findViewById(R.id.more_group);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.life_group_recycler);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else {
            return 1;
        }
    }
}
