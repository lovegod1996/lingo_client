package com.lovegod.newbuy.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.FavouriteQuest;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.LoginActivity;
import com.lovegod.newbuy.view.myinfo.AddAssessActivity;
import com.lovegod.newbuy.view.myinfo.FavouriteActivity;
import com.lovegod.newbuy.view.myinfo.MoreInfoActivity;
import com.lovegod.newbuy.view.myinfo.MyOrderInfoActivity;
import com.lovegod.newbuy.view.myinfo.SettingActivity;

import java.util.ArrayList;
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
    private TextView myInfoText,forThePayHint,toSendTheGoodsHint,forTheGoodsHint,forTheAssessHint;
    private CircleImageView myInfoPortrait;
    private RelativeLayout allOrder,forThePay,toSendTheGoods,forTheGoods,toTheAssess,favouriteGoods,favouriteShop;

    private AskAnswerAdapter adapter;
    private List<FavouriteQuest>questList=new ArrayList<>();
    private int questPage=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_my_info,container,false);
        toolbar=(Toolbar)view.findViewById(R.id.myinfo_tool_bar);
        MainActivity activity= (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);
        forTheAssessHint=(TextView)view.findViewById(R.id.for_the_assess_texthint);
        forThePayHint=(TextView)view.findViewById(R.id.for_the_payment_texthint);
        toSendTheGoodsHint=(TextView)view.findViewById(R.id.to_send_the_goods_texthint);
        forTheGoodsHint=(TextView)view.findViewById(R.id.for_the_goods_texthint);
        favouriteShop=(RelativeLayout)view.findViewById(R.id.favourite_shop_layout);
        favouriteGoods=(RelativeLayout)view.findViewById(R.id.favourite_good_layout);
        toSendTheGoods=(RelativeLayout)view.findViewById(R.id.to_send_the_goods_layout);
        forTheGoods=(RelativeLayout)view.findViewById(R.id.for_the_goods_layout);
        forThePay=(RelativeLayout)view.findViewById(R.id.for_the_payment_layout);
        toTheAssess=(RelativeLayout)view.findViewById(R.id.for_the_assess_layout);
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
        toTheAssess.setOnClickListener(this);
        favouriteGoods.setOnClickListener(this);
        favouriteShop.setOnClickListener(this);

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
        Intent intent;
        switch (v.getId()){
            //点击头像或者用户名
            case R.id.my_info_portrait:
            case R.id.my_info_text:
                startActivityByCheck(MoreInfoActivity.class);
                break;
            //点击全部订单
            case R.id.all_order_layout:
                startActivityByCheck(MyOrderInfoActivity.class);
                break;
            //点击待付款
            case R.id.for_the_payment_layout:
                intent=new Intent(getActivity(), MyOrderInfoActivity.class);
                intent.putExtra("order_page",FOR_THE_PAY_FLAG);
                startActivityByCheckAndInfo(intent);
                break;
            //点击待发货
            case R.id.to_send_the_goods_layout:
                intent=new Intent(getActivity(), MyOrderInfoActivity.class);
                intent.putExtra("order_page",TO_THE_GOODS_FLAG);
                startActivityByCheckAndInfo(intent);
                break;
            //点击待收货
            case R.id.for_the_goods_layout:
                intent=new Intent(getActivity(), MyOrderInfoActivity.class);
                intent.putExtra("order_page",FOR_THE_GOODS_FLAG);
                startActivityByCheckAndInfo(intent);
                break;
            //点击待评价
            case R.id.for_the_assess_layout:
                startActivityByCheck(AddAssessActivity.class);
                break;
            //点击收藏宝贝
            case R.id.favourite_good_layout:
                intent=new Intent(getActivity(),FavouriteActivity.class);
                //0表示前往收藏商品页面
                intent.putExtra("type",0);
                startActivityByCheckAndInfo(intent);
                break;
            case R.id.favourite_shop_layout:
                intent=new Intent(getActivity(),FavouriteActivity.class);
                intent.putExtra("type",1);
                startActivityByCheckAndInfo(intent);
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
        NetWorks.getForTheGoodsOrder(user.getUid(), new BaseObserver<List<Order>>(getActivity()) {
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

        //是否有待评价的商品
        NetWorks.getNoAssessGoods(user.getUid(), 0, new BaseObserver<List<Order.OrderGoods>>(getActivity()) {
            @Override
            public void onHandleSuccess(List<Order.OrderGoods> orderGoodses) {
                if(orderGoodses.size()!=0){
                    forTheAssessHint.setVisibility(View.VISIBLE);
                }else {
                    forTheAssessHint.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onHandleError(List<Order.OrderGoods> orderGoodses) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.myinfo_question:
                //如果登陆了弹出问答对话框
                if(user!=null) {
                    showQuestionDialog();
                }else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
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
     * 弹出问答对话框
     */
    private void showQuestionDialog() {
        final Dialog dialog=new Dialog(getActivity(),R.style.transparent_dialog);
        LinearLayout root= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ask_answer_dialog,null);
        ImageView headImage= (ImageView) root.findViewById(R.id.ask_answer_dialog_headimage);
        ImageView cancelImage=(ImageView)root.findViewById(R.id.ask_answer_dialog_cancelimage);
        RecyclerView recyclerView=(RecyclerView)root.findViewById(R.id.ask_answer_dialog_recyclerview);
        //问答列表初始化
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        adapter=new AskAnswerAdapter(getActivity(),questList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        //获取关注问题的列表
        getFocusQuest();

        dialog.setContentView(root);
        Window window=dialog.getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        params.width=getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //开启动画
        Drawable drawable=headImage.getDrawable();
        if(drawable instanceof Animatable){
            ((Animatable) drawable).start();
        }

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                questPage=0;
                questList.clear();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(recyclerView.computeVerticalScrollExtent()+recyclerView.computeVerticalScrollOffset()>=recyclerView.computeVerticalScrollRange()){
                    getFocusQuest();
                }
            }
        });
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
     * 根据登录状态判断要去的活动
     * 不登录就跳转到登录活动
     * @param intent 携带参数的Intent
     */
    private void startActivityByCheckAndInfo(Intent intent){
        if (user==null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else {
            startActivity(intent);
        }
    }

    /**
     * 调用onResume查询数据是否变动
     */
    private void updateData(){
        onResume();
    }

    /**
     * 获取关注问题列表
     */
    private void getFocusQuest(){
        NetWorks.getQuestFocus(user.getUid(), questPage, new BaseObserver<List<FavouriteQuest>>() {
            @Override
            public void onHandleSuccess(List<FavouriteQuest> favouriteQuests) {
                questPage++;
                for(FavouriteQuest quests:favouriteQuests){
                    questList.add(quests);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<FavouriteQuest> favouriteQuests) {
                Toast.makeText(getActivity(),"没有更多了~",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
