package com.lovegod.newbuy.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.FavouriteQuest;
import com.lovegod.newbuy.bean.Quest;
import com.lovegod.newbuy.view.goods.AskActivity;
import com.lovegod.newbuy.view.goods.ShowOneAskActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created byywx on 2017/8/3.
 */

public class AskAnswerAdapter extends RecyclerView.Adapter<AskAnswerAdapter.ViewHolder> {
    private List<FavouriteQuest>questList=new ArrayList<>();
    private Context mContext;

    public AskAnswerAdapter(Context context,List<FavouriteQuest>favouriteQuests){
        mContext=context;
        questList=favouriteQuests;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.focus_question_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FavouriteQuest favouriteQuest=questList.get(position);
        holder.title.setText(favouriteQuest.getQuestname());
        holder.time.setText("关注时间:"+favouriteQuest.getLooktime());
        Glide.with(mContext).load(favouriteQuest.getGoodslogo()).into(holder.image);

        /**
         * 网络请求该问题的具体信息
         */
        NetWorks.getQuesByID(favouriteQuest.getQid(), new BaseObserver<Quest>() {
            @Override
            public void onHandleSuccess(final Quest quest) {
                holder.count.setText("已有"+quest.getReplies().size()+"个回答");
                /**
                 * 设置该问题点击跳转到详情页面
                 */
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle=new Bundle();
                        //商品信息
                        bundle.putInt("commodityId",quest.getCid());
                        //被点击的问题信息
                        bundle.putSerializable("quest",quest);
                        Intent intent=new Intent(mContext,ShowOneAskActivity.class);
                        intent.putExtra("bundle",bundle);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onHandleError(Quest quest) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return questList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,time,count;
        LinearLayout layout;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            time=(TextView)itemView.findViewById(R.id.focus_question_item_time);
            title=(TextView)itemView.findViewById(R.id.focus_question_item_title);
            count=(TextView)itemView.findViewById(R.id.focus_question_item_reply_count);
            layout=(LinearLayout)itemView.findViewById(R.id.focus_question_item_layout);
            image=(ImageView)itemView.findViewById(R.id.focus_question_item_image);
        }
    }
}
