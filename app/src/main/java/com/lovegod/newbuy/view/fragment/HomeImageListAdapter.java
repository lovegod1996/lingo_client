package com.lovegod.newbuy.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ywx.lib.StarRating;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Location;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.utils.distance.DistanceUtil;
import com.lovegod.newbuy.utils.system.SpUtils;

import java.util.List;

/**
 * Created by 123 on 2017/4/17.
 */

public class HomeImageListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<Shop> shops;

public void bindData(Context context,List<Shop> shops){
    this.context=context;
    this.inflater=LayoutInflater.from(context);
    this.shops=shops;
}

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public Object getItem(int i) {
        return shops.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.suggest_shop_list,null);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageview1);
            viewHolder.name= (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.instruction= (TextView) convertView.findViewById(R.id.shop_instruction);
            viewHolder.sale=(TextView)convertView.findViewById(R.id.shop_sale);
            viewHolder.distance=(TextView)convertView.findViewById(R.id.shop_distance);
            viewHolder.type=(TextView)convertView.findViewById(R.id.shop_type);
            viewHolder.description=(TextView)convertView.findViewById(R.id.shop_description);
            viewHolder.starRating=(StarRating)convertView.findViewById(R.id.shop_star);
            viewHolder.scope=(TextView)convertView.findViewById(R.id.shop_scope);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Shop shop=shops.get(position);
        //使用Glide加载图片
        Glide.with(context)
                .load(shop.getLogo())
                .error(R.mipmap.shop_bg_1)
                .fitCenter()
              //  .placeholder(R.mipmap.shop_bg_1)
                .into(viewHolder.imageView);

        viewHolder.name.setText(shop.getShopname());
        viewHolder.instruction.setText(shop.getSaddress());
        viewHolder.sale.setText(shop.getSalesvo()+"已卖");
        //获取用户位置
        Location location= (Location) SpUtils.getObject(context,"location");
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
        return  convertView;

    }
    class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView instruction;
        TextView sale;
        TextView distance;
        TextView description;
        TextView type;
        TextView scope;
        StarRating starRating;
    }
}
