package com.lovegod.newbuy.view.carts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.D;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.view.BlurImageUtils;
import com.lovegod.newbuy.utils.view.FastBlur;
import com.lovegod.newbuy.view.Shop2Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;
import com.lovegod.newbuy.view.myinfo.MyOrderInfoActivity;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderInfoActivty extends AppCompatActivity {

    private Order order;
    private User user;
    private ImageView headImage;
    private TextView timeText,statueText,addressName,addressPhone,addressDetail,shopName,priceText,orderNum,payNum,createTime,payTime,deliverTime;
    private LinearLayout goodsLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info_activty);
        orderNum=(TextView)findViewById(R.id.order_info_ordernum);
        createTime=(TextView)findViewById(R.id.order_info_createtime);
        payTime=(TextView)findViewById(R.id.order_info_paytime);
        toolbar=(Toolbar)findViewById(R.id.order_info_toolbar);
        shopName=(TextView)findViewById(R.id.order_info_shopname);
        priceText=(TextView)findViewById(R.id.order_info_price);
        goodsLayout=(LinearLayout)findViewById(R.id.order_info_goodslayout);
        addressName=(TextView)findViewById(R.id.order_info_address_name);
        addressPhone=(TextView)findViewById(R.id.order_info_address_phone);
        addressDetail=(TextView)findViewById(R.id.order_info_address_address);
        headImage=(ImageView)findViewById(R.id.order_info_image);
        timeText=(TextView)findViewById(R.id.order_info_time_text);
        statueText=(TextView)findViewById(R.id.order_info_statue_text);
        setSupportActionBar(toolbar);
        user= (User) SpUtils.getObject(this,"userinfo");
        order= (Order) getIntent().getSerializableExtra("order_info");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //获取商店logo
        getShopLogo();

        //根据订单状态显示相应文字
        switch (order.getStatue()){
            case 0:
                getRestTime();
                statueText.setText("等待买家付款");
                break;
            case 1:
                statueText.setText("等待卖家发货");
                break;
            case 2:
                statueText.setText("等待买家确认收货");
                break;
            case 3:
                statueText.setText("交易完成");
                break;
        }

        //获取收货地址
        getAddress();

        //获取商品信息
        getGoodsInfo();


        //将订单信息设置到对应控件里
        initOrderInfo();
    }

    private void initOrderInfo() {
        orderNum.setText("订单编号: "+order.getOid());
        if(!TextUtils.isEmpty(order.getCreatetime())&&order.getCreatetime()!=null){
            createTime.setVisibility(View.VISIBLE);
            createTime.setText("创建时间: "+order.getCreatetime());
        }else {
            createTime.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(order.getPaytime())&&order.getPaytime()!=null){
            payTime.setVisibility(View.VISIBLE);
            payTime.setText("支付时间: "+order.getPaytime());
        }else {
            payTime.setVisibility(View.GONE);
        }
    }


    /**
     * 获取该订单的店铺logo作为背景图,并且获取店铺名
     */
    private void getShopLogo(){
        NetWorks.getIDshop(order.getSid(), new BaseObserver<Shop>() {
            @Override
            public void onHandleSuccess(final Shop shop) {
                //加载该订单所有商品的第一张图片作为头部，先加载Bitmap对其进行高斯模糊处理再设置到Imageview里面
                Glide.with(OrderInfoActivty.this).load(shop.getLogo()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //进行高斯模糊处理并设置禁Imgaeview
                        headImage.setImageBitmap(FastBlur.doBlur(resource,3, true));
                    }
                });
                shopName.setText(shop.getShopname());
                //设置店铺名点击跳转
                shopName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(OrderInfoActivty.this, Shop2Activity.class);
                        intent.putExtra("shop",shop);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onHandleError(Shop shop) {

            }
        });
    }

    /**
     * 访问网络时间与订单创建时间比对计算剩余的时间
     */
    private void getRestTime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url= null;//取得资源对象
                try {
                    url = new URL("http://www.taobao.com");
                    URLConnection uc=url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    final long ld=uc.getDate(); //取得网站日期时间
                    final Date currentDate=new Date(ld); //转换为标准时间对象
                    final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //把创建的时间转化为date类型
                    final Date createDate=format.parse(order.getCreatetime());
                    //获取创建时间一天后的date日期
                    Date finalDate=new Date(createDate.getTime()+24*60*60*1000);
                    //计算它们剩余的天、小时和分钟
                    final long diff=finalDate.getTime()-currentDate.getTime();
                    final long days=diff/(24*60*60*1000);
                    final long hours=((diff-days*(24*60*60*1000))/(60*60*1000));
                    final long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           timeText.setText("剩"+hours+"小时"+minutes+"分自动关闭");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 访问服务器获取地址信息
     */
    private void getAddress() {
        NetWorks.getAddressById(order.getSaid(), new BaseObserver<Address>(this) {
            @Override
            public void onHandleSuccess(Address address) {
                addressName.setText(address.getName());
                addressPhone.setText(address.getPhone());
                addressDetail.setText(address.getAddress().replaceAll(";","").replaceAll(" ",""));
            }

            @Override
            public void onHandleError(Address address) {

            }
        });
    }

    /**
     * 访问服务器获取订单里的商品信息
     */
    private void getGoodsInfo() {
        NetWorks.getOrderGoods(order.getOid(), new BaseObserver<List<Order.OrderGoods>>(this) {
            @Override
            public void onHandleSuccess(List<Order.OrderGoods> orderGoodses) {
                //获取到订单商品成功，遍历每个商品去寻找它的商品信息
                for(final Order.OrderGoods goods:orderGoodses){
                    NetWorks.findCommodity(goods.getCid(), new BaseObserver<Commodity>(OrderInfoActivty.this) {
                        @Override
                        public void onHandleSuccess(final Commodity commodity) {
                            //获取到商品信息，添加信息，并且用作跳转信息
                            final LinearLayout item = new LinearLayout(OrderInfoActivty.this);
                            LayoutInflater.from(OrderInfoActivty.this).inflate(R.layout.pay_good_item, item);
                            final ImageView goodPic = (ImageView) item.findViewById(R.id.pay_good_item_pic);
                            final TextView goodName = (TextView) item.findViewById(R.id.pay_good_item_name);
                            final TextView goodInfo = (TextView) item.findViewById(R.id.pay_good_item_info);
                            final TextView goodPrice = (TextView) item.findViewById(R.id.pay_good_item_price);
                            final TextView goodNum = (TextView) item.findViewById(R.id.pay_good_item_num);
                            Glide.with(OrderInfoActivty.this).load(goods.getLogo()).into(goodPic);
                            goodName.setText(goods.getGoodsname());
                            goodInfo.setText(goods.getParam());
                            goodPrice.setText("¥"+goods.getTotalprice()*1.0F/goods.getCount());
                            goodNum.setText("×"+goods.getCount());
                            goodsLayout.addView(item);
                            //商品item点击跳转
                            item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(OrderInfoActivty.this, GoodActivity.class);
                                    intent.putExtra("commodity",commodity);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onHandleError(Commodity commodity) {

                        }
                    });
                }
                DecimalFormat df=new DecimalFormat("######0.0");
                priceText.setText("¥"+df.format(order.getTotalprice()));
            }

            @Override
            public void onHandleError(List<Order.OrderGoods> orderGoodses) {

            }
        });
    }
}