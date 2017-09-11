package com.lovegod.newbuy.view.myinfo.notification;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.Data;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.view.chat.ChatActivity;

public class NotificationActivity extends AppCompatActivity {
    private FrameLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        layout=(FrameLayout)findViewById(R.id.notification_layout);

        //消息列表的fragment
        EaseConversationListFragment conversationListFragment=new EaseConversationListFragment();

        /**
         * 消息列表item点击监听
         */
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                final Intent intent=new Intent(NotificationActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                //通过手机号获取用户名
                com.hyphenate.easeui.NetWorks.judgeUserType(conversation.conversationId(), new com.hyphenate.easeui.BaseObserver<Data>() {
                    @Override
                    public void onHandleSuccess(Data data) {
                        switch (data.getType()){
                            case 0:
                                intent.putExtra("username",data.getUser().getUsername());
                                startActivity(intent);
                                break;
                            case 1:
                                intent.putExtra("username",data.getBoss().getNickname());
                                startActivity(intent);
                                break;
                        }
                    }

                    @Override
                    public void onHandleError(Data data) {

                    }
                });
            }
        });
        replaceFragment(conversationListFragment);
    }

    /**
     * 替换fragment到主页面中
     * @param fragment 新的fragment
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.notification_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
