package com.lovegod.newbuy.view.carts;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.view.BlurImageUtils;
import com.lovegod.newbuy.utils.view.FastBlur;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoActivty extends AppCompatActivity {

    private List<Order>orderList=new ArrayList<>();
    private User user;
    private ImageView headImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info_activty);
        headImage=(ImageView)findViewById(R.id.order_info_image);
        user= (User) SpUtils.getObject(this,"userinfo");
        orderList= (List<Order>) getIntent().getSerializableExtra("order_info");
        getGoodLogo();
    }

    /**
     * 获取该订单第一个商品的logo
     */
    private void getGoodLogo(){
        NetWorks.getIDshopgoods(orderList.get(0).getSid(), new BaseObserver<List<Commodity>>(this) {
            @Override
            public void onHandleSuccess(List<Commodity> commodities) {
                //加载该订单所有商品的第一张图片作为头部，先加载Bitmap对其进行高斯模糊处理再设置到Imageview里面
                Glide.with(OrderInfoActivty.this).load(SpUtils.getString(OrderInfoActivty.this,commodities.get(0).getLogo())).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //进行高斯模糊处理并设置禁Imgaeview
                        headImage.setImageBitmap(FastBlur.doBlur(resource,50,true));
                    }
                });
            }

            @Override
            public void onHandleError(List<Commodity> commodities) {

            }
        });
    }
}
