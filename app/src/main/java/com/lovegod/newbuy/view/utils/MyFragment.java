/*
 *
 *  *
 *  *  *
 *  *  *  * ===================================
 *  *  *  * Copyright (c) 2016.
 *  *  *  * 作者：安卓猴
 *  *  *  * 微博：@安卓猴
 *  *  *  * 博客：http://sunjiajia.com
 *  *  *  * Github：https://github.com/opengit
 *  *  *  *
 *  *  *  * 注意**：如果您使用或者修改该代码，请务必保留此版权信息。
 *  *  *  * ===================================
 *  *  *
 *  *  *
 *  *
 *
 */

package com.lovegod.newbuy.view.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lovegod.newbuy.MyApplication;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Goods;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.view.Shop2Activity;
import com.lovegod.newbuy.view.goods.GoodActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Monkey on 2015/6/29.
 */
public class MyFragment extends Fragment{

  private View mView;
 // private SwipeRefreshLayout mSwipeRefreshLayout;
  private RecyclerView mRecyclerView;
  private RecyclerView.LayoutManager mLayoutManager;
  private MyRecyclerViewAdapter mRecyclerViewAdapter;
  private MyStaggeredViewAdapter mStaggeredAdapter;

  private static final int LIST_SORT = 0;
  private static final int LIST_ALL = 1;
  private static final int LIST_DISCOUNT = 2;


  private static final int SPAN_COUNT = 2;
  private int flag = 0;
  private List<Commodity> goodslist=new ArrayList<>();

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.frag_main, container, false);
    return mView;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview);

    flag = (int) getArguments().get("flag");
    configRecyclerView();

  }

  private void configRecyclerView() {

    switch (flag) {
      case LIST_SORT:
        mLayoutManager =
            new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        break;
      case LIST_ALL:
        mLayoutManager =
            new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        break;
      case LIST_DISCOUNT:
        mLayoutManager =
            new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        break;
    }

    if (flag != LIST_DISCOUNT) {

       Shop shopp=(Shop)getActivity().getIntent().getSerializableExtra("shop");
        NetWorks.getIDshopgoods(shopp.getSid(), new BaseObserver<List<Commodity>>() {
            @Override
            public void onHandleSuccess(final List<Commodity> goodses) {

                goodslist=goodses;
                mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(),goodslist);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
              mRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                  @Override
                  public void onItemClick(View view, int position) {
                      Intent intent = new Intent(getActivity(), GoodActivity.class);
                      Bundle bundle = new Bundle();
                      bundle.putSerializable("commodity",goodses.get(position));
                      intent.putExtras(bundle);
                      startActivity(intent);
                  }

                  @Override
                  public void onItemLongClick(View view, int position) {

                  }
              });
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

    }
    else {
      mStaggeredAdapter = new MyStaggeredViewAdapter(getActivity());
      mRecyclerView.setAdapter(mStaggeredAdapter);
    }

    mRecyclerView.setLayoutManager(mLayoutManager);
  }

}
