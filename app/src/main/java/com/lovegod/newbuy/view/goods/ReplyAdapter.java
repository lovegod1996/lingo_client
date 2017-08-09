package com.lovegod.newbuy.view.goods;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Circle;
import com.bumptech.glide.Glide;
import com.google.zxing.oned.ITFReader;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Quest;
import com.lovegod.newbuy.bean.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ywx on 2017/7/31.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
    private List<Quest.Reply>replyList=new ArrayList<>();
    private Context mContext;

    public ReplyAdapter(Context context,List<Quest.Reply>replies){
        mContext=context;
        replyList=replies;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.show_one_ask_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Quest.Reply reply=replyList.get(position);
        NetWorks.getIdInfo(reply.getUid(), new BaseObserver<User>(mContext) {
            @Override
            public void onHandleSuccess(User user) {
                if(!TextUtils.isEmpty(user.getHeaderpic())) {
                    Glide.with(mContext).load(user.getHeaderpic()).into(holder.userImage);
                }
                holder.userName.setText(user.getUsername());
            }

            @Override
            public void onHandleError(User user) {

            }
        });
        holder.time.setText(reply.getRetime());
        holder.replyText.setText(reply.getContent());
        if(reply.getBuystatue()==1){
            holder.buyStatue.setBackgroundColor(Color.parseColor("#737272"));
            holder.buyStatue.setText("未买");
        }
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName,time,replyText,buyStatue;
        public ViewHolder(View itemView) {
            super(itemView);
            buyStatue=(TextView)itemView.findViewById(R.id.one_ask_item_buystatue);
            userImage=(CircleImageView)itemView.findViewById(R.id.one_ask_item_image);
            userName=(TextView)itemView.findViewById(R.id.one_ask_item_name);
            time=(TextView)itemView.findViewById(R.id.one_ask_item_time);
            replyText=(TextView) itemView.findViewById(R.id.one_ask_item_reply);
        }
    }
}
