package com.json.basewebview.Base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.json.basewebview.Utils.RxBus.RxBus;

/**
 * Created by user on 2018/10/16.
 */

public abstract class BaseFrag extends Fragment {

    public View mRootView;
    protected Activity mParentAct;
    private boolean shouldRegister =false;

    /**
     * 初始化fragment的UI,findview等
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentAct=getActivity();
        mRootView = inflater.inflate(getResID(),container,false);
        this.shouldRegister=getIsRegister();
        if (shouldRegister){
            RxBus.get().register(this);
        }
        return mRootView;
    }
    protected  abstract int getResID();
    protected abstract boolean getIsRegister();
    public abstract void init();

    /**
     * 可以完成与宿主activity的ui交互逻辑
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (shouldRegister){
            RxBus.get().unRegister(this);
        }
    }
}
