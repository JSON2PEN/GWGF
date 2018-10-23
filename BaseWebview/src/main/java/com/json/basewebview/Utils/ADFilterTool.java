package com.json.basewebview.Utils;

import android.content.Context;
import android.content.res.Resources;

import com.json.basewebview.R;


/**
 * 判断是不是广告的工具类
 */

public class ADFilterTool {
    private static String[] adUrls;
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        if (adUrls==null)
        adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }
}
