package com.json.basewebview.Utils.UpdateApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.json.basewebview.R;


/**
 * 下载中的dialog
 */

public class DoUpdateDialog extends Dialog {
    private String title;
    private String sign;
    private Context context;
    public TextView tv_title;
    public TextView tvDesign;
    public UpdateProgressBar upb;

    public DoUpdateDialog(@NonNull Context context) {
        super(context);
        this.context =context;
    }

    public DoUpdateDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.context =context;
    }

    public DoUpdateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable,cancelListener);
        this.context =context;
    }
    public DoUpdateDialog(@NonNull Context context, String title, String design){
        super(context, R.style.hintDialogStyle);
        this.context =context;
        this.title =title;
        this.sign =design;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_download);

        // 获取到宽度和高度后，可用于计算
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) (dm.widthPixels * 0.8);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(params);

        // 根据id在布局中找到控件对象
        tv_title = (TextView) findViewById(R.id.tv_title);
        tvDesign = (TextView) findViewById(R.id.tv_design);
        upb = (UpdateProgressBar) findViewById(R.id.upb);
        if (!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(sign)){
            tvDesign.setText(sign);
        }
    }
    public void setProgressText(int progress){
        if (upb!=null){
            upb.setProgress(progress);
        }
    }
}
