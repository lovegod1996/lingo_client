package com.lovegod.newbuy.view.myinfo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.utils.system.SpUtils;


/**
 * Created by Administrator on 2017/7/13.
 */

public class ChangePhoneNumberFragment extends Fragment {
    private User user;
    private TextInputLayout phoneInputLayout;
    private TextInputEditText phoneText;
    private Button nextStepButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frgment_change_phone_passwd,container,false);
        phoneText=(TextInputEditText)view.findViewById(R.id.change_phone_edit);
        nextStepButton=(Button) view.findViewById(R.id.change_phone_next_step);
        phoneInputLayout=(TextInputLayout)view.findViewById(R.id.change_phone_edit_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone=phoneText.getText().toString();
                //检测是否为空
                if(TextUtils.isEmpty(phone)){
                    phoneText.setError("请填写新手机号");
                    return;
                }else if(!RegexUtil.isElevenNumber(phone)){
                    phoneText.setError("请填写正确手机号");//检测是否为11位数字
                    return;
                }else {
                    NetWorks.changePhoneNumber(user.getUid(), phone, new BaseObserver<User>(getActivity(),new ProgressDialog(getActivity())) {//提交更新请求
                        @Override
                        public void onHandleSuccess(User mUser) {
                            user.setPhone(phone);
                            SpUtils.putObject(getActivity(),"userinfo",user);
                            getActivity().finish();//请求成功
                        }

                        @Override
                        public void onHandleError(User user) {
                            phoneText.setError("手机号已被注册");//请求失败，存在重复手机号
                        }
                    });
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void init(){
        phoneInputLayout.setPasswordVisibilityToggleEnabled(false);
        phoneInputLayout.setHint("新手机号");
        phoneText.setInputType(InputType.TYPE_CLASS_NUMBER);
        nextStepButton.setText(R.string.commit);
        user= (User) SpUtils.getObject(getActivity(),"userinfo");
    }
}
