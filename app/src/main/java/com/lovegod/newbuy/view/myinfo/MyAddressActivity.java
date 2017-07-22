package com.lovegod.newbuy.view.myinfo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.LoginMessage;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyAddressActivity extends AppCompatActivity {
    private Button addAddressButton;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyAddressAdapter addressAdapter;
    private List<Address>addressList=new ArrayList<>();
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        user= (User) SpUtils.getObject(this,"userinfo");
        addAddressButton=(Button)findViewById(R.id.add_address_button);
        toolbar=(Toolbar)findViewById(R.id.my_address_toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.my_address_recycler);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        addressAdapter=new MyAddressAdapter(this,addressList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(addressAdapter);

        setSupportActionBar(toolbar);

        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAddressActivity.this,ChangeAddressActivity.class));
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 监听删除按钮
         */
        addressAdapter.setOnDeleteClickListener(new MyAddressAdapter.OnDeleteClick() {
            @Override
            public void onDelete(final int position) {
               new AlertDialog.Builder(MyAddressActivity.this).setTitle("提醒").setMessage("确定删除该收货地址吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   //对画框确定按钮的监听
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       NetWorks.deleteAddress(addressList.get(position).getSaid(), new BaseObserver<LoginMessage>(MyAddressActivity.this) {
                           @Override
                           public void onHandleSuccess(LoginMessage loginMessage) {
                               requestMyadressInfo();
                           }
                           @Override
                           public void onHandleError(LoginMessage loginMessage) {

                           }
                       });
                   }
               }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   //对画框取消的监听
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                   }
               }).show();
            }
        });

        /**
         * 每一项点击监听
         */
        addressAdapter.setOnItemClickListener(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(MyAddressActivity.this,ChangeAddressActivity.class);
                intent.putExtra("edit_address",addressList.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 获取我的所有收货地址请求
     */
    private void requestMyadressInfo() {
        NetWorks.getAddress(user.getUid(), new BaseObserver<List<Address>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Address> list) {
                addressList.clear();
                for(Address address:list){
                    address.setUnfold(false);
                    addressList.add(address);
                }
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Address> list) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestMyadressInfo();
    }
}
