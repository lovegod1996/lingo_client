package com.lovegod.newbuy.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.FavouriteGoods;
import com.lovegod.newbuy.bean.FavouriteShop;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.map.ShopLocationMap;
import com.lovegod.newbuy.view.utils.MyFragment;
import com.lovegod.newbuy.view.utils.MyViewPagerAdapter;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.GET;

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

    Shop shopdd;
    TextView shop_name, textview;
    TextView shop_location;
    RatingBar shop_valuation;
    ImageView shop_image, shop_phone, shop_back, shop_san,shop_logo;
    Toolbar toolbar;
    RelativeLayout shop_tops;
    RelativeLayout shop_re1;
    LinearLayout linearlayout_location;
    TabLayout mtablayout;
    ViewPager shop_viewpage;
    Button shop_focus;
    FavouriteShop focusShop;
    User user;
    boolean isFocus=false;

    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_info);
        user= (User) SpUtils.getObject(this,"userinfo");

        shopdd = (Shop) getIntent().getSerializableExtra("shop");

        shop_focus=(Button)findViewById(R.id.shop_focus);
        shop_logo=(ImageView)findViewById(R.id.shop_logo);
        shop_tops = (RelativeLayout) findViewById(R.id.shop_tops);
        textview = (TextView) findViewById(R.id.textview);
        shop_name = (TextView) findViewById(R.id.shop_name);
        shop_location = (TextView) findViewById(R.id.shop_location);
        shop_image = (ImageView) findViewById(R.id.shop_image);
        shop_valuation = (RatingBar) findViewById(R.id.shop_valuation);
        shop_back = (ImageView) findViewById(R.id.shop_back);
//        shop_back.getBackground().setAlpha(180);
        shop_san = (ImageView) findViewById(R.id.shop_san);
//        shop_san.getBackground().setAlpha(180);

        shop_phone = (ImageView) findViewById(R.id.shop_phone);
        shop_re1 = (RelativeLayout) findViewById(R.id.shop_re1);
        linearlayout_location = (LinearLayout) findViewById(R.id.linearlayout_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        shop_viewpage = (ViewPager) findViewById(R.id.shop_viewpage);

        textview.setText(shopdd.getShopname());
        //使用Glide加载图片
        Glide.with(this)
                .load(shopdd.getHeadershow())
                .placeholder(R.mipmap.default_image)
                .into(shop_image);

        Glide.with(this)
                .load(shopdd.getLogo())
                .into(shop_logo);

        shop_name.setText(shopdd.getShopname());
        shop_location.setText(shopdd.getSaddress());
        shop_valuation.setRating((float) shopdd.getSlevel());


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
              //  String[] shopinfo = new String[]{shopdd.getShopname(), String.valueOf(shopdd.getLonggitude()), String.valueOf(shopdd.getLatitude())};
                Bundle bundle = new Bundle();
                Intent intent = new Intent(Shop2Activity.this, ShopLocationMap.class);
                bundle.putSerializable("shop_info",shopdd);
             //   bundle.putStringArray("shop_info", shopinfo);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        shop_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户登录了才可以收藏店铺
                if(user!=null){
                    if(!isFocus){
                        addFocus();
                    }else {
                        cancelFocus();
                    }
                }else {
                    Toast.makeText(Shop2Activity.this,"想收藏需要先登录哦~",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //查询该用户是否关注该商店
        queryFocus();

        /**
         * 电话点击监听
         */
        shop_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setAction("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + shopdd.getStel()));
                startActivity(intent);
            }
        });
        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();
        // 对各种控件进行设置、适配、填充数据
        configViews();

    }

    /**
     * 取消关注
     */
    private void cancelFocus() {
        NetWorks.cancelShopFocus(focusShop.getSaid(), new BaseObserver<FavouriteShop>(this) {
            @Override
            public void onHandleSuccess(FavouriteShop favouriteShop) {
                shop_focus.setText("收藏");
                shop_focus.setTextColor(Color.WHITE);
                shop_focus.setBackgroundColor(Color.parseColor("#FD6861"));
                Toast.makeText(Shop2Activity.this,"取消收藏成功~",Toast.LENGTH_SHORT).show();
                isFocus=false;
            }

            @Override
            public void onHandleError(FavouriteShop favouriteShop) {

            }
        });
    }

    /**
     * 查询用户是否关注该商店(如果登陆的话)
     */
    private void queryFocus() {
        if(user!=null) {
            NetWorks.isShopFocus(user.getUid(), shopdd.getSid(), new BaseObserver<FavouriteShop>(this) {
                @Override
                public void onHandleSuccess(FavouriteShop favouriteShop) {
                    if(favouriteShop!=null){
                        focusShop=favouriteShop;
                        shop_focus.setText("取消收藏");
                        shop_focus.setTextColor(Color.parseColor("#FD6861"));
                        shop_focus.setBackgroundColor(Color.WHITE);
                        isFocus=true;
                    }else {
                        shop_focus.setText("收藏");
                        shop_focus.setTextColor(Color.WHITE);
                        shop_focus.setBackgroundColor(Color.parseColor("#FD6861"));
                        isFocus=false;
                    }
                }

                @Override
                public void onHandleError(FavouriteShop favouriteShop) {

                }
            });
        }

    }

    /**
     * 添加店铺关注
     */
    private void addFocus() {
            final Map<String,String>map=new HashMap<>();
            map.put("sid",shopdd.getSid()+"");
            map.put("uid",user.getUid()+"");
            //联网获取时间
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url= null;//取得资源对象
                    try {
                        url = new URL("http://www.taobao.com");
                        URLConnection uc = url.openConnection();//生成连接对象
                        uc.connect(); //发出连接
                        final long ld = uc.getDate(); //取得网站日期时间
                        final Date currentDate = new Date(ld); //转换为标准时间对象
                        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                map.put("looktime",format.format(currentDate));
                                NetWorks.addFocusShop(map, new BaseObserver<FavouriteShop>(Shop2Activity.this) {
                                    @Override
                                    public void onHandleSuccess(FavouriteShop favouriteShop) {
                                            focusShop=favouriteShop;
                                            shop_focus.setText("取消收藏");
                                            shop_focus.setTextColor(Color.parseColor("#FD6861"));
                                            shop_focus.setBackgroundColor(Color.WHITE);
                                            Toast.makeText(Shop2Activity.this,"收藏成功~",Toast.LENGTH_SHORT).show();
                                            isFocus=true;
                                    }

                                    @Override
                                    public void onHandleError(FavouriteShop favouriteShop) {

                                    }
                                });
                            }
                        });
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
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