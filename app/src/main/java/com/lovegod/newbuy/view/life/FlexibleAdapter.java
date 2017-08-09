package com.lovegod.newbuy.view.life;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Location;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.utils.distance.DistanceUtil;
import com.lovegod.newbuy.utils.system.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/7.
 */

public class FlexibleAdapter extends RecyclerView.Adapter {
    private List<Shop>shopList=new ArrayList<>();
    private Context mContext;
    private static final int SINGLE_TYPE=0;
    private static final int DOUBLE_TYPE=1;
    public FlexibleAdapter(Context context,List<Shop>shops){
        mContext=context;
        shopList=shops;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder=null;
        switch (viewType){
            case SINGLE_TYPE:
                view= LayoutInflater.from(mContext).inflate(R.layout.activity_life,parent,false);
                viewHolder=new SingleViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Shop shop=shopList.get(position);
        switch (getItemViewType(position)){
            case SINGLE_TYPE:
                SingleViewHolder singeViewHolder= (SingleViewHolder) holder;
                bindSingleTypeData(shop,singeViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager= (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type=getItemViewType(position);
                    switch (type){
                        case SINGLE_TYPE:
                            return 2;
                        case DOUBLE_TYPE:
                            return 1;
                        default:
                            return 2;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position%3){
            case SINGLE_TYPE:
                return SINGLE_TYPE;
            default:
                return DOUBLE_TYPE;
        }
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView instruction;
        TextView sale;
        TextView distance;
        TextView description;
        TextView type;
        public SingleViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView)itemView.findViewById(R.id.imageview1);
            name= (TextView)itemView.findViewById(R.id.shop_name);
            instruction= (TextView) itemView.findViewById(R.id.shop_instruction);
            sale=(TextView)itemView.findViewById(R.id.shop_sale);
            distance=(TextView)itemView.findViewById(R.id.shop_distance);
            type=(TextView)itemView.findViewById(R.id.shop_type);
            description=(TextView)itemView.findViewById(R.id.shop_description);
        }
    }

    /**
     * 设置列表布局的数据
     * @param shop 该项的数据Bean类
     * @param singleViewHolder
     */
    private void bindSingleTypeData(Shop shop,SingleViewHolder singleViewHolder){
        Glide.with(mContext)
                .load(shop.getLogo())
                .error(R.mipmap.shop_bg_1)
                .fitCenter()
                .into(singleViewHolder.imageView);

        singleViewHolder.name.setText(shop.getShopname());
        singleViewHolder.instruction.setText(shop.getSaddress());
        singleViewHolder.sale.setText("销售量:"+shop.getSalesvo());
        //获取用户位置
        Location location= (Location) SpUtils.getObject(mContext,"location");
        if(location!=null) {
            //获取距离
            int dis= (int) DistanceUtil.LantitudeLongitudeDist(Double.parseDouble(location.getLon()), Double.parseDouble(location.getLat()), shop.getLonggitude(), shop.getLatitude());
            if(dis>=1000) {
                singleViewHolder.distance.setText(dis / 1000f + "km");
            }else {
                singleViewHolder.distance.setText(dis+ "m");
            }
        }
        singleViewHolder.description.setText(shop.getSubscrib());
        singleViewHolder.type.setText(shop.getType());
    }
}
