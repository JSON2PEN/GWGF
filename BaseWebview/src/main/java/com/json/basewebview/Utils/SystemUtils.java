package com.json.basewebview.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
/**
 * 获取设备DeviceId
 */
public class SystemUtils {
	@SuppressLint("MissingPermission")
	public static String getDeviceID(Context mContext) {
		String strResult = null;
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			strResult = telephonyManager.getDeviceId();
		}
		if (strResult == null) {
			strResult = Settings.Secure.getString(mContext.getContentResolver(),
					Settings.Secure.ANDROID_ID);
		}
		return strResult;
	}
}
