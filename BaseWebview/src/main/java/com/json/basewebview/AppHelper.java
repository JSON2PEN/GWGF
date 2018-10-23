package com.json.basewebview;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.view.WindowManager;

import com.json.basewebview.NetFrame.NetStatusReceiver.NetworkStateReceiver;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;

/**
 * application的帮助类
 */

public class AppHelper {
    public static Context mContext;
    public static boolean isEnablaWifi;
    public static boolean isWifi;
    public static boolean isMobile;
    public static boolean isConnected;
    private static NetworkStateReceiver networkStateReceiver;
    public static void init(Context mContext) {
        AppHelper.mContext = mContext;
        //初始化友盟统计
        UMConfigure.init(mContext, "5bc5abf8b465f53154000156", "UM",UMConfigure.DEVICE_TYPE_PHONE, null);
        //极光推送
        JPushInterface.init(mContext);
        registerNetworkState();
        //设置刷新控件的默认刷新样式
        TwinklingRefreshLayout.setDefaultHeader(ProgressLayout.class.getName());
        TwinklingRefreshLayout.setDefaultFooter(LoadingView.class.getName());
    }

    /**
     * 判断当前进程是不是主进程
     * @param mContext 上下文
     * @param pid 当前进程的pid
     * @return
     */
    public static boolean isMainProcess(Context mContext,int pid) {
        String processNameString = "";
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processNameString = appProcess.processName;
            }
        }
        if (mContext.getPackageName().equals(processNameString)) {
            return true;
        }
        return false;
    }

    /**
     * 注册网络监听
     */

    private static void registerNetworkState() {
        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mContext.registerReceiver(networkStateReceiver, filter);
    }

    /**
     * WiFi状态
     *
     * @param isEnable
     */
    public static void setEnablaWifi(boolean isEnable) {
        AppHelper.isEnablaWifi = isEnable;
    }

    /**
     * 连接的是否是WiFi
     *
     * @param isWifi
     */
    public static void setWifi(boolean isWifi) {
        AppHelper.isWifi = isWifi;
    }

    /**
     * 连接的是否是移动数据
     *
     * @param isMobile
     */
    public static void setMobile(boolean isMobile) {
        AppHelper.isMobile = isMobile;
    }

    /**
     * 网络是否连接
     *
     * @param isConnected
     */
    public static void setConnected(boolean isConnected) {
        AppHelper.isConnected = isConnected;
        if (!isConnected){
            AppHelper.isWifi =false;
            AppHelper.isMobile=false;
        }

    }

    public static boolean isConnected() {
        return isWifi || isMobile;
    }

    public static boolean isMobile() {
        return isMobile;
    }

    public static boolean isWifi() {
        return isWifi;
    }

    /**
     * WiFi是否打开
     *
     * @return
     */
    public static  boolean isEnablaWifi() {
        return isEnablaWifi;
    }
}
