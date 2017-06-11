package com.lovegod.newbuy.view.sorts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StringLoader;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Created by thinking on 2017/5/30.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class CateGoodsAdapter extends RecyclerView.Adapter<CateGoodsAdapter.CategoodViewHolder> {

    private List<Commodity> Dates = new ArrayList<>();
    private Context context;
    private MyRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public CateGoodsAdapter(Context context, List<Commodity> Datas) {
        this.context = context;
        this.Dates = Datas;

    }

    public void setItemClickListener(MyRecyclerViewAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public CategoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategoodViewHolder holder = new CategoodViewHolder(LayoutInflater.from(context).inflate(R.layout.category_list0, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoodViewHolder holder, final int position) {
        /**
         * 得到item的LayoutParams布局参数
         */
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        holder.itemView.setLayoutParams(params);//把params设置item布局

        holder.textView1.setText(Dates.get(position).getProductname());//为控件绑定数据
        holder.textView2.setText(String.valueOf(Dates.get(position).getPrice()));
        Glide.with(context).load(Dates.get(position).getLogo()).fitCenter().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return Dates.size();
    }


    public class CategoodViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        ImageView imageView;

        public CategoodViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_cate_good);
            textView1 = (TextView) itemView.findViewById(R.id.cate_good_name);
            textView2 = (TextView) itemView.findViewById(R.id.cate_good_money);
            //为item添加普通点击回调
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(itemView, getPosition());
                    }
                }
            });
        }
    }
}