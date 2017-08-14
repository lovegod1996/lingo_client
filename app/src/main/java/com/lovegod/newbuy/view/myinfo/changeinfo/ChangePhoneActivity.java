package com.lovegod.newbuy.view.myinfo.changeinfo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.view.fragment.RegisteredFragmentAdapter;
import com.lovegod.newbuy.view.registered.ControlScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class ChangePhoneActivity extends AppCompatActivity {
    private ControlScrollViewPager viewPager;
    private Toolbar toolbar;
    private List<Fragment>fragmentList=new ArrayList<>();
    private RegisteredFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        toolbar=(Toolbar)findViewById(R.id.change_phone_toolbar);
        viewPager=(ControlScrollViewPager)findViewById(R.id.change_phone_viewpager);
        setSupportActionBar(toolbar);
        init();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem=viewPager.getCurrentItem();
                if(currentItem==0){
                    finish();
                }else {
                    setCurrentItem(currentItem-1);
                }
            }
        });
    }
    private void init(){
        viewPager.setCanScroll(false);
        fragmentList.add(new ChangePhonePasswdFragment());
        fragmentList.add(new ChangePhoneNumberFragment());
        adapter=new RegisteredFragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
    }
    public void setCurrentItem(int position){
        viewPager.setCurrentItem(position);
    }
    public int getCurrentItem(){
        return viewPager.getCurrentItem();
    }
}
