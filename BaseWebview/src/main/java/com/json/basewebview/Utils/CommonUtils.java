package com.json.basewebview.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 一些公共的工具类:
 * 1.双击返回
 */

public class CommonUtils {
    //双击返回
    private static long firstTime = 0L;

    public static boolean doubleBack() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 1000) {
            MyToast.showToast("再按一次退出");
            firstTime = secondTime;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取版本号名称
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean canUpdate(Context context, String netVersion) {
        String currentVersion = getVersionName(context);
        if (!TextUtils.isEmpty(currentVersion) && !TextUtils.isEmpty(netVersion)) {
            String[] netVisition = netVersion.split("\\.");
            String[] localVisition = currentVersion.split("\\.");
            if (netVisition.length > 0 && netVisition.length >= localVisition.length) {
                for (int i = 0; i < localVisition.length; i++) {
                    int netNum = Integer.parseInt(netVisition[i]);
                    int localNum = Integer.parseInt(localVisition[i]);
                    if (netNum > localNum) {
                        return true;
                    } else if (netNum < localNum) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     *
     */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    private static String hardWareAddress;
    public static String getMachineHardwareAddress() {
        if (!TextUtils.isEmpty(hardWareAddress)){
            return hardWareAddress;
        }
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();

    }
}