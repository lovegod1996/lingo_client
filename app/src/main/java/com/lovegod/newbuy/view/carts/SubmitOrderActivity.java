package com.lovegod.newbuy.view.carts;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.PayShopBean;
import com.lovegod.newbuy.bean.ShopCartBean;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitOrderActivity extends AppCompatActivity {
    private List<ShopCartBean> buyList=new ArrayList<>();
    private List<PayShopBean> payShopList=new ArrayList<>();
    private Button submitOrderButton;
    private TextView defaultAddressName,defaultAddressPhone,defaultAddress;
    private RelativeLayout defaultLayout;
    private User user;
    private TextView totalPriceText;
    private RecyclerView recyclerView;
    private SubmitOrderAdapter adapter;
    private Toolbar toolbar;
    private Address orderAddress;
    private double totalPrice;
    private ArrayList<String>orderIdList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        user= (User) SpUtils.getObject(this,"userinfo");
        buyList= (List<ShopCartBean>) getIntent().getSerializableExtra("buy_goods");
        defaultLayout=(RelativeLayout)findViewById(R.id.submit_order_default_address_layout);
        submitOrderButton=(Button)findViewById(R.id.submit_order_button);
        defaultAddressName=(TextView)findViewById(R.id.submit_order_default_address_name);
        defaultAddressPhone=(TextView)findViewById(R.id.submit_order_default_address_phone);
        defaultAddress=(TextView)findViewById(R.id.submit_order_default_address_address);
        totalPriceText=(TextView)findViewById(R.id.submit_order_price);
        recyclerView=(RecyclerView)findViewById(R.id.submit_order_recyclerview);
        toolbar=(Toolbar)findViewById(R.id.submit_order_toolbar);
        setSupportActionBar(toolbar);
        init();
        adapter=new SubmitOrderAdapter(this,payShopList);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        //查询默认地址设置到布局中
        requestDefaultAddress();

        /**
         * 返回按键监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 提交订单按钮监听
         */
        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderAddress==null){
                    Toast.makeText(SubmitOrderActivity.this,"请先选择收货地址",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    //执行提交订单
                    commitOrder();
                    //提交完订单清空购物车
                    clearCart();
                }
     }
     });

        /**
         * 更换的收货地址点击监听
         */
        defaultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SubmitOrderActivity.this,ChooseAddressActivity.class),1);
            }
        });
    }


    /**
     * 初始化购买商品的商店列表
     */
    private void init(){
        totalPrice=0F;
        PayShopBean payShopBean=new PayShopBean();
        for(int i=0;i<buyList.size();i++){
            totalPrice+=buyList.get(i).getPrice()*buyList.get(i).getAmount()*1.0F;
            if(i>=1){
                if(buyList.get(i).getSid()!=buyList.get(i-1).getSid()) {
                    payShopList.add(payShopBean);
                    payShopBean = new PayShopBean();
                }
            }
            payShopBean.setSid(buyList.get(i).getSid());
            payShopBean.setShopname(buyList.get(i).getShopname());
            payShopBean.goodList.add(buyList.get(i));
        }
        payShopList.add(payShopBean);
        DecimalFormat df=new DecimalFormat("######0.0");
        totalPriceText.setText("¥"+df.format(totalPrice));
    }

    /**
     * 请求默认地址
     */
    private void requestDefaultAddress(){
        NetWorks.getAddress(user.getUid(), new BaseObserver<List<Address>>(this) {
            @Override
            public void onHandleSuccess(List<Address> list) {
                for(Address address:list){
                    if(address.getStatue()==1){
                        orderAddress=address;
                        defaultAddressName.setText(address.getName());
                        defaultAddressPhone.setText(address.getPhone());
                        defaultAddress.setText(address.getAddress().replace(" ","").replace(";",""));
                        break;
                    }
                }
            }

            @Override
            public void onHandleError(List<Address> list) {

            }
        });
    }

    /**
     * 获取选择地址活动返回回来的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                Address address= (Address) data.getSerializableExtra("choose_address_return");
                orderAddress=address;
                defaultAddressName.setText(address.getName());
                defaultAddressPhone.setText(address.getPhone());
                defaultAddress.setText(address.getAddress().replace(" ","").replace(";",""));
            }
        }
    }

    /**
     * 活动恢复时检查地址是否被删光了
     */
    @Override
    protected void onResume() {
        super.onResume();
        NetWorks.getAddress(user.getUid(), new BaseObserver<List<Address>>(this) {
            @Override
            public void onHandleSuccess(List<Address> list) {
                if(list.size()==0){
                    defaultAddressName.setText("");
                    defaultAddressPhone.setText("");
                    defaultAddress.setText("(你还没有设置默认收货地址或者没有收货地址,点击选择或添加收货地址)");
                }
            }

            @Override
            public void onHandleError(List<Address> list) {

            }
        });
    }

    /**
     * 提交订单请求
     **/
    private void commitOrder(){
        int i;
        Map<String,String> orderMap;
        double price;
        int goodsNum;
        //循环提交订单
        for(i=0;i<payShopList.size();i++){
            price=0F;
            goodsNum=0;
            orderMap=new HashMap<>();
            orderMap.put("uid",user.getUid()+"");
            for(ShopCartBean shopCartBean:payShopList.get(i).goodList){
                price+=shopCartBean.getPrice()*shopCartBean.getAmount()*1.0F;
                goodsNum+=shopCartBean.getAmount();
            }
            orderMap.put("count",goodsNum+"");
            orderMap.put("totalprice",price+"");
            orderMap.put("sid",payShopList.get(i).getSid()+"");
            orderMap.put("said",orderAddress.getSaid()+"");

            //在每个订单里循环该订单的每个商品进行提交
            final int finalI = i;
            NetWorks.commitOrder(orderMap, new BaseObserver<Order>(this) {
                @Override
                public void onHandleSuccess(Order order) {
                    orderIdList.add(order.getOid()+"");
                    Map<String,String>orderGoodsMap;
                    for(ShopCartBean shopCartBean:payShopList.get(finalI).goodList){
                        orderGoodsMap=new HashMap<>();
                        orderGoodsMap.put("oid",order.getOid()+"");
                        orderGoodsMap.put("cid",shopCartBean.getCid()+"");
                        orderGoodsMap.put("sid",shopCartBean.getSid()+"");
                        orderGoodsMap.put("count",shopCartBean.getAmount()+"");
                        orderGoodsMap.put("totalprice",(double)(shopCartBean.getAmount())*shopCartBean.getPrice()+"");
                        orderGoodsMap.put("param",shopCartBean.getCommodity_select());
                        NetWorks.commitOrderGoods(orderGoodsMap, new BaseObserver<Order.OrderGoods>(SubmitOrderActivity.this) {
                            @Override
                            public void onHandleSuccess(Order.OrderGoods orderGoods) {

                            }

                            @Override
                            public void onHandleError(Order.OrderGoods orderGoods) {

                            }
                        });
                    }
                    //如果是最后一个订单了，就打开支付页面
                    if(finalI ==payShopList.size()-1){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(SubmitOrderActivity.this,PayChooseActivity.class);
                                intent.putStringArrayListExtra("order_data",orderIdList);
                                startActivity(intent);
                                finish();
                            }
                        },500);
                    }
                }

                @Override
                public void onHandleError(Order order) {

                }
            });
        }
    }

    /**
     * 清空购物车操作
     */
    private void clearCart() {
        for(ShopCartBean shopCartBean:buyList) {
            NetWorks.DeleteCart(shopCartBean.getCbid(), new BaseObserver<ShopCartBean>(this) {
                @Override
                public void onHandleSuccess(ShopCartBean shopCartBean) {

                }

                @Override
                public void onHandleError(ShopCartBean shopCartBean) {

                }
            });
        }
    }
}
