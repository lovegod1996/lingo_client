package com.lovegod.newbuy.view.carts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayChooseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SuperTextView wechatPay,aliPay;
    private TextView priceText;
    private double payPrice=0F;
    private List<Order>orderList=new ArrayList<>();
    private Button commtButton;
    private String payType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_choose);
        //获取传过来的总价格
        orderList= (List<Order>) getIntent().getSerializableExtra("order_data");
        toolbar=(Toolbar)findViewById(R.id.pay_choose_toolbar);
        priceText=(TextView)findViewById(R.id.pay_choose_pricetext);
        wechatPay=(SuperTextView)findViewById(R.id.pay_choose_wechatpay);
        aliPay=(SuperTextView)findViewById(R.id.pay_choose_alipay);
        commtButton=(Button)findViewById(R.id.pay_choose_button);
        //拿到这次交易的总价
        for(int i=0;i<orderList.size();i++){
            payPrice+=orderList.get(i).getTotalprice();
        }

        setSupportActionBar(toolbar);

        DecimalFormat df=new DecimalFormat("######0.0");
        priceText.setText("¥"+df.format(payPrice));

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
                for(i=0;i<orderList.size();i++){
                    final int finalI = i;
                    NetWorks.commitPayOrder(orderList.get(i).getOid(), payType, new BaseObserver<Order>(PayChooseActivity.this) {
                        @Override
                        public void onHandleSuccess(Order order) {
                            if(finalI ==orderList.size()-1){
//                                Intent intent=new Intent(PayChooseActivity.this,OrderInfoActivty.class);
//                                intent.putExtra("order_info", (Serializable) orderList);
//                                startActivity(intent);
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
}
