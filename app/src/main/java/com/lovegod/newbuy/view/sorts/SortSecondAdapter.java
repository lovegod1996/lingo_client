package com.lovegod.newbuy.view.sorts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.SortFrist;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;

import java.util.List;

/**
 * *****************************************
 * Created by thinking on 2017/5/24.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class SortSecondAdapter extends RecyclerView.Adapter<SortSecondAdapter.SecondViewHolder> {

    private List<SortFrist> m2Datas;
    private Context context;
    private MyRecyclerViewAdapter.OnItemClickListener itemClickListener;
    public SortSecondAdapter(Context context, List<SortFrist> m2Datas){
        this.context = context;
        this.m2Datas = m2Datas;
    }

    @Override
    public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SecondViewHolder holder = new SecondViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sort_second, parent, false));
        return holder;
    }
    public void setItemClickListener(MyRecyclerViewAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onBindViewHolder(final SortSecondAdapter.SecondViewHolder holder, final int position) {


        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        holder.itemView.setLayoutParams(params);//把params设置item布局
        Glide.with(context).load(m2Datas.get(position).getLogo()).fitCenter().into(holder.imageView);
        holder.textView.setText(m2Datas.get(position).getSecend());
      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener!=null){
                    itemClickListener.onItemClick(holder.itemView,position);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return m2Datas.size();
    }

    public class SecondViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public SecondViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.itemsort2_text);
            imageView = (ImageView) itemView.findViewById(R.id.itemsort2_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener!=null){
                        itemClickListener.onItemClick(itemView,getPosition());
                    }
                }
            });

    }
}
}