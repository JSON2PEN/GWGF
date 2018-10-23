package com.json.basewebview.NetFrame.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.json.basewebview.R;

/**
 * Created by user on 2018/5/9.
 */

public class DialogLoading extends Dialog{
    private Context mCtx;
    public DialogLoading(@NonNull Context context) {
        super(context,R.style.hintDialogStyle);
        this.mCtx =context;
    }

    public DialogLoading(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mCtx =context;
    }

    public DialogLoading(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mCtx =context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.view_loading);
        // 获取到宽度和高度后，可用于计算
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) (dm.widthPixels * 0.8);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(params);
    }
}
