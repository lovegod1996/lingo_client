package com.lovegod.newbuy.view.myinfo.favourite;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.FavouriteGoods;
import com.lovegod.newbuy.bean.FavouriteShop;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.userPreferences.UserPreferencesUtil;
import com.lovegod.newbuy.utils.view.AdapterWrapper;
import com.lovegod.newbuy.view.TimeStickyDecoration;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private static final int GOODS_TYPE=0;
    private static final int SHOP_TYPE=1;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView editText,goodsType,shopType;
    private User user;
    private List<FavouriteGoods>goodsList=new ArrayList<>();
    private List<FavouriteShop>shopList=new ArrayList<>();
    private List<String>timeList=new ArrayList<>();
    private FavouriteGoodsAdapter goodsAdapter;
    private FavouriteShopAdapter shopAdapter;
    private int currentPage=0;
    private boolean isEdit=false;
    private int type;
    //是否第一次加载
    private boolean isFirstLoad=true;
    private AdapterWrapper goodsWrapper,shopWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_goods);
        type=getIntent().getIntExtra("type",GOODS_TYPE);
        toolbar=(Toolbar)findViewById(R.id.favourite_goods_toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.favourite_goods_recyclerview);
        goodsType=(TextView)findViewById(R.id.favourite_goods_type);
        shopType=(TextView)findViewById(R.id.favourite_shop_type);
        editText=(TextView)findViewById(R.id.favourite_goods_edit);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        goodsAdapter=new FavouriteGoodsAdapter(this,goodsList);
        shopAdapter=new FavouriteShopAdapter(this,shopList);
        RelativeLayout emptyLayout= (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.no_data_layout,null);
        goodsWrapper=new AdapterWrapper(this,goodsAdapter,emptyLayout);
        shopWrapper=new AdapterWrapper(this,shopAdapter,emptyLayout);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new TimeStickyDecoration(this,timeList,goodsWrapper));

        setSupportActionBar(toolbar);

        //初始化数据
        initShow();

        /**
         * 宝贝点击监听
         */
        goodsType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==SHOP_TYPE){
                    restoreEdit();
                    goodsType.setTextColor(Color.parseColor("#FD6861"));
                    shopType.setTextColor(Color.BLACK);
                    type=GOODS_TYPE;
                    currentPage=0;
                    goodsList.clear();
                    timeList.clear();
                    recyclerView.setAdapter(goodsWrapper);
                }
            }
        });

        /**
         * 店铺点击监听
         */
        shopType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==GOODS_TYPE){
                    restoreEdit();
                    shopType.setTextColor(Color.parseColor("#FD6861"));
                    goodsType.setTextColor(Color.BLACK);
                    currentPage=0;
                    type=SHOP_TYPE;
                    shopList.clear();
                    timeList.clear();
                    recyclerView.setAdapter(shopWrapper);
                }
            }
        });

        /**
         * 删除按钮显示动画结束和隐藏动画结束回调
         */
        goodsAdapter.setOnAnimEndListener(new FavouriteGoodsAdapter.OnAnimEndListener() {
            @Override
            public void onAnimEnd() {
                //动画结束设置编辑按钮可点击防止
                editText.setEnabled(true);
                editText.setClickable(true);
            }
        });

        /**
         * 关注商店删除监听
         */
        shopAdapter.setOnItemClickListener(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                shopWrapper.notifyDataSetChanged();
            }
        });

        /**
         * 关注商品删除按钮点击监听
         */
        goodsAdapter.setOnItemClickListener(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                goodsWrapper.notifyDataSetChanged();
                UserPreferencesUtil.changeTrackInfo(FavouriteActivity.this,user.getUid(),goodsList.get(position).getCid(),3,0);
            }
        });

        shopAdapter.setOnAnimEndListener(new FavouriteGoodsAdapter.OnAnimEndListener() {
            @Override
            public void onAnimEnd() {
                //动画结束设置编辑按钮可点击防止
                editText.setEnabled(true);
                editText.setClickable(true);
            }
        });

        /**
         * 编辑按钮的监听
         */
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==GOODS_TYPE) {
                    if (!isEdit) {
                        for (FavouriteGoods goods : goodsList) {
                            goods.setEdit(true);
                        }
                        editText.setEnabled(false);
                        editText.setClickable(false);
                        goodsWrapper.notifyDataSetChanged();
                        inEdit();
                    } else {
                        for (FavouriteGoods goods : goodsList) {
                            goods.setEdit(false);
                        }
                        editText.setEnabled(false);
                        editText.setClickable(false);
                        goodsWrapper.notifyDataSetChanged();
                        restoreEdit();
                    }
                }else if(type==SHOP_TYPE){
                    if (!isEdit) {
                        for (FavouriteShop shop : shopList) {
                            shop.setEdit(true);
                        }
                        editText.setEnabled(false);
                        editText.setClickable(false);
                        shopWrapper.notifyDataSetChanged();
                        inEdit();
                    } else {
                        for (FavouriteShop shop : shopList) {
                            shop.setEdit(false);
                        }
                        editText.setEnabled(false);
                        editText.setClickable(false);
                        shopWrapper.notifyDataSetChanged();
                        restoreEdit();
                    }
                }
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isFirstLoad) {
                    if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                        if (type == GOODS_TYPE) {
                            getFoucusGoods();
                        } else if (type == SHOP_TYPE) {
                            getFocusShop();
                        }
                    }
                }else {
                    isFirstLoad=false;
                }
            }
        });
    }


    /**
     * 初始化显示
     */
    private void initShow() {
        if(type==SHOP_TYPE){
            goodsType.setTextColor(Color.BLACK);
            shopType.setTextColor(Color.parseColor("#FD6861"));
            recyclerView.setAdapter(shopWrapper);
        }else {
            recyclerView.setAdapter(goodsWrapper);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user= (User) SpUtils.getObject(this,"userinfo");
        //如果点击的是关注商品
        if(type==GOODS_TYPE) {
            currentPage = 0;
            goodsList.clear();
            timeList.clear();
            //获取用户关注的商品列表
            if (user != null) {
                getFoucusGoods();
            }
        }
        //如果点击的是关注店铺
        else if(type==SHOP_TYPE){
            currentPage=0;
            shopList.clear();
            timeList.clear();
            if(user!=null){
                getFocusShop();
            }
        }
    }

    /**
     * 获取用户关注的商品列表
     */
    private void getFoucusGoods() {
        NetWorks.getFoucusGoods(user.getUid(), currentPage, new BaseObserver<List<FavouriteGoods>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<FavouriteGoods> list) {
                for(FavouriteGoods goods:list){
                    goods.setEdit(false);
                    goodsList.add(goods);
                    timeList.add(goods.getLooktime().substring(0,10));
            }
                goodsWrapper.notifyDataSetChanged();
                //查询成功页数加1，方便下一次查找
                currentPage++;
            }

            @Override
            public void onHandleError(List<FavouriteGoods> list) {
                Toast.makeText(FavouriteActivity.this,"没有更多啦~",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取用户关注店铺列表
     */
    private void getFocusShop(){
        NetWorks.getFocusShop(user.getUid(), currentPage, new BaseObserver<List<FavouriteShop>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<FavouriteShop> list) {
                for(FavouriteShop shop:list){
                    shop.setEdit(false);
                    shopList.add(shop);
                    timeList.add(shop.getLooktime().substring(0,10));
                }
                shopWrapper.notifyDataSetChanged();
                currentPage++;
            }

            @Override
            public void onHandleError(List<FavouriteShop> list) {

            }
        });
    }

    /**
     * 编辑按钮复位
     */
    private void restoreEdit(){
        isEdit=false;
        editText.setText("编辑");
    }

    /**
     * 进入编辑状态
     */
    private void inEdit(){
        isEdit=true;
        editText.setText("完成");
    }
}
