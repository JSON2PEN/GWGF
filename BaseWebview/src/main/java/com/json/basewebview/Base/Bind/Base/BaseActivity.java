package com.json.basewebview.Base.Bind.Base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;


import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.umeng.analytics.MobclickAgent;


/**
 * mvvm 的databinding的基类activity,没有任何公共布局的activity
 */
public abstract class BaseActivity<SV extends ViewDataBinding> extends RxFragmentActivity {
    //友盟标识
    private String pageTag = "";
    // 布局view
    protected SV bV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResID());
        getWindow().setBackgroundDrawable(null);//去除window默认背景,执行在setcontentview之后
        initView();
        initData();
        //设置友盟标识
        this.pageTag = setPageTag();
    }


    protected abstract int getResID();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        bV = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        getWindow().setContentView(bV.getRoot());

    }

    protected abstract void initView();

    protected abstract void initData();

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

    public abstract String setPageTag();

    /**
     * 友盟统计
     */
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
    protected void initBundle(android.os.Bundle bundle){
    }
    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
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

}
