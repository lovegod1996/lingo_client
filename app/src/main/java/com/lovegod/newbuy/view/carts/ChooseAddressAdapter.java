package com.lovegod.newbuy.view.carts;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/20.
 */

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.ViewHolder> {
    private Context mContext;
    private List<Address>addressList=new ArrayList<>();
    private ShopCartAdapter.OnItemClickListener onItemClickListener;
    public ChooseAddressAdapter(Context context,List<Address>list){
        mContext=context;
        addressList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.submit_oreder_default_address,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Address address=addressList.get(position);
        holder.nameText.setText(address.getName());
        holder.phoneText.setText(address.getPhone());
        if(address.getStatue()==0) {
            holder.addressText.setText(address.getAddress().replace(";", "").replace(" ", ""));
        }else {
            SpannableStringBuilder builder=new SpannableStringBuilder("[默认]");
            builder.append(address.getAddress().replace(";","").replace(" ",""));
            ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor("#FD6861"));
            builder.setSpan(colorSpan,0,4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.addressText.setText(builder);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder.layout,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText,phoneText,addressText;
        RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            layout=(RelativeLayout)itemView.findViewById(R.id.submit_order_default_address_layout);
            nameText=(TextView)itemView.findViewById(R.id.submit_order_default_address_name);
            phoneText=(TextView)itemView.findViewById(R.id.submit_order_default_address_phone);
            addressText=(TextView)itemView.findViewById(R.id.submit_order_default_address_address);
        }
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
