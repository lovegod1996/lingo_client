package com.lovegod.newbuy.view.sorts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.SortFrist;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * *****************************************
 * Created by thinking on 2017/5/9.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class SortFirstAdapter extends RecyclerView.Adapter<SortFirstAdapter.FirstViewHolder> {

    private ArrayList<String> mDates=new ArrayList<>();
    private Context context;
    private MyRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public SortFirstAdapter(Context context, ArrayList<String> m1Datas){
        this.context = context;
        this.mDates = m1Datas;

    }
    public void setItemClickListener(MyRecyclerViewAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public FirstViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FirstViewHolder holder = new FirstViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sort_first, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final FirstViewHolder holder, final int position) {
        /**
         * 得到item的LayoutParams布局参数
         */
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        holder.itemView.setLayoutParams(params);//把params设置item布局

        holder.textView.setText(mDates.get(position));//为控件绑定数据
        //为TextView添加监听回调
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder.textView,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }


    public class FirstViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public FirstViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.sort_first_name);
            //为item添加普通点击回调
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(itemView,getPosition());
                    }
                }
            });
        }
    }
}
