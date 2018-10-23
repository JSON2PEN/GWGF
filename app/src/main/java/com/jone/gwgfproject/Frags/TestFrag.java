package com.jone.gwgfproject.Frags;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jone.gwgfproject.R;

/**
 * Created by user on 2018/10/16.
 */

public abstract class TestFrag extends Fragment {

    public View mRootView;
    public FrameLayout mFlTitle;
    public FrameLayout mFlFragContent;
    protected Activity mParentAct;

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
        mRootView = inflater.inflate(R.layout.frag_own,container,false);
        mFlTitle = mRootView.findViewById(R.id.fl_title);
        mFlFragContent = mRootView.findViewById(R.id.fl_frag_content);
        return mRootView;
    }

    /**
     * 可以完成与宿主activity的ui交互逻辑
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }
    public abstract void init();
}
