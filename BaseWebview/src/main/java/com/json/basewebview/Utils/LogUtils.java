package com.json.basewebview.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.json.basewebview.AppHelper;

/**
 * desc：日志输出工具类
 *  debug 可以输出日志,relese 不输出日志
 * @author jhj
 * @date 2018.08.31
 */
public class LogUtils {
    private static Boolean isDebug = null;
    private static String TAG = "JHJ";

    /**
     * 设置全局TAG变量
     * @param newTag
     */
    public static void setTAG(String newTag) {
        LogUtils.TAG = newTag;
    }

    public static void e(String msg) {
        if (getState()) {
            e(TAG, msg);
        }
    }
    public static void e(String tag,String msg){
        Log.e(tag,msg);
    }

    public static void d(String msg) {
        if (getState()) {
            d(TAG, msg);
        }
    }
    public static void d(String tag,String msg){
        Log.d(tag,msg);
    }

    public static void w(String msg) {
        if (getState()) {
            w(TAG, msg);
        }
    }
    public static void w(String tag,String msg){
        Log.w(tag,msg);
    }
    public static void wtf(String msg) {
        if (getState()) {
            wtf(TAG, msg);
        }
    }
    public static void wtf(String tag,String msg){
        Log.wtf(tag,msg);
    }

    public static void i(String msg) {
        if (getState()) {
            i(TAG, msg);
        }
    }
    public static void i(String tag,String msg){
        Log.i(tag,msg);
    }

    /**
     * 获取本地保存的状态
     *
     * @return
     */
    private static boolean getState() {
        if (isDebug == null) {
            return isDebug = isDebug(AppHelper.mContext);
        }
        return isDebug.booleanValue();
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


}