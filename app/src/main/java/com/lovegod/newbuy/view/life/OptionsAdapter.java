package com.lovegod.newbuy.view.life;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Options;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/9.
 * 感兴趣的选项卡适配器
 */

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {
    private Context mContext;
    private List<Options>optionsList=new ArrayList<>();
    private ShopCartAdapter.OnItemClickListener onItemClickListener;

    public OptionsAdapter(Context context,List<Options>optionses){
        mContext=context;
        optionsList=optionses;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.round_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Options options=optionsList.get(position);
        holder.text.setText(options.getText());
        if(options.isChoose()){
            holder.text.setTextColor(Color.WHITE);
            holder.text.setBackgroundResource(R.drawable.round_select_shape);
        }else {
            holder.text.setTextColor(Color.parseColor("#8E8E8E"));
            holder.text.setBackgroundResource(R.drawable.round_unselect_shape);
        }

        /**
         * item点击监听
         */
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.round_item_text);
            layout=(LinearLayout)itemView.findViewById(R.id.round_item_layout);
        }
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
