package com.json.basewebview.Web.NoTitle;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.webkit.WebView;

import com.json.basewebview.R;
import com.json.basewebview.Utils.UpdateUI;
import com.json.basewebview.Web.Base.BaseWebFrag;
import com.json.basewebview.bean.ShareBean;


/**
 * 新版抽离出来的嵌套不带头部的基类fragment
 */

public class NoTitleWebFrag extends BaseWebFrag<JhjInterface>implements JhjInterface.JsCallBack {
    private String jsName;
    private String loadUrl;
    private JhjInterface mNoTitleJsInterface;
    public View mTitleView;
    //需要拼接的参数
    public static String SpliceParam = "SpliceParam";


    @Override
    public JhjInterface getJsInterface() {
        return mNoTitleJsInterface;
    }
    @Override
    public String getJsName() {
        return jsName;
    }

    @Override
    public String setWebUrl() {
        return loadUrl;
    }

    @Override
    public boolean beforeBack() {
        return false;
    }

    @Override
    protected void initExtraView() {}
    /**
     * 头布局的手动添加,不能在创建时使用
     * @param title
     */
    public void replaceTitle(View title) {
        if (mTitleView != null) {
            mFlTitle.removeView(mTitleView);
        }
        mFlTitle.addView(title);
        mTitleView = title;
    }


    public void setParams(String loadUrl) {
        this.jsName = "gdabc";
        this.loadUrl = loadUrl;
        this.mNoTitleJsInterface = new JhjInterface(this, this);
    }


    @Override
    public void isMain(boolean isMain) {}

    @Override
    public void beforeLoad(WebView view, String url) {
        resetBG();
    }

    @Override
    public void successsBack() {
        resetBG();
    }

    @Override
    public String setUserId() {
        return null;
    }

    @Override
    public String setMode() {
        return null;
    }

    @Override
    public void loadJsCode(String jsCode) {
        UpdateUI.doOnUI(() -> mWebView.loadUrl("javascript:" + jsCode));
    }

    @Override
    public void doShare(ShareBean shareBean) {}

    @Override
    public void login() {}

    /**
     * 有头布局的需要重写的方法
     * @param color
     */
    @Override
    public void setTitleBg(String color) {}

    /**
     * 设置头内容
     * @param title
     */
    @Override
    public void setWebTitle(String title) {}

    /**
     * title右边的图片
     * @param buttonType
     * @param buttonClickFunction
     */
    @Override
    public void showButton(int buttonType, String buttonClickFunction) {}

    /**
     * 重新设置原来的头部背景
     */
    public void resetBG(){}


}
