package com.lovegod.newbuy.view.registered;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.utils.view.ButtonUtils;
import com.lovegod.newbuy.view.myview.VerifyView;

/**
 * Created by ywx on 2017/7/10.
 * 填写验证码的Fragment
 */

public class VerifyRegisteredFragment extends Fragment {
    private VerifyView verifyView;
    private Button nextStepButton;
    private TextInputEditText verifyInputEdit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_verify_registered,container,false);
        verifyView=(VerifyView)view.findViewById(R.id.verify_view);
        nextStepButton=(Button)view.findViewById(R.id.verify_nextstep_button);
        verifyInputEdit=(TextInputEditText)view.findViewById(R.id.verify_input_edit);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final RegisteredActivity activity=(RegisteredActivity)getActivity();
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifyText=verifyInputEdit.getText().toString();
                if(verifyText.equals(verifyView.getVerifyArray())){
                    if(!ButtonUtils.isFastDoubleClick(R.id.verify_nextstep_button)) {
                        activity.setPage(activity.getCurrentitem() + 1);
                    }
                }else{
                    verifyInputEdit.setError("验证码错误");
                }
            }
        });
    }
}
