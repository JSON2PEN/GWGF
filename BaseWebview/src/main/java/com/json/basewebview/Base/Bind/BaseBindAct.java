package com.json.basewebview.Base.Bind;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
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
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.umeng.analytics.MobclickAgent;


/**
 * mvvm 的databinding的基类activity
 */
public abstract class BaseBindAct<SV extends ViewDataBinding> extends RxFragmentActivity {
    //友盟标识
    private String pageTag = "";
    // 布局view
    protected SV bv;
    private LinearLayout llProgressBar;
    private View refresh;
    private ActBindBaseBinding mBaseBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResID());
        getWindow().setBackgroundDrawable(null);
        if (showTitle()){
            initTitle();
        }
        init();
        this.pageTag = setPageTag();
    }

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
                    finish();
                }
            });
        }
    }

    protected abstract boolean showTitle();

    protected abstract int getResID();

    protected abstract void init();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.act_bind_base, null, false);
        bv = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        // content
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bv.getRoot().setLayoutParams(params);
        mBaseBinding.flContent.addView(bv.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());
    }

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
     * 失败后点击刷新
     */
    public void onErrorRefresh(){}

    /**
     * 字体不受系统设置影响
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    //设置标题
    public void setActTitle(String title) {
        if (tvTitle!=null){
            tvTitle.setText(title);
        }
    }

    /**
     * 友盟统计
     */
    public abstract String setPageTag();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(pageTag);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(pageTag);
        MobclickAgent.onPause(this);
    }

    /**
     * Bundle  传递数据
     *
     * @param extras
     */
    protected void getBundleExtras(Bundle extras) {
    }

    /**
     * Bundle保存activity实例
     */
    protected void initBundle(android.os.Bundle bundle) {
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseBindAct.this, clz));
    }

    /**
     * 携带数据页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult
     *
     * @param clazz       目标Activity
     * @param requestCode 发送判断值
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }
    public void setTitleRight(@DrawableRes int rightImg,RightClickBack rightClickBack){
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
