package com.lovegod.newbuy.view.chat;

import android.content.Intent;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.view.goods.GoodActivity;

/**
 * Created by ywx on 2017/8/17.
 */

public class ChatListFragment extends EaseChatFragment {
    EaseChatFragmentHelper easeChatFragmentHelper=new EaseChatFragmentHelper() {
        @Override
        public void onSetMessageAttributes(EMMessage message) {

        }

        @Override
        public void onEnterToChatDetails() {

        }

        @Override
        public void onAvatarClick(String username) {

        }

        @Override
        public void onAvatarLongClick(String username) {

        }

        @Override
        public boolean onMessageBubbleClick(EMMessage message) {
            if(message.getType()== EMMessage.Type.TXT){
                String body=message.getBody().toString();
                if(body.indexOf("$")!=-1){
                    String cid=body.substring(body.indexOf("$")+1,body.indexOf("￥"));
                    NetWorks.findCommodity(Integer.parseInt(cid), new BaseObserver<Commodity>() {
                        @Override
                        public void onHandleSuccess(Commodity commodity) {
                            Intent intent=new Intent(getActivity(), GoodActivity.class);
                            intent.putExtra("commodity",commodity);
                            startActivity(intent);
                        }

                        @Override
                        public void onHandleError(Commodity commodity) {

                        }
                    });
                }
            }
            return false;
        }

        @Override
        public void onMessageBubbleLongClick(EMMessage message) {

        }

        @Override
        public boolean onExtendMenuItemClick(int itemId, View view) {
            return false;
        }

        @Override
        public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
            return null;
        }
    };

    @Override
    protected void initView() {
        super.initView();
        setChatFragmentHelper(easeChatFragmentHelper);
    }
}
