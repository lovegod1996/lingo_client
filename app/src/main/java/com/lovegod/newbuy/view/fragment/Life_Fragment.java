package com.lovegod.newbuy.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.exceptions.HyphenateException;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Location;
import com.lovegod.newbuy.bean.Options;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;
import com.lovegod.newbuy.view.life.NearbyShopAdapter;
import com.lovegod.newbuy.view.life.OptionsAdapter;
import com.lovegod.newbuy.view.myview.SearchLayout;
import com.lovegod.newbuy.view.search.ControlScrollRecyclerView;
import com.lovegod.newbuy.view.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/7.
 * 生活圈的fragment
 */

public class Life_Fragment extends Fragment {
    private TextView cityText;
    private SearchLayout searchLayout;
    private ControlScrollRecyclerView controlScrollRecyclerView;
    private RecyclerView shopRecycler;
    private List<Options>optionsList=new ArrayList<>();
    private List<Shop>shopList=new ArrayList<>();
    private List<EMChatRoom>roomList=new ArrayList<>();
    private OptionsAdapter optionsAdapter;
    private NearbyShopAdapter nearbyShopAdapter;
    private Handler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_life,container,false);
        searchLayout=(SearchLayout)view.findViewById(R.id.life_search);
        cityText=(TextView)view.findViewById(R.id.life_city);
        shopRecycler=(RecyclerView)view.findViewById(R.id.life_shop_recycler);
        controlScrollRecyclerView=(ControlScrollRecyclerView)view.findViewById(R.id.life_control_recycler);
        controlScrollRecyclerView.setCanScroll(false);
        //头部标签布局管理器
        FlexboxLayoutManager manager=new FlexboxLayoutManager(getActivity());
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setAlignItems(AlignItems.STRETCH);
        manager.setJustifyContent(JustifyContent.FLEX_START);
        controlScrollRecyclerView.setLayoutManager(manager);

        //店铺列表布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        shopRecycler.setLayoutManager(linearLayoutManager);

        //设置店铺列表适配器
        nearbyShopAdapter=new NearbyShopAdapter(shopList,roomList,getActivity());
        shopRecycler.setAdapter(nearbyShopAdapter);

        //设置头部标签适配器
        Options options=new Options();
        options.setText("热门");
        options.setChoose(true);
        optionsList.add(options);
        options=new Options();
        options.setText("家电");
        options.setChoose(false);
        optionsList.add(options);
        optionsAdapter=new OptionsAdapter(getActivity(),optionsList);
        controlScrollRecyclerView.setAdapter(optionsAdapter);


        /**
         * 搜索按钮监听,跳转到搜索界面
         */
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        /**
         * item点击监听
         */
        optionsAdapter.setOnItemClickListener(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //先让所有的item变为未选中
                for(int i=0;i<optionsList.size();i++){
                    optionsList.get(i).setChoose(false);
                }
                //再让当前点击的item变为已选中
                optionsList.get(position).setChoose(true);
                //通知数据发生了变化
                optionsAdapter.notifyDataSetChanged();
            }
        });

        getNearByShop();

//        /**
//         * 接受子线程获取聊天室成功的消息，更新适配器数据
//         */
//        handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what){
//                    case 1:
//                        nearbyShopAdapter.notifyDataSetChanged();
//                }
//            }
//        };
//
//        /**
//         * 获取前10个聊天室的数据
//         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    EMCursorResult<EMChatRoom> result = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(10, null);
//                    for(EMChatRoom room:result.getData()){
//                        roomList.add(room);
//                    }
//
//                    Log.d("room",roomList.size()+"");
//                    Log.d("room name",roomList.get(0).getName());
//                    Log.d("room detail",roomList.get(0).getMemberCount()+"/"+roomList.get(0).getMaxUsers());
//                    //线程请求到数据将消息发送回主线程进行更新
//                    Message message=new Message();
//                    message.what=1;
//                    handler.sendMessage(message);
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        return view;
    }

    /**
     * 获取附近商店
     */
    private void getNearByShop() {
        //获取用户坐标信息
        Location location = (Location) SpUtils.getObject(getActivity(),"location");
        //如果坐标不为空
        if(location!=null){
            cityText.setText(location.getAddress().substring(3,6));
            NetWorks.getNearbyShop(Double.parseDouble(location.getLon()), Double.parseDouble(location.getLat()), 5000, new BaseObserver<List<Shop>>(getActivity()) {
                @Override
                public void onHandleSuccess(List<Shop> shops) {
                 for(Shop shop:shops){
                     shopList.add(shop);
                 }
                 nearbyShopAdapter.notifyDataSetChanged();
                }

                @Override
                public void onHandleError(List<Shop> shops) {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
