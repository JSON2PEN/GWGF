package com.json.basewebview.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 沉浸式状态栏工具类
 */

public class SteepStatusBarUtils {
    /**
     * 代码这是状态栏透明,并使头部距顶部一个状态栏的高度
     * @param view 头布局
     * @param mAct 当前activity
     */
    public static void setImmerseLayout(View view, Activity mAct) {// view为标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mAct.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(mAct);
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
