package com.lovegod.newbuy.view.registered;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.utils.regex.RegexUtil;
import com.lovegod.newbuy.utils.view.ButtonUtils;

/**
 * Created by Administrator on 2017/7/10.
 */

public class IdentityRegisteredFragment extends Fragment {
    private static final String TAG ="IdentityRegisteredFragment" ;
    private RegisteredActivity activity;
    private boolean nameFlag=true,identityFlag=true;
    private TextInputEditText identityEdit,nameEdit;
    private Button nextStepButton;
    private TextView skipView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_identity_registered,container,false);
        identityEdit=(TextInputEditText)view.findViewById(R.id.registered_id_number);
        nameEdit=(TextInputEditText) view.findViewById(R.id.registered_name_number);
        nextStepButton=(Button)view.findViewById(R.id.identity_nextstep_button);
        skipView=(TextView)view.findViewById(R.id.skip_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity= (RegisteredActivity) getActivity();
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEdit.getText().toString();
                String identityNumber=identityEdit.getText().toString();
                if(TextUtils.isEmpty(name)){
                    setNameError("请填写姓名");
                    nameFlag=false;
                }else if(!RegexUtil.isName(name)){
                    setNameError("请正确填写姓名");
                    nameFlag=false;
                }else {
                    nameFlag=true;
                }
                if(TextUtils.isEmpty(identityNumber)){
                    setIdentityError("请填写身份证号");
                    identityFlag=false;
                }else if(!RegexUtil.isIdentityNumber(identityNumber)){
                    setIdentityError("请正确填写身份证号");
                    identityFlag=false;
                }else {
                    identityFlag=true;
                }
                if(nameFlag&&identityFlag){
                    if(!ButtonUtils.isFastDoubleClick(R.id.identity_nextstep_button)) {
                        activity.getUser().setIdnumber(identityNumber);
                        activity.getUser().setRealname(name);
                        Log.d(TAG,activity.getUser().toString());
                        activity.setPage(activity.getCurrentitem() + 1);
                    }
                }
            }
        });
        skipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ButtonUtils.isFastDoubleClick(R.id.skip_view)) {
                    activity.setPage(activity.getCurrentitem() + 1);
                }
            }
        });
    }

    private void setNameError(String error) {
        nameEdit.setError(error);
    }
    private void setIdentityError(String error){
        identityEdit.setError(error);
    }
}
