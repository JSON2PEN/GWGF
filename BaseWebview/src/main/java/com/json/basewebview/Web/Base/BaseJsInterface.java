package com.json.basewebview.Web.Base;

import android.webkit.JavascriptInterface;

import java.util.HashMap;

/**
 * JsInterFace的基类  第二版最新
 */

public class BaseJsInterface {
    private BaseWebFrag baseWebFragment;
    public   boolean isBackClick = false;
    public HashMap<String, BaseUrlBean> urlModelMap;

    public BaseJsInterface(BaseWebFrag baseWebFragment) {
        this.baseWebFragment = baseWebFragment;
    }
    public void setUrlModelMap(HashMap<String,BaseUrlBean> urlModelMap) {
        this.urlModelMap =urlModelMap;
    }

    /**
     * 1,返回指定页面
     *
     * @param isReload 是否刷新
     * @param backUrl  返回的URL
     */
    @JavascriptInterface
    public void setBackUrlReload(boolean isReload, String backUrl) {
        if (isBackClick){
            return;
        }
        if (baseWebFragment.urlBean != null) {
//            baseWebFragment.urlBean.status = 1;
            baseWebFragment.urlBean.backUrl = WebStringUtils.getLastUrlMethodName(backUrl);
            if (urlModelMap!=null){
                BaseUrlBean backToUrlBean =urlModelMap.get(baseWebFragment.urlBean.backUrl);
                if (backToUrlBean!=null){
                    backToUrlBean.isReload =isReload;
                }
            }
        }
        baseWebFragment.cancelDoBehindBackUrl();
    }

    /**
     * 2,跳过指定数量的堆栈
     *
     * @param isReload 是否刷新
     * @param count    跳过的数量(不能大于堆栈数量)
     */
    @JavascriptInterface
    public void setBackCountReload(boolean isReload, int count) {
        if (isBackClick) {
            return;
        }
        if (baseWebFragment.urlBean != null) {
            baseWebFragment.urlBean.backCount = count;
            baseWebFragment.urlBean.isReload = isReload;
        }
        baseWebFragment.cancelDoBehindBackUrl();
    }

    /**
     * 3,指定页面不堆栈
     *
     * @param isAddStack true 不入堆栈
     */
    @JavascriptInterface
    public void setIsAddStack(boolean isAddStack) {
        if (isBackClick) {
            return;
        }
        if (isAddStack) {
            if (baseWebFragment.urlBean != null) {
//                baseWebFragment.urlBean.status = 5;
                baseWebFragment.urlBean.notAddStack = true;
            }
            baseWebFragment.cancelDoBehindBackUrl();
        }
    }

    /**
     * 4,指定后续页面返回特定路径(堆栈中存在的)
     *
     * @param isBehind 后续是否返回本页面
     * @param isReload 是否刷新
     */
    @JavascriptInterface
    public void setBehindReload(boolean isBehind, boolean isReload) {
        if (isBackClick) {
            return;
        }
        if (isBehind) {
            baseWebFragment.doBehindBackUrl(isReload);
        }

    }

    /**
     * 5,设置通配符URL 并设置是否跳过这些页面
     *
     * @param delStr 通配符
     * @param isJump 是否跳过
     */
    @JavascriptInterface
    public void isDelStr(String delStr, boolean isJump) {
        if (isBackClick) {
            return;
        }
        if (isJump) {
            baseWebFragment.checkStrJump(delStr);
            baseWebFragment.cancelDoBehindBackUrl();
        }
    }

    /**
     * 6,返回原生(包含webview的页面)的上级页面
     *
     * @param setIsBackNative 是否返回原生上级页面
     */

    @JavascriptInterface
    public void setIsBackNative(boolean setIsBackNative) {
        if (isBackClick) {
            return;
        }
        if (setIsBackNative && baseWebFragment.urlBean != null) {
            baseWebFragment.urlBean.status = 6;
            baseWebFragment.urlBean.isBackNative = true;
            baseWebFragment.cancelDoBehindBackUrl();
        }
    }

    /**
     * 7,H5页面跳转指定原生页面
     *
     * @param nativeVCName 原生控件(如activity和fragment)的名称
     */
    @JavascriptInterface
    public void setGoNativeName(String nativeVCName) {
        if (isBackClick) {
            return;
        }
        if (baseWebFragment.urlBean != null) {
            baseWebFragment.urlBean.status = 7;
            baseWebFragment.urlBean.nativeVCName = nativeVCName;
            baseWebFragment.cancelDoBehindBackUrl();
        }

    }

    /**
     * 8,返回上一个页面并 执行JS方法
     *
     * @param jsCode js方法和参数
     * @param backDo 是否返回上个页面执行js true为返回上一级页面执行js  false为点击返回当前页面执行
     */

    @JavascriptInterface
    public void setGoBackJS(String jsCode, boolean backDo) {
            if (isBackClick) {
                return;
            }
            if (baseWebFragment.urlBean != null) {
                baseWebFragment.urlBean.backDoJs = jsCode;
                baseWebFragment.urlBean.isBackDoJs=backDo;
//                baseWebFragment.urlBean.status =4;
                baseWebFragment.cancelDoBehindBackUrl();
            }
    }

  /*
     * 9设置当前页面为首页,无返回按钮
     *@param currentFirst true 设置当前页面为首页 false 设置当前页面不是首页

    @JavascriptInterface
    public void setFirstPage(boolean currentFirst){
        if (isBackClick)
            return;
        if (baseWebFragment.urlBean != null) {
            baseWebFragment.urlBean.isFirstPage =currentFirst;
        }
    }*/

    //释放对象
    public void relese() {
        baseWebFragment = null;
    }

}
