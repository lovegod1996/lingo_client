package com.lovegod.newbuy.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.view.utils.MyFragment;
import com.lovegod.newbuy.view.utils.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_FIXED;


/**
 * *****************************************
 * Created by thinking on 2017/5/5.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class Shop2Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    TextView shop_name, textview;
    TextView shop_location;
    RatingBar shop_valuation;
    ImageView shop_image, shop_phone, shop_back, shop_san;
    Toolbar toolbar;
    RelativeLayout shop_tops;
    RelativeLayout shop_re1;
LinearLayout linearlayout_location;
    TabLayout mtablayout;
    ViewPager shop_viewpage;


    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_info);


        shop_tops = (RelativeLayout) findViewById(R.id.shop_tops);
        textview = (TextView) findViewById(R.id.textview);
        shop_name = (TextView) findViewById(R.id.shop_name);
        shop_location = (TextView) findViewById(R.id.shop_location);
        shop_image = (ImageView) findViewById(R.id.shop_image);
        shop_valuation = (RatingBar) findViewById(R.id.shop_valuation);
        shop_back = (ImageView) findViewById(R.id.shop_back);
        shop_san = (ImageView) findViewById(R.id.shop_san);
        shop_phone = (ImageView) findViewById(R.id.shop_phone);
        shop_re1 = (RelativeLayout) findViewById(R.id.shop_re1);
        linearlayout_location=(LinearLayout)findViewById(R.id.linearlayout_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   shop_tablayout = (TableLayout) findViewById(R.id.shop_tablayout);
        shop_viewpage = (ViewPager) findViewById(R.id.shop_viewpage);

        textview.setText("wodeqoeo");

        mtablayout = (TabLayout) findViewById(R.id.shop_tablayout);

        shop_viewpage = (ViewPager) findViewById(R.id.shop_viewpage);

        /* 返回键*/
        shop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /* 三个点键*/
        shop_san.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //
            }
        });
         /* 店铺评价*/
        shop_re1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //店铺评价
            }
        });
        /* 导航*/
       linearlayout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导航
            }
        });
         /* 电话*/
        shop_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:18838150659"));
                startActivity(intent);
            }
        });
        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();
        // 对各种控件进行设置、适配、填充数据
        configViews();

    }

    private void initData() {
        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);
        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            MyFragment mFragment = new MyFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(i, mFragment);
        }

    }

    private void configViews() {
        // 初始化ViewPager的适配器，并设置给它
        // mViewPagerAdapter=new MyViewPagerAdapter(getSupp(),mFragments);
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        shop_viewpage.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        shop_viewpage.setOffscreenPageLimit(3);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        shop_viewpage.addOnPageChangeListener(this);
        mtablayout.setTabMode(MODE_FIXED);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mtablayout.setupWithViewPager(shop_viewpage);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mtablayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }
}