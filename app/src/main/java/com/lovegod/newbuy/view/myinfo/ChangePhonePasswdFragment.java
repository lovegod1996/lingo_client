package com.lovegod.newbuy.view.myinfo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.Md5Util.MD5Util;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.view.ButtonUtils;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ChangePhonePasswdFragment extends Fragment {
    private User user;
    private TextInputEditText passwdText;
    private Button nextStepButton;
    private ChangePhoneActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frgment_change_phone_passwd,container,false);
        passwdText=(TextInputEditText)view.findViewById(R.id.change_phone_edit);
        nextStepButton=(Button) view.findViewById(R.id.change_phone_next_step);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity= (ChangePhoneActivity) getActivity();
        user= (User) SpUtils.getObject(getActivity(),"userinfo");
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String passwd=passwdText.getText().toString();
                if(TextUtils.isEmpty(passwd)) {
                    Toast.makeText(getActivity(), "请填写密码", Toast.LENGTH_SHORT).show();
                }else {
                    NetWorks.getIdInfo(user.getUid(), new BaseObserver<User>(getActivity(),new ProgressDialog(getActivity())) {
                        @Override
                        public void onHandleSuccess(User user) {
                            if(MD5Util.encode(passwd).equals(user.getPassword())){
                                if(!ButtonUtils.isFastDoubleClick(R.id.change_phone_next_step)) {
                                    activity.setCurrentItem(activity.getCurrentItem() + 1);
                                }
                            }else {
                                Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onHandleError(User user) {

                        }
                    });
                }
            }
        });
    }
}
