package com.lovegod.newbuy.view.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.util.List;

/**
 * 标签列表的适配器
 * Created by ywx on 2017/7/13.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<String>list;
    private Context mContext;
    public RecommendAdapter(Context context,List<String>list){
        mContext=context;
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_recommend_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String text=list.get(position);
        holder.recommendText.setText(text);

        /**
         * 列表标签每一项的点击事件，直接跳转到商品结果页
         */
        holder.recommendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.OnItemClick(holder.recommendText.getText().toString());
                }else {
                    Log.e("RecommendAdapter","没有设置OnItemClickListener");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView recommendText;
        public ViewHolder(View itemView) {
            super(itemView);
            recommendText=(TextView)itemView.findViewById(R.id.recycler_recommend_item_hint);
        }
    }
    public interface OnItemClickListener{
        void OnItemClick(String itemText);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
