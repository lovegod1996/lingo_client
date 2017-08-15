package com.lovegod.newbuy.view.myinfo.assess;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/27.
 */

public class AssessPicAdapter extends RecyclerView.Adapter<AssessPicAdapter.ViewHolder> {
    private List<ImageItem> imagesList=new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    public AssessPicAdapter(Context context, List<ImageItem>list){
        mContext=context;
        imagesList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.publish_assess_pic_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageItem imageItem=imagesList.get(position);
        Glide.with(mContext).load(imageItem.path).into(holder.contentImage);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cancelImage,contentImage;
        public ViewHolder(View itemView) {
            super(itemView);
            cancelImage=(ImageView)itemView.findViewById(R.id.publish_assess_pic_item_cancel);
            contentImage=(ImageView)itemView.findViewById(R.id.publish_assess_pic_item_image);

            //设置每张图片右上角删除键的监听
            cancelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
