package com.lovegod.newbuy.view.life;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;;
import com.lovegod.newbuy.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ywx on 2017/8/16.
 */

public class LifeGroupAdapter extends RecyclerView.Adapter<LifeGroupAdapter.ViewHolder> {
    private List<EMChatRoom>rooms=new ArrayList<>();
    private Context mContext;

    public LifeGroupAdapter(Context context,List<EMChatRoom>list){
        mContext=context;
        rooms=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.life_group_fragment,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EMChatRoom room=rooms.get(position);
        holder.name.setText(room.getName());
        holder.num.setText(room.getMemberCount()+"/"+room.getMaxUsers());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name,num;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.life_group_item_image);
            name=(TextView)itemView.findViewById(R.id.life_group_item_name);
            num=(TextView)itemView.findViewById(R.id.life_group_item_num);
        }
    }
}
