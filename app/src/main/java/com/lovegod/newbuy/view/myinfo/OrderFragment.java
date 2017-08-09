package com.lovegod.newbuy.view.myinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.bean.Order;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Created by ywx on 2017/7/21.
 */

public class OrderFragment extends Fragment {
    //全部
    private static final int ALL_FLAG=1;
    //待付款
    private static final int FOR_THE_PAY_FLAG=2;
    //待发货
    private static final int TO_THE_GOODS_FLAG=3;
    //待收货
    private static final int FOR_THE_GOODS_FLAG=4;
    //待评价
    private static final int FINISH_FLAG=5;

    private static final String KEY="order_key";
    private RecyclerView recyclerView;
    private OrderPageAdapter adapter;
    private List<Order>orderList=new ArrayList<>();
    private User user;
    private int pageType;
    private int allPage=0,forThePayPage=0,forTheGoodsPage=0,toTheGoodsPage=0,finishPage=0;

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
        user= (User) SpUtils.getObject(getActivity(),"userinfo");
        Bundle bundle=getArguments();
        pageType=bundle.getInt(KEY);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter=new OrderPageAdapter(getActivity(),orderList,getActivity());
        recyclerView.setAdapter(adapter);
        //初始化查询第一页，并且将当前页数设置为1，防止切换viewpager的时候该方法不断执行导致页数增加
        switch (pageType){
            case ALL_FLAG:
                //查询所有订单
                orderList.clear();
                allPage=0;
                queryAllOrder(0);
                break;
            case FOR_THE_PAY_FLAG:
                //查询待付款订单
                orderList.clear();
                forThePayPage=0;
                queryOrderByStatue(forThePayPage,0,0);
                break;
            case TO_THE_GOODS_FLAG:
                //查询待发货订单
                orderList.clear();
                forTheGoodsPage=0;
                queryOrderByStatue(forTheGoodsPage,0,1);
                break;
            case FOR_THE_GOODS_FLAG:
                //查询待收货订单
                orderList.clear();
                toTheGoodsPage=0;
                queryOrderByStatue(toTheGoodsPage,0,2);
                break;
            case FINISH_FLAG:
                //查询已完成订单
                orderList.clear();
                finishPage=0;
                queryOrderByStatue(finishPage,1,3);
                break;
        }

        /**
         * 监听recyclerview的滚动而没用第三方上拉加载
         * 因为这里第三方加载会有问题
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()){
                    queryByType();
                }
            }
        });
    }

    /**
     * 分页查询所有的订单信息
     * @param page 第几页
     */
    private void queryAllOrder(final int page){
        final int[] requestOrderCount = {0};
        NetWorks.getAllOrderByPage(user.getUid(),page, new BaseObserver<List<Order>>(getActivity(),new ProgressDialog(getActivity())) {
           @Override
           public void onHandleSuccess(final List<Order> orders) {
               allPage=page+1;
               int i;
               for(i=0;i<orders.size();i++){
                   //如果订单未关闭
                   if((orders.get(i).getOpenstatue()==0)||(orders.get(i).getOpenstatue()==1&&orders.get(i).getStatue()==3)) {
                       final int finalI = i;
                       NetWorks.getOrderGoods(orders.get(i).getOid(), new BaseObserver<List<Order.OrderGoods>>() {
                           @Override
                           public void onHandleSuccess(List<Order.OrderGoods> orderGoodses) {
                               requestOrderCount[0]++;
                               orders.get(finalI).setOrderGoodsList(orderGoodses);
                               orderList.add(orders.get(finalI));
                               if(requestOrderCount[0] ==orders.size()){
                                   //排序
                                   Collections.sort(orderList,new orderComparator());
                                   adapter.notifyDataSetChanged();
                               }
                           }

                           @Override
                           public void onHandleError(List<Order.OrderGoods> orderGoodses) {

                           }
                       });
                   }else {
                       requestOrderCount[0]++;
                   }
               }
           }

           @Override
           public void onHandleError(List<Order> orders) {
           }
       });
    }

    /**
     * 根据订单状态分页查询
     * @param page 第几页
     * @param statue 订单的状态
     */
    private void queryOrderByStatue(int page,int openstatue,int statue){
        final int[] requestOrderCount = {0};
        NetWorks.getOrderByStatue(user.getUid(),statue,openstatue, page, new BaseObserver<List<Order>>(getActivity(),new ProgressDialog(getActivity())) {
            @Override
            public void onHandleSuccess(final List<Order> orders) {
                //判断类型增加相应的页数加一
                switch (pageType){
                    case FOR_THE_PAY_FLAG:
                        forThePayPage++;
                        break;
                    case FOR_THE_GOODS_FLAG:
                        forTheGoodsPage++;
                        break;
                    case TO_THE_GOODS_FLAG:
                        toTheGoodsPage++;
                        break;
                    case FINISH_FLAG:
                        finishPage++;
                        break;
                }
                for(int i=0;i<orders.size();i++){
                    //如果订单正在进行，或者订单关闭且已经确认收货就添加到列表中
                        final int finalI = i;
                        NetWorks.getOrderGoods(orders.get(i).getOid(), new BaseObserver<List<Order.OrderGoods>>() {
                            @Override
                            public void onHandleSuccess(List<Order.OrderGoods> orderGoodses) {
                                requestOrderCount[0]++;
                                orders.get(finalI).setOrderGoodsList(orderGoodses);
                                orderList.add(orders.get(finalI));
                                if(requestOrderCount[0] ==orders.size()){
                                    //排序
                                    Collections.sort(orderList,new orderComparator());
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onHandleError(List<Order.OrderGoods> orderGoodses) {

                            }
                        });
                }
            }

            @Override
            public void onHandleError(List<Order> orders) {
            }
        });
    }

    private void queryByType(){
        switch (pageType){
            case ALL_FLAG:
                //查询所有订单
                queryAllOrder(allPage);
                break;
            case FOR_THE_PAY_FLAG:
                //查询待付款订单
                queryOrderByStatue(forThePayPage,0,0);
                break;
            case FOR_THE_GOODS_FLAG:
                //查询待发货订单
                queryOrderByStatue(forTheGoodsPage,0,1);
                break;
            case TO_THE_GOODS_FLAG:
                //查询待收货订单
                queryOrderByStatue(toTheGoodsPage,0,2);
                break;
            case FINISH_FLAG:
                //查询已完成订单
                queryOrderByStatue(finishPage,1,3);
                break;
        }
    }

    /**
     * 自定义排序，按照订单创建的时间由晚到早进行排序
     */
    class orderComparator implements Comparator<Order>{
        @Override
        public int compare(Order o1, Order o2) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date o1Date = null,o2Date=null;
            try {
                o1Date=format.parse(o1.getCreatetime());
                o2Date=format.parse(o2.getCreatetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return (int) (o2Date.getTime()-o1Date.getTime());
        }
    }
}
