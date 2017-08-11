package com.lovegod.newbuy.view.myinfo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.FavouriteGoods;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/1.
 * 收藏宝贝列表适配器
 */

public class FavouriteGoodsAdapter extends RecyclerView.Adapter<FavouriteGoodsAdapter.ViewHolder> {
    private List<FavouriteGoods>goodsList=new ArrayList<>();
    private Context mContext;
    private ObjectAnimator animator;
    private OnAnimEndListener onAnimEndListener;
    private ShopCartAdapter.OnItemClickListener onItemClickListener;

    public FavouriteGoodsAdapter(Context context,List<FavouriteGoods>list){
        mContext=context;
        goodsList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_child,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FavouriteGoods favouriteGoods=goodsList.get(position);

        //判断每个item是否处于可编辑状态，处于就显示删除按钮并且Item不可点击，否则就隐藏并且Item可点击
        if(favouriteGoods.isEdit()){
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.goodsLayout.setClickable(false);
            holder.goodsLayout.setEnabled(false);
            animator=ObjectAnimator.ofFloat(holder.deleteButton,"translationX",120f,0).setDuration(150*(position+1));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if((float)animation.getAnimatedValue()==120f){
                        if(onAnimEndListener!=null){
                            onAnimEndListener.onAnimEnd();
                        }
                    }
                }
            });
            animator.start();
        }else {
            animator=ObjectAnimator.ofFloat(holder.deleteButton,"translationX",0,120f).setDuration(150*(position+1));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if((float)animation.getAnimatedValue()==120f){
                        holder.deleteButton.setVisibility(View.GONE);
                        holder.goodsLayout.setClickable(true);
                        holder.goodsLayout.setEnabled(true);
                        if(onAnimEndListener!=null) {
                            onAnimEndListener.onAnimEnd();
                        }
                    }
                }
            });
            animator.start();
        }

        Glide.with(mContext).load(favouriteGoods.getGoodslogo()).into(holder.goodsPic);
        holder.goodsName.setText(favouriteGoods.getGoodsName());
        NetWorks.findCommodity(favouriteGoods.getCid(), new BaseObserver<Commodity>(mContext) {
            @Override
            public void onHandleSuccess(final Commodity commodity) {
                holder.goodsMoney.setText(commodity.getPrice()+"");
                /**
                 * item点击事件
                 */
                holder.goodsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, GoodActivity.class);
                        intent.putExtra("commodity",commodity);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onHandleError(Commodity commodity) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView goodsPic;
        TextView goodsName,goodsMoney;
        RelativeLayout goodsLayout;
        Button deleteButton;
        public ViewHolder(View itemView) {
            super(itemView);
            deleteButton=(Button)itemView.findViewById(R.id.good_delete_button);
            goodsPic=(ImageView)itemView.findViewById(R.id.good_picture);
            goodsName=(TextView)itemView.findViewById(R.id.good_name);
            goodsMoney=(TextView)itemView.findViewById(R.id.good_money);
            goodsLayout=(RelativeLayout)itemView.findViewById(R.id.good_layout);

            /**
             * 删除按钮监听
             */
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    NetWorks.cancelFoucusGoods(goodsList.get(getAdapterPosition()).getGaid(), new BaseObserver<FavouriteGoods>(mContext) {
                        @Override
                        public void onHandleSuccess(FavouriteGoods favouriteGoods) {
                            goodsList.remove(getAdapterPosition());
                            if(onItemClickListener!=null){
                                onItemClickListener.onItemClick(v,getAdapterPosition());
                            }
                        }

                        @Override
                        public void onHandleError(FavouriteGoods favouriteGoods) {

                        }
                    });
                }
            });
        }
    }

    public interface OnAnimEndListener{
        //动画结束的回调
        void onAnimEnd();
    }

    public void setOnAnimEndListener(OnAnimEndListener onAnimEndListener) {
        this.onAnimEndListener = onAnimEndListener;
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
