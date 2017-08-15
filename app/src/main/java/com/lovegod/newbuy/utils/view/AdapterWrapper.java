package com.lovegod.newbuy.utils.view;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ywx on 2017/8/11.
 * recyclerview的适配器的装饰类，用于给已有的adapter添加header和footer以及总数据时显示的默认界面
 */

public class AdapterWrapper extends RecyclerView.Adapter {
    private static final int BASE_HEADER=0x186A0;
    private static final int BASE_FOOTER=0x30d40;
    private static final int EMPTY_TYPE=0x61A80;
    //空布局id默认值
    private static final int NO_VALUE=-0x186A0;
    //被装饰的adapter
    private RecyclerView.Adapter innerAdapter;
    //头部view的集合
    private SparseArrayCompat<View>headerViews=new SparseArrayCompat<>();
    //底部view的集合
    private SparseArrayCompat<View>footerViews=new SparseArrayCompat<>();
    //列表为空时显示的view
    private View emptyView;
    private Context mContext;

    public AdapterWrapper(Context context, RecyclerView.Adapter adapter, View emptyView){
        innerAdapter=adapter;
        this.emptyView=emptyView;
        mContext=context;
    }

    public AdapterWrapper(Context context, RecyclerView.Adapter adapter){
        innerAdapter=adapter;
        mContext=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==EMPTY_TYPE){
            Holder holder=new Holder(emptyView);
            ViewGroup.MarginLayoutParams params=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            emptyView.setLayoutParams(params);
            return holder;
        }
        //如果是头部的值
        if(headerViews.get(viewType)!=null){
            Holder holder=new Holder(headerViews.get(viewType));
            return holder;
        }
        //如果是底部的值
        if(footerViews.get(viewType)!=null){
            Holder holder=new Holder(footerViews.get(viewType));
            return holder;
        }
        if(viewType!=EMPTY_TYPE) {
            return innerAdapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getRealItemCount()==0){
            return;
        }
        if(isHeaderPositon(position)){
            return;
        }
        if(isFooterPosition(position)){
            return;
        }
        innerAdapter.onBindViewHolder(holder,position-getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return innerAdapter.getItemCount()>0?headerViews.size()+footerViews.size()+innerAdapter.getItemCount():1;
    }

    @Override
    public int getItemViewType(int position) {
        if(getRealItemCount()==0){
            return EMPTY_TYPE;
        }
        if(isHeaderPositon(position)){
            return headerViews.keyAt(position);
        }else if(isFooterPosition(position)){
            return footerViews.keyAt(position-getHeaderCount()-getRealItemCount());
        }
        return innerAdapter.getItemViewType(position-getHeaderCount());
    }

    /**
     * 获取除了头部和底部的列表项个数
     * @return
     */
    public int getRealItemCount(){
        return innerAdapter.getItemCount();
    }

    /**
     * 添加头部view
     * @param view
     */
    public void addHeader(View view){
        headerViews.put(BASE_HEADER+headerViews.size(),view);
    }

    /**
     * 添加底部view
     * @param view
     */
    public void addFooter(View view){
        footerViews.put(BASE_FOOTER+footerViews.size(),view);
    }

    /**
     * 添加列表为空时显示的view
     * @param view
     */
    public void addEmptyView(View view){
        emptyView=view;
    }

    /**
     * 获取头部view的个数
     * @return
     */
    public int getHeaderCount(){
        return headerViews.size();
    }

    /**
     * 获取尾部view个数
     * @return
     */
    public int getFooterCount(){
        return footerViews.size();
    }

    /**
     * 判断当前位置是不是头部的位置
     * @param position 当前位置
     * @return
     */
    public boolean isHeaderPositon(int position){
        return position<getHeaderCount();
    }

    /**
     * 判断当前位置是不是底部
     * @param position 当前位置
     * @return
     */
    public boolean isFooterPosition(int position){
        return position>=getHeaderCount()+getRealItemCount();
    }

    class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    /**
     *
     * @param position
     */
    public void wrapperNotifyItemRemoved(int position){
        innerAdapter.notifyItemRemoved(getHeaderCount()+position);
    }
}
