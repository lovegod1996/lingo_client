package com.lovegod.newbuy.view.myinfo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.FavouriteShop;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.view.Shop2Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/2.
 * 收藏店铺列表的适配器
 */

public class FavouriteShopAdapter extends RecyclerView.Adapter<FavouriteShopAdapter.ViewHolder> {
    private List<FavouriteShop>shopList=new ArrayList<>();
    private Context mContext;
    private ObjectAnimator animator;
    private FavouriteGoodsAdapter.OnAnimEndListener onAnimEndListener;

    public FavouriteShopAdapter(Context context,List<FavouriteShop>list){
        mContext=context;
        shopList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.focus_shop_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FavouriteShop favouriteShop=shopList.get(position);

        if(favouriteShop.isEdit()){
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.layout.setClickable(false);
            holder.layout.setEnabled(false);
            animator= ObjectAnimator.ofFloat(holder.deleteButton,"translationX",120f,0).setDuration(150*(position+1));
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
                        holder.layout.setClickable(true);
                        holder.layout.setEnabled(true);
                        if(onAnimEndListener!=null) {
                            onAnimEndListener.onAnimEnd();
                        }
                    }
                }
            });
            animator.start();
        }

        Glide.with(mContext).load(favouriteShop.getShoplogo()).into(holder.shopLogo);
        holder.shopName.setText(favouriteShop.getShopname());
        NetWorks.getIDshop(favouriteShop.getSid(), new BaseObserver<Shop>(mContext) {
            @Override
            public void onHandleSuccess(final Shop shop) {
                holder.shopType.setText(shop.getType());
                /**
                 * 每个item的点击监听
                 */
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, Shop2Activity.class);
                        intent.putExtra("shop",shop);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onHandleError(Shop shop) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView shopLogo;
        TextView shopName,shopType;
        LinearLayout layout;
        Button deleteButton;
        public ViewHolder(View itemView) {
            super(itemView);
            shopLogo=(ImageView)itemView.findViewById(R.id.focus_shop_item_logo);
            shopName=(TextView)itemView.findViewById(R.id.focus_shop_item_name);
            shopType=(TextView)itemView.findViewById(R.id.focus_shop_item_type);
            layout=(LinearLayout) itemView.findViewById(R.id.focus_shop_item_layout);
            deleteButton=(Button)itemView.findViewById(R.id.focus_shop_item_delete);

            /**
             * 删除按钮监听
             */
           deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetWorks.cancelShopFocus(shopList.get(getAdapterPosition()).getSaid(), new BaseObserver<FavouriteShop>(mContext) {
                        @Override
                        public void onHandleSuccess(FavouriteShop favouriteShop) {
                            shopList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }

                        @Override
                        public void onHandleError(FavouriteShop favouriteShop) {

                        }
                    });
                }
            });
        }
    }

    public void setOnAnimEndListener(FavouriteGoodsAdapter.OnAnimEndListener onAnimEndListener) {
        this.onAnimEndListener = onAnimEndListener;
    }
}
