package com.json.basewebview.Base.Bind;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.json.basewebview.R;
import com.json.basewebview.Utils.Listener.PerfectClickListener;
import com.json.basewebview.databinding.ActBindBaseBinding;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;


/**
 * 是没有title的Fragment
 */
public abstract class BaseBindFrag<SV extends ViewDataBinding> extends RxFragment {
    //友盟标识
    private String pageTag = "";
    // 布局view
    protected SV bv;
    // fragment是否显示了
    protected boolean mIsVisible = false;
    private ActBindBaseBinding mBaseBinding ;
    private LinearLayout llProgressBar;
    private View refresh;
    public ImageView ivTitleRight;
    public ImageView ivBack;
    private ImageView imgErr;
    private TextView tvTitle;
    private TextView tvEmptyDes;
    public View mVStatus;
    public View mVTitle;
    public ImageView mIvRightIcon;
    public FrameLayout mFlRightIcon;
    public FrameLayout mFlBack;
    public FragmentActivity mRootAct;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootAct=getActivity();
        bv = DataBindingUtil.inflate(mRootAct.getLayoutInflater(), setContent(), null, false);
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.act_bind_base, null, true);
        if (showTitle()){
            initTitle();
        }
        this.pageTag=setPageTag();
        mBaseBinding.flContent.addView(bv.getRoot());
        return mBaseBinding.getRoot();
    }

    protected abstract boolean showTitle();


    /**
     * 在这里实现Fragment数据的缓加载.
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
        initTitle();
        init();
    }
    /**
     * 布局
     */
    public abstract int setContent();

    protected abstract void init();

    private void initTitle() {
        if (mVTitle==null){
            mVTitle = mBaseBinding.vsTitle.getViewStub().inflate();
            ivBack =mVTitle.findViewById(R.id.iv_title_left);
            tvTitle = mVTitle.findViewById(R.id.tv_web_title);
            mIvRightIcon = mVTitle.findViewById(R.id.iv_right_icon);
            mFlRightIcon = mVTitle.findViewById(R.id.fl_right_icon);
            mFlBack = mVTitle.findViewById(R.id.fl_back);
            mFlBack.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    mRootAct.finish();
                }
            });
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitleText(String title) {
        tvTitle.setText(title);
    }

    /**
     * 加载失败后点击后的操作
     */
    protected void onErrorRefresh(){};

    /**
     * 显示加载中状态
     */
    protected void showLoading() {
        if (llProgressBar.getVisibility() != View.VISIBLE) {
            llProgressBar.setVisibility(View.VISIBLE);
        }
        if (bv.getRoot().getVisibility() != View.GONE) {
            bv.getRoot().setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成的状态
     */
    protected void showContentView() {
        if (llProgressBar.getVisibility() != View.GONE) {
            llProgressBar.setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
        if (bv.getRoot().getVisibility() != View.VISIBLE) {
            bv.getRoot().setVisibility(View.VISIBLE);
        }
    }

    public void showError() {
        initStutasView();
        imgErr.setImageResource(R.drawable.pic_no_net);
        tvEmptyDes.setText("加载失败,点击重试");
        showStutasView();
    }

    private void initStutasView() {
        if (mVStatus==null){
            mVStatus = mBaseBinding.vsStatus.getViewStub().inflate();
            llProgressBar = mVStatus.findViewById(R.id.ll_progress_bar);
            refresh = mVStatus.findViewById(R.id.ll_error_refresh);
            imgErr = mVStatus.findViewById(R.id.iv_err);
            tvEmptyDes = mVStatus.findViewById(R.id.tv_empty_des);
        }
    }

    public void showEmpty(String empty) {
        initStutasView();
        imgErr.setImageResource(R.drawable.pic_empty);
        tvEmptyDes.setText(empty);
        showStutasView();
    }
    private void showStutasView(){
        refresh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showLoading();
                onErrorRefresh();
            }
        });
        if (llProgressBar.getVisibility() != View.GONE) {
            llProgressBar.setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.VISIBLE) {
            refresh.setVisibility(View.VISIBLE);
        }
        if (bv.getRoot().getVisibility() != View.GONE) {
            bv.getRoot().setVisibility(View.GONE);
        }
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
    public void setTitleRight(@DrawableRes int rightImg, BaseBindFrag.RightClickBack rightClickBack){
        if (ivTitleRight!=null){
            ivTitleRight.setImageResource(rightImg);
        }
        if (mFlRightIcon!=null){
            mFlRightIcon.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    rightClickBack.onRightClick();
                }
            });
        }
    }
    public void hintBack(){
        if (mFlBack!=null){
            mFlBack.setVisibility(View.GONE);
        }
    }
    /**
     * title右侧点击回调
     */
    public interface RightClickBack{
        void onRightClick();
    }
}
