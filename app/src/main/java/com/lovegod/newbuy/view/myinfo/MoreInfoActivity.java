package com.lovegod.newbuy.view.myinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.ActivityCollector;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.myview.ItemTextLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private User user;
    private Button unLoginButton;
    private ItemTextLayout userNameItem,addressItem;
    private CircleImageView userPortrait;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_activity);
        ActivityCollector.addActivity(MoreInfoActivity.this);
        unLoginButton=(Button)findViewById(R.id.un_login_button);
        userNameItem=(ItemTextLayout)findViewById(R.id.more_info_username);
        userPortrait=(CircleImageView)findViewById(R.id.more_info_portrait);
        addressItem=(ItemTextLayout)findViewById(R.id.more_info_address);
        toolbar=(Toolbar)findViewById(R.id.more_info_toolbar);
        unLoginButton.setOnClickListener(this);
        userNameItem.setOnClickListener(this);
        addressItem.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getUserInfo(){
        user= (User) SpUtils.getObject(this,"userinfo");
        userNameItem.setRightText(user.getUsername());
        if(!TextUtils.isEmpty(user.getHeaderpic())) {
            Glide.with(this).load(user.getHeaderpic()).into(userPortrait);
        }else {
            Glide.with(this).load(R.mipmap.defulat_portrait).into(userPortrait);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //注销按钮，直接结束当前活动
            case R.id.un_login_button:
                SpUtils.removeKey(MoreInfoActivity.this,"userinfo");
                finish();
                break;
            case R.id.more_info_username:
                startActivity(new Intent(MoreInfoActivity.this,ChangeUserNameActivity.class));
                break;
            case R.id.more_info_address:
                startActivity(new Intent(MoreInfoActivity.this,MyAddressActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }
}
