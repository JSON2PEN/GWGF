package com.json.basewebview.Base.Bind.Base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;


/**
 * 是没有默认布局的Fragment
 */
public abstract class BaseFragment<SV extends ViewDataBinding> extends RxFragment {
    //友盟标识
    private String pageTag = "";
    // 布局view
    protected SV bV;
    // fragment是否显示了
    protected boolean mIsVisible = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bV = DataBindingUtil.inflate(getActivity().getLayoutInflater(), setContent(), null, false);
        this.pageTag=setPageTag();
        return bV.getRoot();
    }
    /**
     * 在这里实现Fragment数据的缓加载.(只在viewpager中有用)
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {
    }

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected void loadVisiData() {
    }

    protected void onVisible() {
        loadVisiData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    /**
     * 布局
     */
    public abstract int setContent();

    protected abstract void initData();

    protected <T extends View> T getView(int id) {
        return (T) getView().findViewById(id);
    }

    /**
     * 友盟统计
     */
    public abstract String setPageTag();
    @Override
    public void onResume() {
        //设置友盟统计的页面头
        this.pageTag =setPageTag();
        super.onResume();
        MobclickAgent.onPageStart(pageTag); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(pageTag);
    }
}
