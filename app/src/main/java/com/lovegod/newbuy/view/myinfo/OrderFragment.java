package com.lovegod.newbuy.view.myinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovegod.newbuy.R;

/**
 * Created by ywx on 2017/7/21.
 */

public class OrderFragment extends Fragment {
    //全部
    private static final int ALL_FLAG=1;
    //待付款
    private static final int FOR_THE_PAY_FLAG=2;
    //待发货
    private static final int FOR_THE_GOODS_FLAG=3;
    //待收货
    private static final int TO_THE_GOODS_FLAG=4;
    //待评价
    private static final int TO_THE_ASSESS_FLAG=5;
    private static final String KEY="order_key";
    private RecyclerView recyclerView;

    public static Fragment newInstance(int flag){
        OrderFragment orderFragment=new OrderFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(KEY,flag);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_info_order,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.my_info_order_recyclerview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle=getArguments();
        int pageTtpe=bundle.getInt(KEY);
        switch (pageTtpe){
            case ALL_FLAG:
                break;
            case FOR_THE_PAY_FLAG:
                break;
            case FOR_THE_GOODS_FLAG:
                break;
            case TO_THE_GOODS_FLAG:
                break;
            case TO_THE_ASSESS_FLAG:
                break;
        }
    }
}
