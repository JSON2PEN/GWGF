package com.json.basewebview.Base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.json.basewebview.Utils.RxBus.RxBus;

/**
 * 基类activity
 * @author jhj
 * @date 2018.10.15
 */

public abstract class BaseAct extends FragmentActivity{
    private boolean shouldRegister =false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除窗口背景
        if (getIsDropBG()){
            this.getWindow().getDecorView().setBackground(null);
        }
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getResID());
        this.shouldRegister=getIsRegister();
        if (shouldRegister){
            RxBus.get().register(this);
        }
        ActManger.getInstance().pushAct(this);
        init();
    }
    protected  abstract int getResID();
    protected  abstract boolean getIsDropBG();
    protected abstract boolean getIsRegister();
    protected  abstract void init();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shouldRegister){
            RxBus.get().unRegister(this);
        }
        ActManger.getInstance().removeAct(this);
    }
}
