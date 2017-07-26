package com.lovegod.newbuy.view.myinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovegod.newbuy.R;

/**
 * Created by ywx on 2017/7/26.
 */

public class AddAssessAdapter extends RecyclerView.Adapter<AddAssessAdapter.ViewHolder> {
    private Context mContext;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.add_assess_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headImage;
        TextView nameText,paramText,timeText;
        Button commitButton;
        public ViewHolder(View itemView) {
            super(itemView);
            headImage=(ImageView)itemView.findViewById(R.id.add_assess_item_image);
            nameText=(TextView)itemView.findViewById(R.id.add_assess_item_name);
            paramText=(TextView)itemView.findViewById(R.id.add_assess_item_param);
            timeText=(TextView)itemView.findViewById(R.id.add_assess_item_dealtime);
            commitButton=(Button)itemView.findViewById(R.id.add_assess_item_button);
        }
    }
}
