package com.lovegod.newbuy.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.LoginActivity;
import com.lovegod.newbuy.view.myinfo.MoreInfoActivity;
import com.lovegod.newbuy.view.myinfo.MyOrderInfoActivity;
import com.lovegod.newbuy.view.myinfo.SettingActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ywx on 2017/7/26.
 */

public class MyInfo_Activity extends Fragment implements View.OnClickListener{
    //全部
    private static final int ALL_FLAG=1;
    //待付款
    private static final int FOR_THE_PAY_FLAG=2;
    //待发货
    private static final int TO_THE_GOODS_FLAG=3;
    //待收货
    private static final int FOR_THE_GOODS_FLAG=4;
    //待评价
    private static final int TO_THE_ASSESS_FLAG=5;

    private SwipeRefreshLayout refreshLayout;
    private User user;
    private Toolbar toolbar;
    private TextView myInfoText,forThePayHint,toSendTheGoodsHint,forTheGoodsHint;
    private CircleImageView myInfoPortrait;
    private RelativeLayout allOrder,forThePay,toSendTheGoods,forTheGoods;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_my_info,container,false);
        toolbar=(Toolbar)view.findViewById(R.id.myinfo_tool_bar);
        MainActivity activity= (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);
        forThePayHint=(TextView)view.findViewById(R.id.for_the_payment_texthint);
        toSendTheGoodsHint=(TextView)view.findViewById(R.id.to_send_the_goods_texthint);
        forTheGoodsHint=(TextView)view.findViewById(R.id.for_the_goods_texthint);
        toSendTheGoods=(RelativeLayout)view.findViewById(R.id.to_send_the_goods_layout);
        forTheGoods=(RelativeLayout)view.findViewById(R.id.for_the_goods_layout);
        forThePay=(RelativeLayout)view.findViewById(R.id.for_the_payment_layout);
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        myInfoText=(TextView)view.findViewById(R.id.my_info_text);
        myInfoPortrait=(CircleImageView)view.findViewById(R.id.my_info_portrait);
        allOrder=(RelativeLayout)view.findViewById(R.id.all_order_layout);
        myInfoText.setOnClickListener(this);
        myInfoPortrait.setOnClickListener(this);
        allOrder.setOnClickListener(this);
        toSendTheGoods.setOnClickListener(this);
        forThePay.setOnClickListener(this);
        forTheGoods.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(user==null){
                    refreshLayout.setRefreshing(false);
                }else {
                    updateData();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_info_toolbar_menu,menu);
    }

    /**
     * 判断用户登录状态
     */
    private void judgeWhetherLogin() {
        //从缓存中获取对象
        user=(User) SpUtils.getObject(getActivity(),"userinfo");
        //如果对象不为空,表示已经登录
        if(user!=null){
            //判断各个货物状态的货物有多少
            judgeHint();
            //设置名字
            myInfoText.setText(user.getUsername());
            //判断该用户是否有头像
            if(!TextUtils.isEmpty(user.getHeaderpic())){
                Glide.with(this).load(user.getHeaderpic()).into(myInfoPortrait);
            }
        }else {
            myInfoText.setText(R.string.myinfo_login_hint);
            Glide.with(this).load(R.mipmap.defulat_portrait).into(myInfoPortrait);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_info_portrait:
            case R.id.my_info_text:
                startActivityByCheck(MoreInfoActivity.class);
                break;
            case R.id.all_order_layout:
                startActivityByCheck(MyOrderInfoActivity.class);
                break;
            case R.id.for_the_payment_layout:
                startActivityByCheckAndInfo(MyOrderInfoActivity.class,FOR_THE_PAY_FLAG);
                break;
            case R.id.to_send_the_goods_layout:
                startActivityByCheckAndInfo(MyOrderInfoActivity.class,TO_THE_GOODS_FLAG);
                break;
            case R.id.for_the_goods_layout:
                startActivityByCheckAndInfo(MyOrderInfoActivity.class,FOR_THE_GOODS_FLAG);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        judgeWhetherLogin();
        user=(User) SpUtils.getObject(getActivity(),"userinfo");

        //如果用户登陆了
        if(user!=null) {
            NetWorks.getIdInfo(user.getUid(), new BaseObserver<User>(getActivity()) {
                @Override
                public void onHandleSuccess(User newUser) {
                    if (!user.getPassword().equals(newUser.getPassword())) {
                        SpUtils.removeKey(getActivity(), "userinfo");
                        AlertDialog dialog=new AlertDialog.Builder(getActivity()).create();
                        dialog.setTitle("警告");
                        dialog.setMessage("当前账户密码被修改! 请重新登陆!");
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    } else {
                        SpUtils.removeKey(getActivity(), "userinfo");
                        SpUtils.putObject(getActivity(), "userinfo", user);
                    }
                    refreshLayout.setRefreshing(false);
                    judgeWhetherLogin();
                }

                @Override
                public void onHandleError(User user) {
                    Toast.makeText(getActivity(), "发生错误", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            judgeWhetherLogin();
        }
    }

    /**
     *
     */
    private void judgeHint() {
        //获取未付款订单个数
        NetWorks.getForThePaymentOrder(user.getUid(), new BaseObserver<List<Order>>(getActivity()) {
            @Override
            public void onHandleSuccess(List<Order> orders) {
                if(orders.size()!=0) {
                    forThePayHint.setVisibility(View.VISIBLE);
                    forThePayHint.setText(orders.size() + "");
                }else {
                    forThePayHint.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onHandleError(List<Order> orders) {

            }
        });

        //获取待发货订单个数
        NetWorks.getToSendGoodsOrder(user.getUid(), new BaseObserver<List<Order>>(getActivity()) {
            @Override
            public void onHandleSuccess(List<Order> orders) {
                if(orders.size()!=0){
                    toSendTheGoodsHint.setVisibility(View.VISIBLE);
                    toSendTheGoodsHint.setText(orders.size()+"");
                }else {
                    toSendTheGoodsHint.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onHandleError(List<Order> orders) {

            }
        });

        //获取待收货订单个数
        NetWorks.getForTheGoodsOrder(user.getUid(), new BaseObserver<List<Order>>() {
            @Override
            public void onHandleSuccess(List<Order> orders) {
                if(orders.size()!=0){
                    forTheGoodsHint.setVisibility(View.VISIBLE);
                    forTheGoodsHint.setText(orders.size()+"");
                }else {
                    forTheGoodsHint.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onHandleError(List<Order> orders) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.myinfo_skin:
                break;
            case R.id.myinfo_settings:
                startActivityByCheck(SettingActivity.class);
                break;
            case R.id.myinfo_notification:
                break;
        }
        return true;
    }

    /**
     * 根据登录状态判断要去的活动
     * 不登录就跳转到登录活动
     * @param startClass 如果登陆状态要去的活动
     */
    private void startActivityByCheck(Class<?> startClass){
        if (user==null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else {
            startActivity(new Intent(getActivity(), startClass));
        }
    }

    /**
     * 根据登录状态判断要去的活动并且携带参数
     * 不登录就跳转到登录活动
     * @param startClass 登录状态下去的活动
     * @param flag 携带的参数
     */
    private void startActivityByCheckAndInfo(Class<?> startClass,int flag){
        if (user==null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else {
            Intent intent=new Intent(getActivity(), startClass);
            intent.putExtra("order_page",flag);
            startActivity(intent);
        }
    }

    /**
     * 调用onResume查询数据是否变动
     */
    private void updateData(){
        onResume();
    }
}
