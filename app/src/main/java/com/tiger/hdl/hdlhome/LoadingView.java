package com.tiger.hdl.hdlhome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

public class LoadingView extends ProgressDialog {
    Activity mActivity;

    public LoadingView(Activity context) {
        super(context);
        mActivity = context;
    }

    public LoadingView(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.loading_view);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() { // 显示Dialog
        super.show();
    }

    @Override
    public void dismiss() { // 关闭Dialog
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.onBackPressed();
        } else
            super.onBackPressed();
    }

}