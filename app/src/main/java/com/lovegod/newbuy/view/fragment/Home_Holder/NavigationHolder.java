package com.lovegod.newbuy.view.fragment.Home_Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.view.search.ControlScrollRecyclerView;


/**
 * Created by ywx on 2017/8/17.
 * 首页导航栏的holder
 */

public class NavigationHolder extends RecyclerView.ViewHolder {
    public ControlScrollRecyclerView controlScrollRecyclerView;
    public NavigationHolder(View itemView) {
        super(itemView);
        controlScrollRecyclerView=(ControlScrollRecyclerView)itemView.findViewById(R.id.home_navigation_list);
    }
}
