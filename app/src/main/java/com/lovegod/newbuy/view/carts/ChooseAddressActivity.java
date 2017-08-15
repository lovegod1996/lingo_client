package com.lovegod.newbuy.view.carts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Address;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.myinfo.address.MyAddressActivity;

import java.util.ArrayList;
import java.util.List;

public class ChooseAddressActivity extends AppCompatActivity {
    private User user;
    private RecyclerView recyclerView;
    private List<Address>addressList=new ArrayList<>();
    private Toolbar toolbar;
    private TextView editText;
    private ChooseAddressAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        user= (User) SpUtils.getObject(this,"userinfo");
        recyclerView=(RecyclerView)findViewById(R.id.choose_area_recyclerview);
        toolbar=(Toolbar)findViewById(R.id.choose_address_toobar);
        editText=(TextView)findViewById(R.id.choose_address_toolbar_edit);
        setSupportActionBar(toolbar);


        adapter=new ChooseAddressAdapter(this,addressList);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        requestAddress();

        adapter.setOnItemClickListener(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent();
                intent.putExtra("choose_address_return",addressList.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        /**
         * 返回监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 管理按钮监听
         */
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseAddressActivity.this, MyAddressActivity.class));
            }
        });
    }

    private void requestAddress(){
        NetWorks.getAddress(user.getUid(), new BaseObserver<List<Address>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Address> list) {
                addressList.clear();
                for(Address address:list){
                    addressList.add(address);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Address> list) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAddress();
    }
}
