package com.lovegod.newbuy.api;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lovegod.newbuy.bean.BaseBean;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by 123 on 2017/4/14.
 */

public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private Context mContext;
    private ProgressDialog mDialog;
    private Disposable mDisposable;
    private final int SUCCESS_CODE = 0;
    private View view;

    public BaseObserver() {
    }

    public BaseObserver(View view) {
        this.view = view;
    }
    public BaseObserver(View view, ProgressDialog dialog) {
        this.view = view;
        this.mDialog=dialog;
         mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mDisposable.dispose();
            }
        });
    }
    public BaseObserver(Context context, ProgressDialog dialog) {
        mContext = context;
        mDialog = dialog;

        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mDisposable.dispose();
            }
        });
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(BaseBean<T> value) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (value.getCode() == SUCCESS_CODE) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getCode(), value.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.d("gesanri", "error:" + e.toString());
        if (mContext != null) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_LONG).show();
        } else if (view != null) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            Snackbar.make(view, "网络异常，请稍后再试", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onComplete() {
        Log.d("gesanri", "onComplete");

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public abstract void onHandleSuccess(T t);

    void onHandleError(int code, String message) {
        if (mContext != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        } else if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        }
    }
}
