package com.json.basewebview.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.json.basewebview.AppHelper;

/**
 * 普通封装的toast
 * @author jhj
 */
public class MyToast {
    public static Toast mToast;

    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(AppHelper.mContext, msg, Toast.LENGTH_SHORT);
        } else {
            if (!TextUtils.isEmpty(msg)) {
                mToast.setText(msg);
            }else {
                return;
            }
        }
        mToast.show();
    }

    public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(AppHelper.mContext, msg, Toast.LENGTH_SHORT);
        } else {
            if (!TextUtils.isEmpty(msg)) {
                mToast.setText(msg);
            }else {
                return;
            }
        }
        mToast.show();
    }

    public static void showToast(Context context, String msg, int showTime) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, showTime);
        } else {
            if (!TextUtils.isEmpty(msg)) {
                mToast.setText(msg);
            }else {
                return;
            }
        }
        mToast.show();
    }
}
