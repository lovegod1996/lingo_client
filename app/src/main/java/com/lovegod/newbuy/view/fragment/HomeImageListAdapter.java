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
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Shop;

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
            ViewHoler viewHoler;
        if(convertView==null){
            viewHoler=new ViewHoler();
            convertView=inflater.inflate(R.layout.suggest_shop_list,null);
             viewHoler.imageView= (ImageView) convertView.findViewById(R.id.imageview1);
            viewHoler.textView1= (TextView) convertView.findViewById(R.id.shop_name);
            viewHoler.textView2= (TextView) convertView.findViewById(R.id.shop_instruction);
            convertView.setTag(viewHoler);
        }else{
            viewHoler= (ViewHoler) convertView.getTag();
        }
        Shop shop=shops.get(position);
        //使用Glide加载图片
        Glide.with(context)
                .load(shop.getLogo())
                .error(R.mipmap.shop_bg_1)
                .fitCenter()
              //  .placeholder(R.mipmap.shop_bg_1)
                .into(viewHoler.imageView);

        viewHoler.textView1.setText(shop.getShopname());
        viewHoler.textView2.setText(shop.getSaddress());

        return  convertView;

    }
    class ViewHoler{
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
    }
}
