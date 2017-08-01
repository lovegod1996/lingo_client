package com.lovegod.newbuy.view.goods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Quest;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/28.
 * 问答的适配器
 */

public class AskAdapter extends RecyclerView.Adapter<AskAdapter.ViewHolder> {
    private List<Quest>questList=new ArrayList<>();
    private Context mContext;
    private ShopCartAdapter.OnItemClickListener onItemClick;

    public AskAdapter(Context context,List<Quest>quests){
        questList=quests;
        mContext=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.ask_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Quest quest=questList.get(position);
        holder.askText.setText(quest.getTitle());
        if(quest.getReplies().size()!=0) {
            holder.answerText.setText(quest.getReplies().get(0).getContent());
        }else {
            holder.answerText.setText("(该问题还没有人回答,快来回答他吧~)");
        }
        holder.moreText.setText("查看"+quest.getReplies().size()+"个回答");
        holder.askTime.setText(quest.getAsktime());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onItemClick.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView askText,answerText,moreText,askTime;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            askText=(TextView)itemView.findViewById(R.id.ask_item_asktext);
            answerText=(TextView)itemView.findViewById(R.id.ask_item_answertext);
            moreText=(TextView)itemView.findViewById(R.id.ask_item_more);
            askTime=(TextView)itemView.findViewById(R.id.ask_item_time);
            layout=(LinearLayout)itemView.findViewById(R.id.ask_item_layout);
        }
    }

    public void setOnItemClick(ShopCartAdapter.OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }
}
