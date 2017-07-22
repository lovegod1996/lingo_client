package com.lovegod.newbuy.view.search;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.utils.system.SpUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_CODE=1;
    private static final int SHOP_OPTION=2;
    private static final int GOODS_OPTION=3;
    private int searchOption=GOODS_OPTION;
    private Button searchOptionButton;
    private PopupWindow selectOptionWindow;
    private TextView searchButton;
    private ImageView deleteButton;
    private ImageButton backButton;
    private LinearLayout recommendLayout;
    private ControlScrollRecyclerView firstRecycler,secondRecycler;
    private MatchListAdapter adapter;
    private SearchView searchView;
    private ListView listView;
    private List<String> searchList=new ArrayList<>();
    private List<String> recommendList=new ArrayList<>();
    private List<String> historyList=new ArrayList<>();
    RecommendAdapter firstAdapter,secondAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);
        //第一次启动获取历史记录
        historyList=SpUtils.getStringArray(this,"historylist");

        searchOptionButton=(Button)findViewById(R.id.search_option_button);
        searchButton=(TextView) findViewById(R.id.search_search_button);
        deleteButton=(ImageView)findViewById(R.id.search_delete_button);
        backButton=(ImageButton)findViewById(R.id.search_back_button);
        recommendLayout=(LinearLayout)findViewById(R.id.search_recommend_layout);
        searchView=(SearchView)findViewById(R.id.search_activity_searchview);
        listView=(ListView)findViewById(R.id.search_activity_listview);
        firstRecycler=(ControlScrollRecyclerView)findViewById(R.id.search_recycler_first);
        secondRecycler=(ControlScrollRecyclerView)findViewById(R.id.search_recycler_second);

        //设置两个标签列表不滚动
        firstRecycler.setCanScroll(false);
        secondRecycler.setCanScroll(false);

        //第一个弹性布局管理，设置给Recycler
        FlexboxLayoutManager fisrtlayoutManager=new FlexboxLayoutManager(this);
        fisrtlayoutManager.setFlexWrap(FlexWrap.WRAP);
        fisrtlayoutManager.setFlexDirection(FlexDirection.ROW);
        fisrtlayoutManager.setAlignItems(AlignItems.STRETCH);
        fisrtlayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        firstRecycler.setLayoutManager(fisrtlayoutManager);

        //第二个弹性布局，两个Recycler不能用一个Manager不然会报错
        FlexboxLayoutManager secondlayoutManager=new FlexboxLayoutManager(this);
        secondlayoutManager.setFlexWrap(FlexWrap.WRAP);
        secondlayoutManager.setFlexDirection(FlexDirection.ROW);
        secondlayoutManager.setAlignItems(AlignItems.STRETCH);
        secondlayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        secondRecycler.setLayoutManager(secondlayoutManager);

        //初始化SearchView
        initSearchView();

        //初始化各个适配器数组
        initList();

        //第一个RecyclerView的适配器初始化
        firstAdapter=new RecommendAdapter(this,recommendList);
        //第二个RecyclerView的适配器初始化
        secondAdapter=new RecommendAdapter(this,historyList);
        firstRecycler.setAdapter(firstAdapter);
        secondRecycler.setAdapter(secondAdapter);

        //searchView的Listview适配器初始化
        adapter=new MatchListAdapter(this,searchList);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView=(TextView)view.findViewById(R.id.match_item_text);
                onQuery(textView.getText().toString());
            }
        });

        //searchView的监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            /**
             * 用户点击搜索按钮时执行的操作
             */
            public boolean onQueryTextSubmit(String query) {
                //执行搜索操作
                onQuery(query);
                return false;
            }

            @Override
            /**
             * 输入框里内容发生变动时的操作
             */
            public boolean onQueryTextChange(String newText) {
                //如果输入框为空，显示搜索标签页，隐藏搜索匹配列表
                if(TextUtils.isEmpty(newText)){
                    listView.clearTextFilter();
                    recommendLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }else {
                    //如果不为空，隐藏标签，显示匹配列表
                    recommendLayout.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        /**
         * 返回按钮的监听
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 清空历史记录的按钮监听
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyList.clear();
                secondAdapter.notifyDataSetChanged();
                SpUtils.removeStringArray(SearchActivity.this,"historylist");
            }
        });

        /**
         * 右上角搜索按钮监听
         */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuery(searchView.getQuery().toString());
            }
        });

        /**
         * 搜索选项监听
         */
        searchOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionWindow();
            }
        });

        /**
         * 第一个列表实现item监听，itemText是被点击条目里面的字符串
         */
        firstAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String itemText) {
                onQuery(itemText);
            }
        });

        /**
         * 第二个列表实现item监听，itemText是被点击条目里面的字符串,历史记录条目直接跳转不需要添加
         */
        secondAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String itemText) {
                Intent intent=new Intent(SearchActivity.this,SearchResultActivity.class);
                //传递查询内容
                intent.putExtra("searchcontent",itemText);
                //传递查询选项（店铺或者商品)
                intent.putExtra("searchoption",searchOption);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }


    /**
     * 初始化匹配列表，推荐列表，历史记录列表
     */
    private void initList() {
        searchList.add("小米");
        searchList.add("电视 曲面");
        searchList.add("电脑");
        searchList.add("小米电视");
        searchList.add("三星");
        recommendList.add("小米手机6");
        recommendList.add("小米");
        recommendList.add("空调");
        recommendList.add("格力空调");
        recommendList.add("曲面电视");
        recommendList.add("笔记本电脑");
        recommendList.add("全面屏手机");
        recommendList.add("3D打印机");
        recommendList.add("3D电视");
        recommendList.add("变频空调");
    }

    /**
     * 配置searchView的各个参数
     */
    public void initSearchView(){
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        //获取searchview内部的输入控件
        SearchView.SearchAutoComplete textView= (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        //设置字体大小
        textView.setTextSize(14);
    }

    /**
     * 当此活动不再与用户交互，保存历史记录
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(historyList.size()==0){
            SpUtils.removeStringArray(this,"historylist");
        }else {
            SpUtils.putStringArray(this, "historylist", historyList);
        }
    }

    /**
     * 执行查询操作
     */
    private void onQuery(final String query){
        //如果去掉空格后不为空
        if(!TextUtils.isEmpty(query.trim())) {
            //如果内容不重复,就加入历史标签
            if (!historyList.contains(query)) {
                //添加进入列表
                historyList.add(query);
            }
            //将搜索内容传给搜索结果页面
            Intent intent=new Intent(SearchActivity.this,SearchResultActivity.class);
            //传递查询内容
            intent.putExtra("searchcontent",query);
            //传递查询选项（店铺或者商品)
            intent.putExtra("searchoption",searchOption);
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    /**
     * 显示选项页
     */
    private void showOptionWindow() {
        //加载popupwindow布局
        View contentView= LayoutInflater.from(this).inflate(R.layout.select_option_layout,null);
        selectOptionWindow=new PopupWindow(contentView);
        //需要指定长宽才可以显示popupwindow
        selectOptionWindow.setWidth(CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        selectOptionWindow.setHeight(CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout shopOption=(LinearLayout)contentView.findViewById(R.id.pop_shop_option);
        LinearLayout goodsOption=(LinearLayout)contentView.findViewById(R.id.pop_goods_option);
        shopOption.setOnClickListener(this);
        goodsOption.setOnClickListener(this);

        //下面两步用来设置点击popupwindow以外的区域消失
        selectOptionWindow.setBackgroundDrawable(new BitmapDrawable());
        selectOptionWindow.setOutsideTouchable(true);

        //设置弹出消失动画
        selectOptionWindow.setAnimationStyle(R.style.popWindowStyle);

        //设置出现位置在searchOptionButton下面，并向左偏移了15
        selectOptionWindow.showAsDropDown(searchOptionButton,-15,0);
    }

    /**
     *  搜索选项页面的item点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_goods_option:
                searchOptionButton.setText("商品");
                searchOption=GOODS_OPTION;
                selectOptionWindow.dismiss();
                break;
            case R.id.pop_shop_option:
                searchOptionButton.setText("店铺");
                searchOption=SHOP_OPTION;
                selectOptionWindow.dismiss();
                break;
        }
    }

    /**
     * 处理返回键
     */
    @Override
    public void onBackPressed() {
        if (selectOptionWindow != null && selectOptionWindow.isShowing()) {
            selectOptionWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 处理搜索结果页返回来的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE:
                if(resultCode==RESULT_OK){
                    //获取从搜索结果页传递过来的数据设置到搜索框上
                    searchView.setQuery(data.getStringExtra("searchcontent_return"),false);
                }
                break;
            default:
        }
    }
}
