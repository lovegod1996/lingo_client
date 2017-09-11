package com.lovegod.newbuy.view.goods;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Compare;

import java.util.ArrayList;
import java.util.List;


/**
 * *****************************************
 * Created by thinking on 2017/4/5.
 * 创建时间：
 * <p>
 * 描述：比较列表
 * <p/>
 * <p/>
 * *******************************************
 */

public class CompareActivity extends Activity {
    ImageView compare;
    ImageView compare_back;
    TextView compare_goodname;
    RecyclerView compare_list;
    RatingBar ratingbar;
    CompareAdapter adapter;
    List<Compare>compares=new ArrayList<>();
    Commodity commodity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_compare);
        init();
    }



    private void init() {
        compare=(ImageView)findViewById(R.id.compare);
        compare_back=(ImageView)findViewById(R.id.compare_back);
        compare_goodname=(TextView)findViewById(R.id.compare_goodname);
        compare_list=(RecyclerView)findViewById(R.id.compare_list);
        ratingbar=(RatingBar)findViewById(R.id.ratingbar);
        compare_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        getCommodity();

        setCommodityInfo();

        initListInfo();

        initRecyclerView();
    }

    /**
     * 初始化列表信息
     */
    private void initListInfo() {
        int[] shop_images = {R.mipmap.tu1, R.mipmap.tu2, R.mipmap.tu3, R.mipmap.tu4, R.mipmap.tu5, R.mipmap.tu6};
        int[] shop_sale = {4132,3345,1234,9991,8898,7878};
        String[] shop_name={"百业家电专营店","思迪电器专营店","中佳电器专营店","华强官方旗舰店","粤城电器","杭越电器专营店"};
        Double[] ratingbars={5.0,4.5,5.0,4.5,4.5,4.5};
        Double[] moneys={3900.0,3990.0,4099.0,4199.0,4199.00,4399.0};

        for (int i = 0; i < shop_images.length; i++) {
            Compare compare=new Compare();
            compare.setShopName(shop_name[i]);
            compare.setRate(ratingbars[i]);
            compare.setPrice(moneys[i]);
            compare.setPic(shop_images[i]);
            compare.setSale(shop_sale[i]);
            compares.add(compare);
        }
    }

    /**
     * 设置商品信息
     */
    private void setCommodityInfo() {
        Glide.with(this).load(commodity.getLogo()).into(compare);
        compare_goodname.setText(commodity.getProductname());
    }

    /**
     * 获得商品实例
     */
    private void getCommodity() {
        commodity= (Commodity) getIntent().getSerializableExtra("commodity");
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        adapter=new CompareAdapter(this,compares);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        compare_list.setAdapter(adapter);
        compare_list.setLayoutManager(manager);
    }
}
