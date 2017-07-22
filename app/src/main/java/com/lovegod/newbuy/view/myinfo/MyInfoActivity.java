package com.lovegod.newbuy.view.myinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.ActivityCollector;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private SwipeRefreshLayout refreshLayout;
    private User user;
    private Toolbar toolbar;
    private TextView myInfoText;
    private CircleImageView myInfoPortrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        ActivityCollector.addActivity(MyInfoActivity.this);
        toolbar=(Toolbar)findViewById(R.id.myinfo_tool_bar);
        setSupportActionBar(toolbar);

        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        myInfoText=(TextView)findViewById(R.id.my_info_text);
        myInfoPortrait=(CircleImageView)findViewById(R.id.my_info_portrait);
        myInfoText.setOnClickListener(this);
        myInfoPortrait.setOnClickListener(this);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_info_toolbar_menu,menu);
        return true;
    }

    /**
     * 判断用户登录状态
     */
    private void judgeWhetherLogin() {
        //从缓存中获取对象
        user=(User) SpUtils.getObject(MyInfoActivity.this,"userinfo");
        //如果对象不为空,表示已经登录
        if(user!=null){
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
        }
    }

    /**
     * 查询密码是否发生改变
     * 改变了就清除用户本地缓存，这样用户就要重新登录
     */
    @Override
    protected void onResume() {
        super.onResume();
        judgeWhetherLogin();
        user=(User) SpUtils.getObject(MyInfoActivity.this,"userinfo");

        //如果用户登陆了
        if(user!=null) {
            NetWorks.getIdInfo(user.getUid(), new BaseObserver<User>(MyInfoActivity.this) {
                @Override
                public void onHandleSuccess(User newUser) {
                    if (!user.getPassword().equals(newUser.getPassword())) {
                        SpUtils.removeKey(MyInfoActivity.this, "userinfo");
                        AlertDialog dialog=new AlertDialog.Builder(MyInfoActivity.this).create();
                        dialog.setTitle("警告");
                        dialog.setMessage("当前账户密码被修改! 请重新登陆!");
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    } else {
                        SpUtils.removeKey(MyInfoActivity.this, "userinfo");
                        SpUtils.putObject(MyInfoActivity.this, "userinfo", user);
                    }
                    refreshLayout.setRefreshing(false);
                    onRefresh();
                }

                @Override
                public void onHandleError(User user) {
                    Toast.makeText(MyInfoActivity.this, "发生错误", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            onRefresh();
        }
    }

    /**
     * toolbar的点击事件
     * @param item
     * @return
     */
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
            startActivity(new Intent(MyInfoActivity.this, LoginActivity.class));
        }else {
            startActivity(new Intent(MyInfoActivity.this, startClass));
        }
    }

    /**
     * 调用onResume查询数据是否变动
     */
    private void updateData(){
        onResume();
    }
    private void onRefresh(){
        judgeWhetherLogin();
    }
}
