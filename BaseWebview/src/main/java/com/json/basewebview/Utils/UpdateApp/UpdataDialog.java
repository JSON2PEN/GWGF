package com.json.basewebview.Utils.UpdateApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.json.basewebview.R;


public class UpdataDialog extends Dialog {

    public TextView tvUpdataDes;
    public View vArea;

    public interface HintDialogListener {
        public void onConfirm(UpdataDialog dialog);

        public void onCancel(UpdataDialog dialog);
    }

    ;

    private HintDialogListener listener;

    public UpdataDialog setListener(HintDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public String contentStr = "", titleStr = "", cancelStr = "", confirmStr = "", updataDes = "";
    Context context;
    public Button confirmBtn;// 确定
    public Button cancelBtn;// 取消
    public TextView tv_title;// 提示
    public TextView tv_dialog_content;// 提示内容
    public boolean noShowCancelBtn = true;// 是否显示确定按钮

    public UpdataDialog(Context context, String contentStr) {
        super(context, R.style.hintDialogStyle);
        this.context = context;
        this.contentStr = contentStr;
    }

    public UpdataDialog(String contentStr, Context context, String updataDes) {
        super(context, R.style.hintDialogStyle);
        this.context = context;
        this.contentStr = contentStr;
        this.updataDes =updataDes;
    }

    /**
     * @param context
     * @param contentStr
     * @param confirmStr
     */
    public UpdataDialog(Context context, String contentStr, String confirmStr) {
        super(context, R.style.hintDialogStyle);
        this.context = context;
        this.contentStr = contentStr;
        this.confirmStr = confirmStr;
    }

    /**
     * @param context
     * @param titleStr
     * @param contentStr
     * @param cancelStr
     * @param confirmStr
     * @param noShowCancelBtn
     */
    public UpdataDialog(Context context, String titleStr, String contentStr, String cancelStr, String confirmStr, boolean noShowCancelBtn) {
        super(context, R.style.hintDialogStyle);
        this.context = context;
        this.contentStr = contentStr;
        this.confirmStr = confirmStr;
        this.titleStr = titleStr;
        this.cancelStr = cancelStr;
        this.noShowCancelBtn = noShowCancelBtn;
    }

    public UpdataDialog(Context context, int layout) {
        super(context, R.style.hintDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_updata);

        // 获取到宽度和高度后，可用于计算
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) (dm.widthPixels * 0.8);
        params.height = LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(params);

        // 根据id在布局中找到控件对象
        tv_title = (TextView) findViewById(R.id.tv_title);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        tv_dialog_content = (TextView) findViewById(R.id.tv_dialog_content);
        tvUpdataDes = (TextView) findViewById(R.id.tv_updata_des);
        vArea = findViewById(R.id.v_area);

        tv_dialog_content.setText(contentStr);
        if (TextUtils.isEmpty(updataDes)){
            vArea.setVisibility(View.VISIBLE);
            tvUpdataDes.setVisibility(View.GONE);
        }else {
            tvUpdataDes.setText(updataDes);
        }
//		if (!TextUtils.isEmpty(titleStr)) {
//			tv_title.setText(titleStr);
//		} else {
//			tv_title.setVisibility(View.GONE);
//		}
        if (!TextUtils.isEmpty(cancelStr)) {
            cancelBtn.setText(cancelStr);
        }
        if (!TextUtils.isEmpty(confirmStr)) {
            confirmBtn.setText(confirmStr);
        }
        // 设置按钮的文本颜色


        // 为按钮绑定点击事件监听器
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                listener.onConfirm(UpdataDialog.this);
            }
        });
        if (!noShowCancelBtn) {
            cancelBtn.setVisibility(View.GONE);
        }
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                listener.onCancel(UpdataDialog.this);
            }
        });
    }
}