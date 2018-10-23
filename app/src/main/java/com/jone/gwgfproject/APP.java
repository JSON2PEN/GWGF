package com.jone.gwgfproject;

import android.app.Application;
import android.content.Context;

import com.json.basewebview.AppHelper;
import com.mob.MobSDK;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by user on 2018/10/15.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (AppHelper.isMainProcess(this,android.os.Process.myPid())){
            //shareSDK初始化
            MobSDK.init(this);
            AppHelper.init(this);
        }
    }
}
