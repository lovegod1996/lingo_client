package com.lovegod.newbuy.view.registered;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.net.NetWorkUtil;
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.utils.view.ButtonUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ywx on 2017/7/10.
 * 填写手机号的Fragment
 */

public class PhoneRegisteredFragment extends Fragment {
    private static final String TAG="PhoneRegisteredFragment";
    private RegisteredActivity activity;
    private TextInputEditText registeredPhoneEdit;
    private Button nextStepButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_phone_registered,container,false);
        registeredPhoneEdit=(TextInputEditText)view.findViewById(R.id.registered_phone_number);
        nextStepButton=(Button)view.findViewById(R.id.phone_nextstep_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity= (RegisteredActivity) getActivity();
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=registeredPhoneEdit.getText().toString();
                if(TextUtils.isEmpty(phoneNumber)){
                   setError("手机号为空");
                }else if(!RegexUtil.isElevenNumber(phoneNumber)){
                    setError("手机号不正确");
                }else if(RegexUtil.isElevenNumber(phoneNumber)){
                    checkPhoneNumberExist(phoneNumber);
                }
            }
        });
    }

    private void checkPhoneNumberExist(final String phoneNumber) {
        NetWorks.getPhoneInfo(phoneNumber, new BaseObserver<User>(getActivity(),new ProgressDialog(getActivity())) {
            @Override
            public void onHandleSuccess(User user) {
                if (user == null) {
                    if(!ButtonUtils.isFastDoubleClick(R.id.phone_nextstep_button)) {
                        activity.getUser().setPhone(phoneNumber);
                        Log.d(TAG,activity.getUser().toString());
                        activity.setPage(activity.getCurrentitem() + 1);
                    }
                } else {
                    setError("手机号已被注册");
                }
            }

            @Override
            public void onHandleError(User user) {

            }
        });
    }
    private void setError(String error) {
        registeredPhoneEdit.setError(error);
    }
}
