package com.lovegod.newbuy.view.goods;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Assess;
<<<<<<< HEAD
=======
import com.lovegod.newbuy.bean.BaseBean;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.userPreferences.UserPreferencesUtil;
import com.lovegod.newbuy.view.utils.MyRecyclerViewAdapter;
>>>>>>> 291cb3c5f5bb7dd43f5378c3efd8e7153b40df00

import java.util.ArrayList;
import java.util.List;


/**
 * *****************************************
 * Created by thinking on 2017/6/13.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class AssessActivity extends AppCompatActivity {

    RadioGroup radioGroupID;
    RadioButton all_assess;
    RadioButton total_assess;
    RadioButton good_assess;
    RadioButton second_assess;
    RadioButton bad_assess;
    RadioButton pic_assess;
    RecyclerView assess_listview;
    List<Assess> assessList=new ArrayList<>();
    private AssessRecyclerViewAdapter assessRecyclerViewAdapter;
    private Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_assess);
        toolbar=(Toolbar)findViewById(R.id.good_assess_toolbar);
        radioGroupID=(RadioGroup)findViewById(R.id.radioGroupID);
        all_assess=(RadioButton) findViewById(R.id.all_assess);
        total_assess=(RadioButton)findViewById(R.id.total_assess);
        good_assess=(RadioButton)findViewById(R.id.good_assess);
        second_assess=(RadioButton)findViewById(R.id.second_assess);
        bad_assess=(RadioButton)findViewById(R.id.bad_assess);
        pic_assess=(RadioButton)findViewById(R.id.pic_assess);
        assess_listview=(RecyclerView) findViewById(R.id.assess_listview);

        setSupportActionBar(toolbar);

        int cid=Integer.valueOf(getIntent().getStringExtra("Cid"));
        user= (User) SpUtils.getObject(this,"userinfo");

        //添加评价足迹
        if(user!=null) {
            UserPreferencesUtil.changeTrackInfo(this,user.getUid(),cid,1,0);
        }

        /**
         * 返回按钮监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NetWorks.getAllAssess(cid, new BaseObserver<List<Assess>>(this) {
            @Override
            public void onHandleSuccess(List<Assess> assesses) {
                assessList=assesses;

                all_assess.setText("全部评论("+assesses.size()+")");
                int total=0,good=0,second=0,bad=0,pic=0;
                for (int  i=0;i<assesses.size();i++){
                    switch ((int) assesses.get(i).getGrade()){
                        case 0: bad++;break;
                        case 1: bad++;break;
                        case 2: second++;break;
                        case 3: second++;break;
                        case 4: good++;break;
                        case 5: good++;break;
                        default:break;
                    }
                    if (assesses.get(i).getPics()!=null){
                        pic++;
                    }
                    if (assesses.get(i).getHollrall()!=null){
                        total++;
                    }
                }
                if (total!=0) {
                    total_assess.setText("总评(" + total + ")");
                }
                else total_assess.setVisibility(View.GONE);

                if (good!=0) {
                    good_assess.setText("好评("+good+")");
                }
                else good_assess.setVisibility(View.GONE);

                if (second!=0) {
                    second_assess.setText("中评("+second+")");
                }
                else second_assess.setVisibility(View.GONE);

                if (bad!=0) {
                    bad_assess.setText("差评("+bad+")");
                }
                else bad_assess.setVisibility(View.GONE);

                if (pic!=0) {
                    pic_assess.setText("有图("+pic+")");
                }
                else pic_assess.setVisibility(View.GONE);

                LinearLayoutManager layoutManager = new LinearLayoutManager(AssessActivity.this);//设置布局管理器
                assess_listview.setLayoutManager(layoutManager);
                all_assess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all_assess.setChecked(true);
                        total_assess.setChecked(false);
                        good_assess.setChecked(false);
                        second_assess.setChecked(false);
                        bad_assess.setChecked(false);
                        pic_assess.setChecked(false);
                        // all_assess.setTextColor(getResources().getColor(R.color.colorPrimary));
                        assessRecyclerViewAdapter.notifyDataSetChanged();
                        assessRecyclerViewAdapter= new AssessRecyclerViewAdapter(AssessActivity.this,assessList);
                        assess_listview.setAdapter(assessRecyclerViewAdapter);//设置Adapter
                    }
                });
                total_assess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  total_assess.setTextColor(getResources().getColor(R.color.colorPrimary));
                        all_assess.setChecked(false);
                        total_assess.setChecked(true);
                        good_assess.setChecked(false);
                        second_assess.setChecked(false);
                        bad_assess.setChecked(false);
                        pic_assess.setChecked(false);
                        List<Assess> assesstotal = new ArrayList<Assess>();
                        for (int  i=0;i<assessList.size();i++) {
                            if (assessList.get(i).getHollrall() != null) {
                                assesstotal.add(assessList.get(i));
                            }
                        }
                        assessRecyclerViewAdapter= new AssessRecyclerViewAdapter(AssessActivity.this,assesstotal);
                        assess_listview.setAdapter(assessRecyclerViewAdapter);//设置Adapter
                    }
                });


                good_assess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all_assess.setChecked(false);
                        total_assess.setChecked(false);
                        good_assess.setChecked(true);
                        second_assess.setChecked(false);
                        bad_assess.setChecked(false);
                        pic_assess.setChecked(false);
                        // good_assess.setTextColor(getResources().getColor(R.color.colorPrimary));
                        List<Assess> assessgood = new ArrayList<Assess>();
                        for (int  i=0;i<assessList.size();i++) {
                            if ((int)assessList.get(i).getGrade()==4||(int)assessList.get(i).getGrade()==5) {
                                assessgood.add(assessList.get(i));
                            }
                        }
                        assessRecyclerViewAdapter= new AssessRecyclerViewAdapter(AssessActivity.this,assessgood);
                        assess_listview.setAdapter(assessRecyclerViewAdapter);//设置Adapter
                    }
                });
                second_assess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all_assess.setChecked(false);
                        total_assess.setChecked(false);
                        good_assess.setChecked(false);
                        second_assess.setChecked(true);
                        bad_assess.setChecked(false);
                        pic_assess.setChecked(false);
                        //   second_assess.setTextColor(getResources().getColor(R.color.colorPrimary));
                        List<Assess> assesssecond = new ArrayList<Assess>();
                        for (int  i=0;i<assessList.size();i++) {
                            if ((int)assessList.get(i).getGrade()==3||(int)assessList.get(i).getGrade()==2) {
                                assesssecond.add(assessList.get(i));
                            }
                        }
                        assessRecyclerViewAdapter= new AssessRecyclerViewAdapter(AssessActivity.this,assesssecond);
                        assess_listview.setAdapter(assessRecyclerViewAdapter);//设置Adapter
                    }
                });
                bad_assess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all_assess.setChecked(false);
                        total_assess.setChecked(false);
                        good_assess.setChecked(false);
                        second_assess.setChecked(false);
                        bad_assess.setChecked(true);
                        pic_assess.setChecked(false);
                        //   bad_assess.setTextColor(getResources().getColor(R.color.colorPrimary));
                        List<Assess> assessbad = new ArrayList<Assess>();
                        for (int  i=0;i<assessList.size();i++) {
                            if ((int)assessList.get(i).getGrade()==0||(int)assessList.get(i).getGrade()==1) {
                                assessbad.add(assessList.get(i));
                            }
                        }
                        assessRecyclerViewAdapter= new AssessRecyclerViewAdapter(AssessActivity.this,assessbad);
                        assess_listview.setAdapter(assessRecyclerViewAdapter);//设置Adapter
                    }
                });
                pic_assess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        total_assess.setChecked(false);
                        all_assess.setChecked(false);
                        good_assess.setChecked(false);
                        second_assess.setChecked(false);
                        bad_assess.setChecked(false);
                        pic_assess.setChecked(true);
                        //  pic_assess.setTextColor(getResources().getColor(R.color.colorPrimary));
                        List<Assess> assesspic = new ArrayList<Assess>();
                        for (int  i=0;i<assessList.size();i++) {
                            if (assessList.get(i).getPics() != null) {
                                assesspic.add(assessList.get(i));
                            }
                        }
                        assessRecyclerViewAdapter= new AssessRecyclerViewAdapter(AssessActivity.this,assesspic);
                        assess_listview.setAdapter(assessRecyclerViewAdapter);//设置Adapter
                    }
                });


                assessRecyclerViewAdapter= new AssessRecyclerViewAdapter(AssessActivity.this,assesses);
                assess_listview.setAdapter(assessRecyclerViewAdapter);//设置Adapter

            }

            @Override
            public void onHandleError(List<Assess> assesses) {

            }
        });
    }


}
