package com.lovegod.newbuy.view.myinfo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Trial;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.view.AdapterWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.lovegod.newbuy.R.id.t1;

public class Trial_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TrialAdapter adapter;
    private List<Trial>trialList=new ArrayList<>();
    private User user;
    private AdapterWrapper wrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        user= (User) SpUtils.getObject(this,"userinfo");
        toolbar=(Toolbar)findViewById(R.id.trial_toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.trial_recycler);
        setSupportActionBar(toolbar);

        TextView t = new TextView(this);
        t.setText("Header 1");
        //设置列表的布局以及适配器
        LinearLayoutManager manager=new LinearLayoutManager(this);
        adapter=new TrialAdapter(this,trialList);
        recyclerView.setLayoutManager(manager);
        RelativeLayout emptyLayout= (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.no_data_layout,null);
        wrapper=new AdapterWrapper(this,adapter,emptyLayout);
        recyclerView.setAdapter(wrapper);

        /**
         * 返回监听按钮
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取申请的列表
        getApplyList();
    }

    private void getApplyList() {
        NetWorks.getTrialGoods(user.getUid(), new BaseObserver<List<Trial>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Trial> trials) {
                for(Trial trial:trials){
                    trialList.add(trial);
                }
                wrapper.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Trial> trials) {

            }
        });
    }
}
