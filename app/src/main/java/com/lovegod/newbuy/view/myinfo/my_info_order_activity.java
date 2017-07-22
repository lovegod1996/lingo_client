package com.lovegod.newbuy.view.myinfo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lovegod.newbuy.R;

import java.util.ArrayList;
import java.util.List;

public class my_info_order_activity extends AppCompatActivity {
    //全部
    private static final int ALL_FLAG=1;
    //待付款
    private static final int FOR_THE_PAY_FLAG=2;
    //待发货
    private static final int FOR_THE_GOODS_FLAG=3;
    //待收货
    private static final int TO_THE_GOODS_FLAG=4;
    //待评价
    private static final int TO_THE_ASSESS_FLAG=5;
    private List<Fragment>fragmentList=new ArrayList<>();
    private List<String>titleList=new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private OrderInfoFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_order_activity);
        viewPager=(ViewPager)findViewById(R.id.my_info_order_viewpager);
        tabLayout=(TabLayout)findViewById(R.id.my_info_order_tablayout);
        init();
    }

    /**
     * 初始化viewpager和tablayout相关的东西
     */
    private void init(){
        fragmentList.add(OrderFragment.newInstance(ALL_FLAG));
        fragmentList.add(OrderFragment.newInstance(FOR_THE_PAY_FLAG));
        fragmentList.add(OrderFragment.newInstance(FOR_THE_GOODS_FLAG));
        fragmentList.add(OrderFragment.newInstance(TO_THE_GOODS_FLAG));
        fragmentList.add(OrderFragment.newInstance(TO_THE_ASSESS_FLAG));
        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待发货");
        titleList.add("待收货");
        titleList.add("待评价");
        adapter=new OrderInfoFragmentAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
