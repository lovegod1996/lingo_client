package com.lovegod.newbuy.view.myinfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lovegod.newbuy.bean.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询订单ViewPager的Fragment适配器
 * Created by ywx on 2017/7/22.
 */

public class OrderInfoFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment>fragmentList=new ArrayList<>();
    private List<String>titleList=new ArrayList<>();
    public OrderInfoFragmentAdapter(FragmentManager fm,List<Fragment>fragmentList,List<String>titleList) {
        super(fm);
        this.fragmentList=fragmentList;
        this.titleList=titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
