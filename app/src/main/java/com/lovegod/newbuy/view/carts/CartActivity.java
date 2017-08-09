package com.lovegod.newbuy.view.carts;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.ShopCartBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * *****************************************
 * Created by thinking on 2017/4/23.
 * 创建时间：
 * <p>
 * 描述：购物车
 * 购物车活动，在商品页面跳转到点击购物车跳转到此活动而不跳转到首页fragment的购物车页面
 * *******************************************
 */

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvShopCartSubmit;
    private TextView tvShopCartSelect;
    private TextView tvShopCartTotalNum;
    private View mEmtryView;
    private TextView tv_item_shopcart_shopname;
    private LinearLayout linearlayout_child_cart;
    private RecyclerView rlvShopCart;
    private ShopCartAdapter mShopCartAdapter;
    private LinearLayout llPay;
    private RelativeLayout rlHaveProduct;

    private TextView cartname;
    private TextView cartnum;

    //购物车列表数组
    private List<ShopCartBean> mcartlist=new ArrayList<>();
    //选中的列表数组
    private List<ShopCartBean> mcartlist2=new ArrayList<>();
    private List<String> mHotProductsList = new ArrayList<>();
    private TextView tvShopCartTotalPrice;
    private int mCount,mPosition;
    private float mTotalPrice1;
    private boolean mSelect;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartname = (TextView) findViewById(R.id.cartname);
        cartnum = (TextView) findViewById(R.id.cartnum);
        cartname.setText("购物车");

        toolbar=(Toolbar)findViewById(R.id.shop_cart_toolbar);
        tvShopCartSubmit=(TextView)findViewById(R.id.tv_shopcart_submit);
        tv_item_shopcart_shopname=(TextView) findViewById(R.id.tv_item_shopcart_shopname);
        linearlayout_child_cart=(LinearLayout) findViewById(R.id.linearlayout_child_cart);

        tvShopCartSelect = (TextView) findViewById(R.id.tv_shopcart_addselect);
        tvShopCartTotalPrice = (TextView) findViewById(R.id.tv_shopcart_totalprice);
        tvShopCartTotalNum = (TextView) findViewById(R.id.tv_shopcart_totalnum);

        rlHaveProduct = (RelativeLayout) findViewById(R.id.rl_shopcart_have);
        rlvShopCart = (RecyclerView) findViewById(R.id.rlv_shopcart);
        mEmtryView = (View) findViewById(R.id.emtryview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mEmtryView.setVisibility(View.GONE);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        llPay.setLayoutParams(lp);
        tvShopCartSubmit = (TextView) findViewById(R.id.tv_shopcart_submit);
        rlvShopCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        mShopCartAdapter = new ShopCartAdapter(CartActivity.this,mcartlist);
        rlvShopCart.setAdapter(mShopCartAdapter);

        //设置返回按钮
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        /**
         * 设置返回按钮点击监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 去结算点击监听
         */
        tvShopCartSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mcartlist2.size()==0){
                    Toast.makeText(CartActivity.this,"未选中任何商品",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(CartActivity.this,SubmitOrderActivity.class);
                intent.putExtra("buy_goods", (Serializable) mcartlist2);
                startActivity(intent);
            }
        });

        /**
         * 返回按钮监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    public static void isSelectFirst(List<ShopCartBean> list){
        if(list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getSid() == list.get(i - 1).getSid()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }
    }

    /**
     * 对购买的物品列表进行排序，让同一商店的商品在一起
     */
    class ShopCompartor implements Comparator<ShopCartBean>{

        @Override
        public int compare(ShopCartBean o1, ShopCartBean o2) {
            return o1.getSid()-o2.getSid();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCartInfo();
    }

    /**
     * 请求购物车信息
     */
    private void requestCartInfo(){
        mcartlist2.clear();
        mcartlist.clear();
        NetWorks.getCarts(1, new BaseObserver<List<ShopCartBean>>() {
            @Override
            public void onHandleSuccess(List<ShopCartBean> shopCartBeen) {
//                if(shopCartBeen.size()==0){
//                    mcartlist.clear();
//                    mShopCartAdapter.notifyDataSetChanged();
//                    tvShopCartTotalPrice.setText("总价：" + 0.0);
//                    tvShopCartTotalNum.setText("共" + 0+ "件商品");
//                    return;
//                }
                for(ShopCartBean shopCartBean:shopCartBeen){
                    mcartlist.add(shopCartBean);
                }
                Collections.sort(mcartlist,new ShopCompartor());
                cartnum.setText("("+mcartlist.size()+")");
                //删除商品接口
                mShopCartAdapter.setOnDeleteClickListener(new ShopCartAdapter.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(View view, int position,int cartid) {
                        mShopCartAdapter.notifyDataSetChanged();
                        int num=mcartlist.size()-1;
                        cartnum.setText("("+num+")");
                        if (num==0){
                            mTotalPrice1 =0;
                            tvShopCartTotalPrice.setText("总价：" + 0.0);
                        }
                    }
                });
                //修改数量接口
                mShopCartAdapter.setOnEditClickListener(new ShopCartAdapter.OnEditClickListener() {
                    @Override
                    public void onEditClick(int position, int cartid, int count) {
                        mCount = count;
                        mPosition = position;
                    }
                });
                //实时监控全选按钮
                mShopCartAdapter.setResfreshListener(new ShopCartAdapter.OnResfreshListener() {
                    @Override
                    public void onResfresh( boolean isSelect) {
                        mSelect = isSelect;
                        if(isSelect){
                            Drawable left = getResources().getDrawable(R.mipmap.shopcart_selected);
                            tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                        }else {
                            Drawable left = getResources().getDrawable(R.mipmap.shopcart_unselected);
                            tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                        }
                        float mTotalPrice = 0;
                        int mTotalNum = 0;
                        mTotalPrice1 = 0;
                        mcartlist2.clear();
                        for(int i = 0;i < mcartlist.size(); i++)
                            if(mcartlist.get(i).isSelect()) {
                                //    mTotalPrice += Float.parseFloat(mcartlist.get(i).getCommodity_select()) * mcartlist.get(i).getAmount();
                                mTotalPrice += mcartlist.get(i).getPrice() * mcartlist.get(i).getAmount();
                                mTotalNum += mcartlist.get(i).getAmount();
                                mcartlist2.add(mcartlist.get(i));
                            }

                   /* if(mAllOrderList.get(i).getIsSelect()) {
                        mTotalPrice += Float.parseFloat(mAllOrderList.get(i).getPrice()) * mAllOrderList.get(i).getCount();
                        mTotalNum += 1;
                        mGoPayList.add(mAllOrderList.get(i));
                    }*/

                        mTotalPrice1 = mTotalPrice;
                        tvShopCartTotalPrice.setText("总价：" + mTotalPrice);
                        tvShopCartTotalNum.setText("共" + mTotalNum + "件商品");
                    }
                });

                //全选
                tvShopCartSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelect = !mSelect;
                        if(mSelect){
                            Drawable left = getResources().getDrawable(R.mipmap.shopcart_selected);
                            tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                            for(int i = 0;i < mcartlist.size();i++){
                                mcartlist.get(i).setSelect(true);
                                mcartlist.get(i).setShopSelect(true);
                            }
                        }else{
                            Drawable left = getResources().getDrawable(R.mipmap.shopcart_unselected);
                            tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                            for(int i = 0;i < mcartlist.size();i++){
                                mcartlist.get(i).setSelect(false);
                                mcartlist.get(i).setShopSelect(false);
                            }
                        }
                        mShopCartAdapter.notifyDataSetChanged();

                    }
                });


                isSelectFirst(mcartlist);
                mShopCartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<ShopCartBean> shopCartBeen) {

            }
        });

    }
}
