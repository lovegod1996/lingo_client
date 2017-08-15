package com.lovegod.newbuy.view.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.utils.view.AdapterWrapper;
import com.lovegod.newbuy.view.Shop2Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.myview.SearchLayout;
import com.lovegod.newbuy.view.sorts.CateGoodsAdapter;
import com.lovegod.newbuy.view.sorts.CategoryGoodsActivity;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private static final int SHOP_OPTION=2;
    private static final int GOODS_OPTION=3;
    private int searchOption;
    private int currentPage=0;
    private FloatingActionButton backToUpButton;
    private PullLoadMoreRecyclerView searchResultRecycler;
    private SearchLayout searchLayout;
    private Toolbar toolbar;
    private CateGoodsAdapter goodsAdapter;
    private ShopItemAdapter shopAdapter;
    private String searchContent="";
    private List<Commodity> commodityList=new ArrayList<>();
    private List<Shop>shopList=new ArrayList<>();
    private AdapterWrapper goodsWrapper,shopWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        toolbar=(Toolbar)findViewById(R.id.search_result_toolbar);
        searchLayout=(SearchLayout)findViewById(R.id.search_result_searchlayout);
        backToUpButton=(FloatingActionButton)findViewById(R.id.search_result_back_to_up_button);
        searchResultRecycler=(PullLoadMoreRecyclerView)findViewById(R.id.search_result_recyclerview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //设置线性布局
        searchResultRecycler.setLinearLayout();
        //禁用下拉刷新
        searchResultRecycler.setPullRefreshEnable(false);
        //获取搜索页面传过来的信息
        Intent intent=getIntent();
        //获取查询页面传递过来的内容给搜索框
        searchContent=intent.getStringExtra("searchcontent");
        searchLayout.setSearchHint(searchContent);
        //获取查询页面传递过来的选项
        searchOption=intent.getIntExtra("searchoption",1);

        RelativeLayout emptyLayout= (RelativeLayout) LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.no_data_layout,null);
        //包装商品适配器
        goodsAdapter = new CateGoodsAdapter(this, commodityList);
        goodsWrapper=new AdapterWrapper(this,goodsAdapter,emptyLayout);
        //包装商店适配器
        shopAdapter=new ShopItemAdapter(this,shopList);
        shopWrapper=new AdapterWrapper(this,shopAdapter,emptyLayout);

        /*先查询当前页面，第一次是第一页，对应page=0
        先判断是要查询商品还是店铺
         */
        if(searchOption==GOODS_OPTION) {
            searchGoodsOnPage(currentPage);
        }else if(searchOption==SHOP_OPTION){
            searchShopOnPage(currentPage);
        }

        /**
         * 设置下拉刷新和下拉加载监听（这里只需要上拉加载）
         */
        searchResultRecycler.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                //加载下一页
                if(searchOption==GOODS_OPTION) {
                    searchGoodsOnPage(currentPage + 1);
                }else if(searchOption==SHOP_OPTION){
                    searchShopOnPage(currentPage + 1);
                }
            }
        });

        /**
         * toolbar上的搜索框的点击监听,把当前搜索框里的内容通过Intent启动传递给搜索页面
         */
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("searchcontent_return",searchLayout.getSearchHint());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        /**
         * 返回按钮的监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("searchcontent_return",searchLayout.getSearchHint());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        /**
         *  返回最上面按钮监听
         */
        backToUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResultRecycler.scrollToTop();
            }
        });
    }

    /**
     * 查询具体页数的商品
     * @param page 具体的页数
     */
    private void searchGoodsOnPage(int page){
        //判断是否是第一页，是的话就需要显示加载框
        if(page==0) {
            searchResultRecycler.setAdapter(goodsWrapper);
            NetWorks.getNameGoods(searchContent, page, new BaseObserver<List<Commodity>>(this, new ProgressDialog(this)) {
                @Override
                public void onHandleSuccess(List<Commodity> commodities) {
                    for(Commodity commodity:commodities){
                        commodityList.add(commodity);
                    }
                    goodsWrapper.notifyDataSetChanged();
                    //绑定每一项布局点击监听
                    goodsAdapter.setItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(SearchResultActivity.this, GoodActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("commodity", commodityList.get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                }

                @Override
                public void onHandleError(List<Commodity> commodities) {
                }
            });
        }
        //否则不显示加载框
        else {
            NetWorks.getNameGoods(searchContent, page, new BaseObserver<List<Commodity>>(this) {
                @Override
                public void onHandleSuccess(List<Commodity> commodities) {
                    //页面加一
                    currentPage++;
                    //从下一页加载的数据加到原先list上
                    for(int i=0;i<commodities.size();i++){
                        commodityList.add(commodities.get(i));
                    }
                    goodsWrapper.notifyDataSetChanged();

                    //关闭上拉加载提示
                    searchResultRecycler.setPullLoadMoreCompleted();
                }

                @Override
                public void onHandleError(List<Commodity> commodities) {
                    Toast.makeText(SearchResultActivity.this, "已经到底啦", Toast.LENGTH_SHORT).show();
                    searchResultRecycler.setPullLoadMoreCompleted();
                }
            });
        }

    }

    /**
     * 查询具体页数的店铺
     * @param page 页数
     */
    private void searchShopOnPage(int page) {
        //第一次加载加载框
        if (page == 0) {
            searchResultRecycler.setAdapter(shopWrapper);
            NetWorks.getNameShop(searchContent, page, new BaseObserver<List<Shop>>(this,new ProgressDialog(this)) {
                @Override
                public void onHandleSuccess(List<Shop> list) {
                    for(Shop shop:list){
                        shopList.add(shop);
                    }
                    shopWrapper.notifyDataSetChanged();
                    shopAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(SearchResultActivity.this, Shop2Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shopList.get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                }

                @Override
                public void onHandleError(List<Shop> list) {
                }
            });
        }else {
            NetWorks.getNameShop(searchContent, page, new BaseObserver<List<Shop>>(this) {
                @Override
                public void onHandleSuccess(List<Shop> list) {
                    currentPage++;

                    for(int i=0;i<list.size();i++){
                        shopList.add(list.get(i));
                    }
                    shopWrapper.notifyDataSetChanged();
                    //关闭上拉加载提示
                    searchResultRecycler.setPullLoadMoreCompleted();
                }

                @Override
                public void onHandleError(List<Shop> list) {
                    Toast.makeText(SearchResultActivity.this, "已经到底啦", Toast.LENGTH_SHORT).show();
                    searchResultRecycler.setPullLoadMoreCompleted();
                }
            });
        }
    }

    /**
     * 设置系统返回键功能与点击搜索框和自定义返回键相同
     */
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("searchcontent_return",searchLayout.getSearchHint());
        setResult(RESULT_OK,intent);
        finish();
    }
}
