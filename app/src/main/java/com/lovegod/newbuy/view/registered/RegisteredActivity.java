package com.lovegod.newbuy.view.registered;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.view.LoginActivity;
import com.lovegod.newbuy.view.fragment.RegisteredFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class RegisteredActivity extends AppCompatActivity {
    //填写手机号
    private static final int PHONE_SUBTITLE=0;
    //填写验证码
    private static final int VERIFY_SUBTITLE=1;
    //填写身份信息
    private static final int IDENTITY_SUBTITLE=2;
    //填写用户名密码
    private static final int LOGIN_INFO_SUBTITLE=3;
    private Toolbar toolbar;
    private ControlScrollViewPager viewPager;
    private List<Fragment> fragmentList=new ArrayList<>();
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        user=new User();
        toolbar=(Toolbar)findViewById(R.id.registered_tool_bar);
        setSupportActionBar(toolbar);
        viewPager=(ControlScrollViewPager)findViewById(R.id.registered_view_pager);
        init();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem=viewPager.getCurrentItem();
                if(currentItem==0){
                    startActivity(new Intent(RegisteredActivity.this, LoginActivity.class));
                    finish();
                }else {
                    setPage(currentItem-1);
                    changeSubTitle();
                }
            }
        });
    }

    private void init() {
        viewPager.setCanScroll(false);
        fragmentList.add(new PhoneRegisteredFragment());
        fragmentList.add(new VerifyRegisteredFragment());
        fragmentList.add(new IdentityRegisteredFragment());
        fragmentList.add(new LoginInfoRegisteredFragment());
        RegisteredFragmentAdapter adapter=new RegisteredFragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
    }

    public void setPage(int index)
    {
        //如果是最后一页注册，设置软键盘弹出自动调整布局让输入框不被覆盖
        if(index==LOGIN_INFO_SUBTITLE){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        //否则就不用，因为那样自定义的验证码在每次软键盘弹出隐藏都会被刷新
        else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
        viewPager.setCurrentItem(index);
        changeSubTitle();
    }
    public int getCurrentitem()
    {
        return viewPager.getCurrentItem();
    }
    public void changeSubTitle()
    {
        int currentItem=viewPager.getCurrentItem();
        switch (currentItem){
            case PHONE_SUBTITLE:
                toolbar.setSubtitle("手机号");
                break;
            case VERIFY_SUBTITLE:
                toolbar.setSubtitle("验证码");
                break;
            case IDENTITY_SUBTITLE:
                toolbar.setSubtitle("身份信息");
                break;
            case LOGIN_INFO_SUBTITLE:
                toolbar.setSubtitle("注册登录信息");
                break;
        }
    }
    public User getUser() {
        return user;
    }
}
