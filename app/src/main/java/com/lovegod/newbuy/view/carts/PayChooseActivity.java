package com.lovegod.newbuy.view.carts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.view.myinfo.order.MyOrderInfoActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayChooseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SuperTextView wechatPay,aliPay;
    private TextView priceText;
    private double payPrice=0F;
    private ArrayList<String> orderIdList=new ArrayList();
    private Button commtButton;
    private String payType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_choose);

        //获取一次提交的订单Id数组
        orderIdList= getIntent().getStringArrayListExtra("order_data");
        toolbar=(Toolbar)findViewById(R.id.pay_choose_toolbar);
        priceText=(TextView)findViewById(R.id.pay_choose_pricetext);
        wechatPay=(SuperTextView)findViewById(R.id.pay_choose_wechatpay);
        aliPay=(SuperTextView)findViewById(R.id.pay_choose_alipay);
        commtButton=(Button)findViewById(R.id.pay_choose_button);

        //访问服务器查询最终
        getTotalPrice();

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 设置微信支付item复选框监听，当微信支付被勾选，取消勾选支付宝支付
         */
        wechatPay.setCheckBoxCheckedChangeListener(new SuperTextView.OnCheckBoxCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    payType="微信支付";
                    aliPay.setCbChecked(false);
                }
            }
        });

        /**
         * 设置支付宝item复选框监听，当支付宝被勾选，取消勾选微信支付
         */
        aliPay.setCheckBoxCheckedChangeListener(new SuperTextView.OnCheckBoxCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    payType="支付宝支付";
                    wechatPay.setCbChecked(false);
                }
            }
        });


        /**
         * 设置确认支付按钮监听
         */
        commtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                //如果没选择支付方式
                if(!wechatPay.getCbisChecked()&&!aliPay.getCbisChecked()){
                    Toast.makeText(PayChooseActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                    return;
                }
                //选择了就一个一个订单添加支付信息
                for(i=0;i<orderIdList.size();i++){
                    final int finalI = i;
                    long id=Long.parseLong(orderIdList.get(i));
                    NetWorks.commitPayOrder(id, payType, new BaseObserver<Order>(PayChooseActivity.this) {
                        @Override
                        public void onHandleSuccess(Order order) {
                            if(finalI ==orderIdList.size()-1){
                                startActivity(new Intent(PayChooseActivity.this,MyOrderInfoActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onHandleError(Order order) {
                            Toast.makeText(PayChooseActivity.this,"错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    /**
     * 查询最终的价格
     */
    private void getTotalPrice(){
        //遍历此次提交的订单列表
        for(int i=0;i<orderIdList.size();i++) {
            long id=Long.parseLong(orderIdList.get(i));
            final int finalI = i;
            NetWorks.getOrderGoods(id, new BaseObserver<List<Order.OrderGoods>>(this,new ProgressDialog(this)) {
                @Override
                public void onHandleSuccess(List<Order.OrderGoods> orderGoodses) {
                    //算出总价
                    for(Order.OrderGoods goods:orderGoodses){
                        payPrice+=goods.getTotalprice();
                    }
                    if(finalI ==orderIdList.size()-1){
                        DecimalFormat df=new DecimalFormat("######0.0");
                        priceText.setText("¥"+df.format(payPrice));
                    }
                }

                @Override
                public void onHandleError(List<Order.OrderGoods> orderGoodses) {

                }
            });
        }
    }
}
