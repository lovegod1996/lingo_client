package com.lovegod.newbuy.view.myinfo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.util.List;

/**
 * 收货地址列表的适配器
 * Created by ywx on 2017/7/19.
 */

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder>{
    private List<Address>addressList;
    private Context mContext;
    private ShopCartAdapter.OnItemClickListener onItemClickListener;
    private OnDeleteClick onDeleteClickListener;
    //最大滑动距离
    private int maxWidth;
    private ValueAnimator smoothAnimator;
    private float x;
    //是否该阻止父容器拦截的标志
    private boolean isViewTouch=false;

    public MyAddressAdapter(Context context, List<Address>list){
        addressList=list;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.my_address_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Address address=addressList.get(position);
        //每次通知数据改变都将item复位
        holder.layout.scrollTo(0,0);
//        if(address.isUnfold()){
//            if(holder.layout.getScrollX()<=0) {
//                Toast.makeText(mContext,"unfold"+position+" "+holder.layout.getScrollX(),Toast.LENGTH_SHORT).show();
//                smoothAnimator = ObjectAnimator.ofInt(0, maxWidth);
//                smoothAnimator.setDuration(300);
//                smoothAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int value = (int) animation.getAnimatedValue();
//                        holder.layout.scrollTo(value, 0);
//                    }
//                });
//                smoothAnimator.start();
//            }
//        }else if(!address.isUnfold()){
//            if(holder.layout.getScrollX()>=maxWidth) {
//                Toast.makeText(mContext,"fold"+position+" "+holder.layout.getScrollX(),Toast.LENGTH_SHORT).show();
//                smoothAnimator = ObjectAnimator.ofInt(maxWidth, 0);
//                smoothAnimator.setDuration(300);
//                smoothAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int value = (int) animation.getAnimatedValue();
//                        holder.layout.scrollTo(value, 0);
//                    }
//                });
//                smoothAnimator.start();
//            }
//        }
        holder.name.setText(address.getName());
        StringBuilder hiddenPhone=new StringBuilder(address.getPhone());
        holder.phone.setText(hiddenPhone.replace(3,7,"****"));
        if(address.getStatue()==0) {
            holder.address.setText(address.getAddress().replace(";", " "));
        }else {
            SpannableStringBuilder defaultAddressText = new SpannableStringBuilder("[默认]");
            defaultAddressText.append(address.getAddress().replace(";", " "));
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FD6861"));
            defaultAddressText.setSpan(colorSpan,0,4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.address.setText(defaultAddressText);
        }
        /**
         * 设置回调处理条目点击
         */
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder.editButton,position);
                }
            }
        });

        /**
         * 设置回调处理删除按钮的点击事件
         */
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteClickListener!=null){
                    onDeleteClickListener.onDelete(position);
                }
            }
        });


        final LinearLayout layout=holder.layout;
        /**
         * 处理每个条目的滑动事件
         * @param v
         * @param event
         * @return
         */
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //位移的距离
                        final float moveX = event.getX() - x;
                        //如果移动距离大于4就置为true
                        if(Math.abs(moveX)>4&&!isViewTouch){
                            isViewTouch=true;
                        }
                        //根据标志判断是否请求父容器不拦截滑动事件
                        if(isViewTouch){
                            layout.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        //允许向右滑并不小于最小滑动距离以及向左滑并不大于最大滑动距离
                        if ((layout.getScrollX() > 0 && moveX>0)||(moveX<0&&layout.getScrollX()<maxWidth)) {
                            //进行滑动
                            layout.scrollBy(-(int) moveX, 0);
                        }
                        x = event.getX();
                        break;
                    //手指抬起时
                    case MotionEvent.ACTION_UP:
                        //如果此时布局小于最小距离或者小于最大距离的四分之一，就复原
                        if(layout.getScrollX()<0||layout.getScrollX()<maxWidth/4){
                            smoothAnimator= ObjectAnimator.ofInt(layout.getScrollX(),0);
                            smoothAnimator.setDuration(300);
                            smoothAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int value= (int) animation.getAnimatedValue();
                                    layout.scrollTo(value,0);
                                }
                            });
                            smoothAnimator.start();
                        }
                        //如果此时布局大于最大距离或者大于最大距离的四分之一，就全部展开
                        else if(layout.getScrollX()>=maxWidth/4||layout.getScrollX()>=maxWidth){
                            smoothAnimator= ObjectAnimator.ofInt(layout.getScrollX(),maxWidth);
                            smoothAnimator.setDuration(300);
                            smoothAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int value= (int) animation.getAnimatedValue();
                                    layout.scrollTo(value,0);
                                }
                            });
                            smoothAnimator.start();
                        }
                        isViewTouch=false;
                        break;
                    //如果事件还是被父容器拦截掉了，就用默认的处理，跟手指抬起处理一样，但是由于滑动途中被拦截是不触发手指抬起的事件的，就用这里处理
                    default:
                        if(layout.getScrollX()<0||layout.getScrollX()<maxWidth/4){
                            smoothAnimator= ObjectAnimator.ofInt(layout.getScrollX(),0);
                            smoothAnimator.setDuration(300);
                            smoothAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int value= (int) animation.getAnimatedValue();
                                    layout.scrollTo(value,0);
                                }
                            });
                            smoothAnimator.start();
                        }else if(layout.getScrollX()>=maxWidth/4||layout.getScrollX()>maxWidth){
                            smoothAnimator= ObjectAnimator.ofInt(layout.getScrollX(),maxWidth);
                            smoothAnimator.setDuration(300);
                            smoothAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int value= (int) animation.getAnimatedValue();
                                    layout.scrollTo(value,0);
                                }
                            });
                            smoothAnimator.start();
                        }
                        isViewTouch=false;
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ViewTreeObserver.OnGlobalLayoutListener{
        TextView name,phone,address;
        Button deleteButton;
        LinearLayout layout;
        Button editButton;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.my_address_name_item);
            phone=(TextView)itemView.findViewById(R.id.my_address_phone_item);
            address=(TextView)itemView.findViewById(R.id.my_address_address_item);
            deleteButton=(Button)itemView.findViewById(R.id.my_address_delete_item);
            layout=(LinearLayout)itemView.findViewById(R.id.my_address_item_layout);
            editButton=(Button) itemView.findViewById(R.id.my_address_edit_button);
            deleteButton.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        /**
         * 用来获取控件的宽和高
         */
        @Override
        public void onGlobalLayout() {
            maxWidth=deleteButton.getWidth();
            deleteButton.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        public void recovery() {
            if (layout.getScrollX() >= maxWidth) {
                smoothAnimator = ObjectAnimator.ofInt(layout.getScrollX(), 0);
                smoothAnimator.setDuration(300);
                smoothAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        layout.scrollTo(value, 0);
                    }
                });
                smoothAnimator.start();
            }
        }
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnDeleteClick{
        void onDelete(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClick onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
