package com.lovegod.newbuy.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.view.fragment.Home_Holder.HomeGoodsHolder;
import com.lovegod.newbuy.view.fragment.Home_Holder.NavigationHolder;
import com.lovegod.newbuy.view.fragment.Home_Holder.SlideHolder;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/17.
 * 主页列表适配器
 */

public class HomeAdapter extends RecyclerView.Adapter {
    private static final int PIC_TYPE=0;
    private static final int NAVIGATION_TYPE=1;
    private static final int SINGLE_GOODS=2;
    private static final int DOUBLE_GOODS=3;
    private Context mContext;
    private List<Commodity>commodityList=new ArrayList<>();
    private int[] images;

    public HomeAdapter(Context context,List<Commodity>commodities,int[] images){
        mContext=context;
        commodityList=commodities;
        this.images=images;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case PIC_TYPE:
                view= LayoutInflater.from(mContext).inflate(R.layout.home_pic_layout,parent,false);
                SlideHolder slideHolder=new SlideHolder(view);
                return slideHolder;
            case NAVIGATION_TYPE:
                view=LayoutInflater.from(mContext).inflate(R.layout.home_navigation_layout,parent,false);
                NavigationHolder navigationHolder=new NavigationHolder(view);
                return navigationHolder;
            case SINGLE_GOODS:
            case DOUBLE_GOODS:
                view=LayoutInflater.from(mContext).inflate(R.layout.home_goods_layout,parent,false);
                HomeGoodsHolder homeGoodsHolder=new HomeGoodsHolder(view);
                return homeGoodsHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case PIC_TYPE:
                Log.d("init","initSlider");
                SlideHolder slideHolder= (SlideHolder) holder;
                initSlider(slideHolder.mSliderLayout,slideHolder.indicator);
                break;
            case NAVIGATION_TYPE:
                NavigationHolder navigationHolder= (NavigationHolder) holder;
                NavigationAdapter navigationAdapter=new NavigationAdapter(mContext,images);
                GridLayoutManager manager=new GridLayoutManager(mContext,4);
                navigationHolder.controlScrollRecyclerView.setCanScroll(false);
                navigationHolder.controlScrollRecyclerView.setAdapter(navigationAdapter);
                navigationHolder.controlScrollRecyclerView.setLayoutManager(manager);
                break;
            case SINGLE_GOODS:
            case DOUBLE_GOODS:
                final Commodity commodity=commodityList.get(position-2);
                HomeGoodsHolder homeGoodsHolder= (HomeGoodsHolder) holder;
                Glide.with(mContext).load(commodity.getLogo()).into(homeGoodsHolder.pic);
                homeGoodsHolder.name.setText(commodity.getProductname());
                homeGoodsHolder.price.setText("￥"+commodity.getPrice());
                homeGoodsHolder.sale.setText(commodity.getSalesvolu()+"人已买");
                homeGoodsHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, GoodActivity.class);
                        intent.putExtra("commodity",commodity);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return commodityList.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return PIC_TYPE;
        }else if(position==1){
            return NAVIGATION_TYPE;
        }else if(position>=2&&position<=4){
            return SINGLE_GOODS;
        }else {
            return DOUBLE_GOODS;
        }
    }

    /**
     * 设置轮播图
     * @param mSliderLayout
     * @param indicator
     */
    private void initSlider(SliderLayout mSliderLayout, PagerIndicator indicator) {
        mSliderLayout.removeAllSliders();
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502106197328&di=53f4cc298f1cef374cd6315bc3ee469f&imgtype=0&src=http%3A%2F%2Fpic2.ooopic.com%2F10%2F73%2F38%2F90b1OOOPIC9d.jpg");
        imageUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502106231949&di=c821f7c7371b60a71c28b58d2aee3a05&imgtype=0&src=http%3A%2F%2Fpic2.ooopic.com%2F11%2F95%2F09%2F16bOOOPIC6b_1024.jpg");
        imageUrls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3922813695,1834018485&fm=26&gp=0.jpg");
        for (int i = 0; i < imageUrls.size(); i++) {
            //新建三个展示View，并且添加到SliderLayout
            TextSliderView tsv = new TextSliderView(mContext);
            tsv.image(imageUrls.get(i));
            final int finalI = i;
            tsv.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(mContext, "图", Toast.LENGTH_SHORT).show();
                }
            });
            mSliderLayout.addSlider(tsv);
        }
        //对SliderLayout进行一些自定义的配置
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSliderLayout.setDuration(3000);
        mSliderLayout.setCustomIndicator(indicator);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type){
                        case PIC_TYPE:
                        case NAVIGATION_TYPE:
                        case SINGLE_GOODS:
                            return 6;
                        case DOUBLE_GOODS:
                            return 3;
                        default:
                            return 3;
                    }
                }
            });
        }
    }
}
