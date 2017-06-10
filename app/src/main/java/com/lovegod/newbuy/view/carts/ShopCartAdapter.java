package com.lovegod.newbuy.view.carts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.util.List;

import retrofit2.http.Field;

import static android.R.attr.data;
import static android.R.string.no;
import static com.lovegod.newbuy.R.id.iv_item_shopcart_shopselect;

/**
 * Created by Administrator on 2016/10/14.
 */

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder> {

    private Context context;
    //private List<ShopCartBean.CartlistBean> data;
    private List<ShopCartBean> data;
    private View headerView;
    private OnDeleteClickListener mOnDeleteClickListener;
    private OnEditClickListener mOnEditClickListener;
    private OnResfreshListener mOnResfreshListener;

    public ShopCartAdapter(Context context, List<ShopCartBean> data) {
        this.context = context;
        this.data = data;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_shopcart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Glide.with(context).load(data.get(position).getCommodity_pic()).into(holder.ivShopCartgoodPic);
        if (position > 0) {
            if (data.get(position).getSid() == data.get(position - 1).getSid()) {
                holder.llShopCartHeader.setVisibility(View.GONE);
            } else {
                holder.llShopCartHeader.setVisibility(View.VISIBLE);
            }
        } else {
            holder.llShopCartHeader.setVisibility(View.VISIBLE);
        }

        // holder.tvShopCartClothColor.setText("颜色：" + data.get(position).getColor());
        // holder.tvShopCartClothSize.setText( data.get(position).getSize());
        holder.tvShopCartgoodName.setText(data.get(position).getCommodity_name());
        holder.tvShopCartShopName.setText(data.get(position).getShopname());
        holder.tvShopCartgoodPrice.setText(String.valueOf(data.get(position).getPrice()));
        holder.tvShopCartgoodColor.setText(data.get(position).getCommodity_select());
        holder.etShopCartgoodNum.setText(data.get(position).getAmount() + "");

        if (mOnResfreshListener != null) {
            boolean isSelect = false;
            for (int i = 0; i < data.size(); i++) {

                if (!data.get(i).isSelect()) {
                    isSelect = false;
                    break;
                } else {
                    isSelect = true;
                }
            }
            mOnResfreshListener.onResfresh(isSelect);
        }

        holder.etShopCartgoodNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////

            }
        });
        holder.ivShopCartgoodMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (data.get(position).getAmount() == 1) {

                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(context);
                    normalDialog.setTitle("提示");
                    normalDialog.setMessage("你确定要删除吗?");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    showDialog(v, position);

                                }
                            });
                    normalDialog.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    // 显示
                    normalDialog.show();

                }
                if (data.get(position).getAmount() > 1) {
                    final int count = data.get(position).getAmount() - 1;
                    NetWorks.putAddNumCart(data.get(position).getCbid(), count, new BaseObserver<ShopCartBean>() {
                        @Override
                        public void onHandleSuccess(ShopCartBean shopCartBean) {
                            if (mOnEditClickListener != null) {
                                mOnEditClickListener.onEditClick(position, data.get(position).getCid(), count);
                            }

                            data.get(position).setAmount(count);
                            notifyDataSetChanged();
                        }
                    });
                }

            }
        });

        holder.ivShopCartgoodAdd.setOnClickListener(new View.OnClickListener() {

            int count = data.get(position).getAmount() + 1;

            @Override
            public void onClick(View v) {

                NetWorks.putAddNumCart(data.get(position).getCbid(), count, new BaseObserver<ShopCartBean>() {
                    @Override
                    public void onHandleSuccess(ShopCartBean shopCartBean) {
                        if (mOnEditClickListener != null) {
                            mOnEditClickListener.onEditClick(position, data.get(position).getCid(), count);
                        }
                        data.get(position).setAmount(count);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        if (data.get(position).isSelect()) {
            holder.ivShopCartgoodSel.setImageDrawable(context.getResources().getDrawable(R.mipmap.shopcart_selected));
        } else {
            holder.ivShopCartgoodSel.setImageDrawable(context.getResources().getDrawable(R.mipmap.shopcart_unselected));
        }

        if (data.get(position).isShopSelect()) {
            holder.ivShopCartShopSel.setImageDrawable(context.getResources().getDrawable(R.mipmap.shopcart_selected));
        } else {
            holder.ivShopCartShopSel.setImageDrawable(context.getResources().getDrawable(R.mipmap.shopcart_unselected));
        }

  /*   holder.ivShopCartClothDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v,position);
            }
        });*/

        holder.ivShopCartgoodSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setSelect(!data.get(position).isSelect());
                //通过循环找出不同商铺的第一个商品的位置
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getIsFirst() == 1) {
                        //遍历去找出同一家商铺的所有商品的勾选情况
                        for (int j = 0; j < data.size(); j++) {
                            //如果是同一家商铺的商品，并且其中一个商品是未选中，那么商铺的全选勾选取消
                            if (data.get(j).getSid() == data.get(i).getSid() && !data.get(j).isSelect()) {
                                data.get(i).setShopSelect(false);
                                break;
                            } else {
                                //如果是同一家商铺的商品，并且所有商品是选中，那么商铺的选中全选勾选
                                data.get(i).setShopSelect(true);
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        holder.ivShopCartShopSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getIsFirst() == 1) {
                    data.get(position).setShopSelect(!data.get(position).isShopSelect());
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getSid() == data.get(position).getSid()) {
                            data.get(i).setSelect(data.get(position).isShopSelect());
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NetWorks.findCommodity(data.get(position).getCid(), new BaseObserver<Commodity>() {
                    @Override
                    public void onHandleSuccess(Commodity commodity) {
                        Intent intent=new Intent(context, GoodActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("commodity",commodity);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            }
        });
    }

    private void showDialog(final View view, final int position) {
        //调用删除某个规格商品的接口
        NetWorks.DeleteCart(data.get(position).getCbid(), new BaseObserver<ShopCartBean>() {
            @Override
            public void onHandleSuccess(ShopCartBean shopCartBean) {
                if (mOnDeleteClickListener != null) {
                    mOnDeleteClickListener.onDeleteClick(view, position, data.get(position).getCid());
                }
                data.remove(position);
                //重新排序，标记所有商品不同商铺第一个的商品位置
                CartActivity.isSelectFirst(data);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if (headerView != null) {
            count++;
        }
        return count;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivShopCartShopSel;
        private TextView tvShopCartShopName;
        private TextView tvShopCartgoodName;
        private TextView tvShopCartgoodPrice;
        private TextView etShopCartgoodNum;
        private TextView tvShopCartgoodColor;
        private TextView tvShopCartgoodSize;
        private ImageView ivShopCartgoodSel;
        private ImageView ivShopCartgoodMinus;
        private ImageView ivShopCartgoodAdd;
        private ImageView ivShopCartgoodPic;
        private LinearLayout llShopCartHeader;

        public MyViewHolder(View view) {
            super(view);
            llShopCartHeader = (LinearLayout) view.findViewById(R.id.ll_shopcart_header);
            ivShopCartShopSel = (ImageView) view.findViewById(iv_item_shopcart_shopselect);
            tvShopCartShopName = (TextView) view.findViewById(R.id.tv_item_shopcart_shopname);
            tvShopCartgoodName = (TextView) view.findViewById(R.id.tv_item_shopcart_clothname);
            tvShopCartgoodPrice = (TextView) view.findViewById(R.id.tv_item_shopcart_cloth_price);
            etShopCartgoodNum = (TextView) view.findViewById(R.id.et_item_shopcart_cloth_num);
            tvShopCartgoodColor = (TextView) view.findViewById(R.id.tv_item_shopcart_cloth_color);
            tvShopCartgoodSize = (TextView) view.findViewById(R.id.tv_item_shopcart_cloth_size);
            ivShopCartgoodSel = (ImageView) view.findViewById(R.id.tv_item_shopcart_clothselect);
            ivShopCartgoodMinus = (ImageView) view.findViewById(R.id.iv_item_shopcart_cloth_minus);
            ivShopCartgoodAdd = (ImageView) view.findViewById(R.id.iv_item_shopcart_cloth_add);
            ivShopCartgoodPic = (ImageView) view.findViewById(R.id.iv_item_shopcart_cloth_pic);

        }
    }

    public View getHeaderView() {
        return headerView;
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(View view, int position, int cartid);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener mOnDeleteClickListener) {
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public interface OnEditClickListener {
        void onEditClick(int position, int cartid, int count);
    }

    public void setOnEditClickListener(OnEditClickListener mOnEditClickListener) {
        this.mOnEditClickListener = mOnEditClickListener;
    }

    public interface OnResfreshListener {
        void onResfresh(boolean isSelect);
    }

    public void setResfreshListener(OnResfreshListener mOnResfreshListener) {
        this.mOnResfreshListener = mOnResfreshListener;
    }

}
