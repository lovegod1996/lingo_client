package com.lovegod.newbuy.view.myinfo;

import android.app.ProgressDialog;
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
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.view.AdapterWrapper;

import java.util.ArrayList;
import java.util.List;

public class AddAssessActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<Order.OrderGoods>orderGoodsList=new ArrayList<>();
    private AddAssessAdapter adapter;
    private User user;
    private int currentPage;
    private AdapterWrapper wrapper;
    private boolean isFirstLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assess);
        user= (User) SpUtils.getObject(this,"userinfo");
        toolbar=(Toolbar)findViewById(R.id.add_assess_toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.add_assess_recyclerview);
        setSupportActionBar(toolbar);


        isFirstLoad=true;
        adapter=new AddAssessAdapter(this,orderGoodsList);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        //初始化列表
        recyclerView.setLayoutManager(manager);
        RelativeLayout emptyLayout= (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.no_data_layout,null);
        wrapper=new AdapterWrapper(this,adapter,emptyLayout);
        recyclerView.setAdapter(wrapper);

        //上拉加载监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isFirstLoad) {
                    if (recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent() >= recyclerView.computeVerticalScrollRange()) {
                        requestGoods();
                    }
                }else {
                    isFirstLoad=false;
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 请求未评价商品数据
     */
    private void requestGoods() {
        NetWorks.getNoAssessGoods(user.getUid(), currentPage, new BaseObserver<List<Order.OrderGoods>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Order.OrderGoods> orderGoodses) {
                currentPage++;
                for(Order.OrderGoods goods:orderGoodses){
                    orderGoodsList.add(goods);
                }
                wrapper.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Order.OrderGoods> orderGoodses) {
                Toast.makeText(AddAssessActivity.this,"没有更多啦~",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 首次或者当活动返回到用户可见状态都重新从第一页请求数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        orderGoodsList.clear();
        currentPage=0;
        requestGoods();
    }
}
