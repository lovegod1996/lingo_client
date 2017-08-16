package com.lovegod.newbuy.view.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Shop;
import com.lovegod.newbuy.bean.User;

/**
 * 说明：
 * Date: 2017/8/13
 * Created by lovegod .
 * Email:dx96_j@163.com
 */

public class ChatActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    private int sid;
    private int uid;
    private String toChatUsername;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat_activity_into);

        sid=getIntent().getIntExtra("sid",-1);
        uid=getIntent().getIntExtra("uid",-1);
        toChatUsername=getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new EaseChatFragment();  //环信聊天界面
        chatFragment.setArguments(getIntent().getExtras()); //需要的参数
        getSupportFragmentManager().beginTransaction().add(R.id.layout_chat,chatFragment).commit();  //Fragment切换


        setTitleBar();
    }

    /**
     * 设置标题栏标题
     */
    private void setTitleBar() {
        //如果是从商品页传过来的，设置店铺名，从用户列表传过来的，设置用户名
        if(uid==-1&&sid!=-1) {
            NetWorks.getIDshop(sid, new BaseObserver<Shop>() {
                @Override
                public void onHandleSuccess(Shop shop) {
                    chatFragment.setTitle(shop.getShopname());
                }

                @Override
                public void onHandleError(Shop shop) {

                }
            });
        }else if(uid!=-1&&sid==-1){
            NetWorks.getIdInfo(uid, new BaseObserver<User>() {
                @Override
                public void onHandleSuccess(User user) {
                    chatFragment.setTitle(user.getUsername());
                }

                @Override
                public void onHandleError(User user) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername(){
        return toChatUsername;
    }
}
