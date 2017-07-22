package com.lovegod.newbuy.view.myinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.myview.ItemTextLayout;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private User user;
    private ItemTextLayout changePasswdItem,changePhoneItem,userInfoItem;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar=(Toolbar)findViewById(R.id.settings_toolbar);
        changePasswdItem=(ItemTextLayout)findViewById(R.id.settings_change_passwd);
        changePhoneItem=(ItemTextLayout)findViewById(R.id.settings_change_phone);
        userInfoItem=(ItemTextLayout)findViewById(R.id.settings_my_info);
        userInfoItem.setOnClickListener(this);
        changePasswdItem.setOnClickListener(this);
        changePhoneItem.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_change_passwd:
                startActivity(new Intent(SettingActivity.this,ChangePasswdActivity.class));
                break;
            case R.id.settings_my_info:
                startActivity(new Intent(SettingActivity.this,MoreInfoActivity.class));
                break;
            case R.id.settings_change_phone:
                startActivity(new Intent(SettingActivity.this,ChangePhoneActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user= (User) SpUtils.getObject(this,"userinfo");
        changePhoneItem.setRightText(user.getPhone());
    }
}
