package com.lovegod.newbuy.view.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/15.
 */

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {
    private MyRecyclerViewAdapter.OnItemClickListener onItemClickListener;
    private Context mContext;
    private List<Shop> shopList;
    private List<Commodity>commodityList;
    public ShopItemAdapter(Context context,List<Shop>list){
        mContext=context;
        shopList=list;
    }

    @Override
    public ShopItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.shop_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShopItemAdapter.ViewHolder holder, int position) {
        Shop shop=shopList.get(position);
        Glide.with(mContext).load(shop.getLogo()).into(holder.logo);
        holder.shopName.setText(shop.getShopname());
        holder.shopInfo.setText("销量 "+shop.getSalesvo());
        NetWorks.getIDshopgoods(shop.getSid(), new BaseObserver<List<Commodity>>(mContext) {
            @Override
            public void onHandleSuccess(List<Commodity> commodities) {
                for(int i=0;i<commodities.size();i++) {
                    if(i==3){
                        break;
                    }
                    switch (i){
                        case 0:
                            Glide.with(mContext).load(commodities.get(i).getLogo()).into(holder.pic1);
                            holder.pic1_price.setText(commodities.get(i).getPrice()+"");
                            break;
                        case 1:
                            Glide.with(mContext).load(commodities.get(i).getLogo()).into(holder.pic2);
                            holder.pic2_price.setText(commodities.get(i).getPrice()+"");
                            break;
                        case 2:
                            Glide.with(mContext).load(commodities.get(i).getLogo()).into(holder.pic3);
                            holder.pic3_price.setText(commodities.get(i).getPrice()+"");
                            break;
                    }
                }
            }

            @Override
            public void onHandleError(List<Commodity> commodities) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView logo,pic1,pic2,pic3;
        TextView shopName,shopInfo,pic1_price,pic2_price,pic3_price;
        public ViewHolder(final View itemView) {
            super(itemView);
            logo=(ImageView)itemView.findViewById(R.id.shop_item_logo);
            pic1=(ImageView)itemView.findViewById(R.id.shop_item_pic1);
            pic2=(ImageView)itemView.findViewById(R.id.shop_item_pic2);
            pic3=(ImageView)itemView.findViewById(R.id.shop_item_pic3);
            shopName=(TextView)itemView.findViewById(R.id.shop_item_name);
            shopInfo=(TextView)itemView.findViewById(R.id.shop_item_info);
            pic1_price=(TextView)itemView.findViewById(R.id.shop_item_pic1_price);
            pic2_price=(TextView)itemView.findViewById(R.id.shop_item_pic2_price);
            pic3_price=(TextView)itemView.findViewById(R.id.shop_item_pic3_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView,getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(MyRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
