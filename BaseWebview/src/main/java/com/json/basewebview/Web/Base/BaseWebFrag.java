package com.json.basewebview.Web.Base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.json.basewebview.R;
import com.json.basewebview.Utils.ADFilterTool;
import com.json.basewebview.Utils.UICallBack.UpdateBack;
import com.json.basewebview.Utils.UpdateUI;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;


/**
 * 不带title的webview模板,封装的是返回的所有逻辑
 */

public abstract class BaseWebFrag<T extends BaseJsInterface> extends RxFragment {

    private View rootView;//跟布局
    public WebView mWebView;//webview
    private ViewStub vsError;//加载失败的布局
    public TwinklingRefreshLayout refreshLayout;//粘性滑动的控件
    private ImageView ivRefresh;//刷新图标
    private FrameLayout flRefresh;//刷新布局
    private View imView;//加载失败的布局
    private T jsInterface;//jsInterface 对象
    public Activity mActivity;


    private String inMethod;//进入的方法名,作为判断是否为首页的依据
    public boolean firstIn = true;//是否是首次进入应用,用来获取首次进入的url,用来判断是否是首页
    public String currentUrlMethod;//当前url的方法名
    private boolean isLoadFail = false;//判断是否联网失败
    private boolean backClick = false;//用户判断是 点击返回  后续执行刷新或者返回执行JS语句
    public BaseUrlBean urlBean;//保存url堆栈消息的model
    public HashMap<String, BaseUrlBean> urlModelMap = new HashMap();//保存URL对象的
    public boolean isMain = true;//判断是否为首页的标记
    private int bgColor = -1;//webview背景色
    private int bgFragmentColor = -1;//根布局的背景色
    private boolean needClearHistory;//是否需要清空webview堆栈
    protected boolean isCrossSliding = true;//是否能越界滑动,默认能
    protected String mixParam;//URL需要拼接的参数
    private boolean isNeedMix = false;//用于判断是否需要拼接参数
    public BaseUrlBean backToUrlBean;
    public String addMixTag;//用于URL判断是否为需要添加Mix的Url(需要isNeedMix=true时使用)
    private boolean enableRefresh = false;//是否允许刷新,默认为不可以
    public FrameLayout mFlTitle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        rootView = View.inflate(mActivity, R.layout.jhj_frag_web_base, null);
        initView();
        return rootView;
    }


    private void initView() {
        mFlTitle = findView(R.id.fl_title);
        initExtraView();
        mWebView = findView(R.id.mWebView);
        vsError = findView(R.id.vs_error);
        refreshLayout = findView(R.id.refreshLayout);
        if (enableRefresh) {
            refreshLayout.setEnableLoadmore(false);
            refreshLayout.setHeaderView(new ProgressLayout(mActivity));
            refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
                @Override
                public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                    if (mWebView != null) {
                        mWebView.reload();
                    }
//                    new Handler().postDelayed(() -> refreshLayout.finishRefreshing(),1000);
                }
            });
        } else {
            refreshLayout.setPureScrollModeOn();
        }
        ivRefresh = findView(R.id.iv_refresh);
        flRefresh = findView(R.id.fl_refresh);
        if (bgColor != -1)
            mWebView.setBackgroundColor(getResources().getColor(bgColor));
        if (bgFragmentColor != -1)
            refreshLayout.setBackgroundColor(getResources().getColor(bgFragmentColor));
        if (!isCrossSliding) {
            refreshLayout.setIntercept(false);
        }
        Glide.with(getActivity()).load(R.drawable.pic_default_rf).asGif().into(ivRefresh);
        initWebView();
    }

    protected abstract void initExtraView();

    private void initWebView() {
        jsInterface = (T) getJsInterface();
        jsInterface.setUrlModelMap(urlModelMap);
        mWebView.addJavascriptInterface(jsInterface, getJsName());
        mWebView.setWebChromeClient(new WebChromeClient());//弹窗相关的操作
        mWebView.requestFocus();

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(true);//保存表单数据
        webSettings.setJavaScriptEnabled(true);
        //设置加载进来的页面自适应手机屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                //正常显示隐藏原生loading
                if (newProgress == 100) {
                    flRefresh.setVisibility(View.INVISIBLE);
                } else {
                    if (flRefresh.getVisibility() != View.VISIBLE)
                        flRefresh.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public BaseUrlBean finishUrlBean;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                beforeLoad(view, url);
                jsInterface.isBackClick = false;
                urlBean = new BaseUrlBean();
                if (behindToUrl != null) {
                    urlBean.backUrl = behindToUrl;
                }
                urlModelMap.put(WebStringUtils.getLastUrlMethodName(url), urlBean);
                isLoadFail = false;
                backClick = false;
                if (isCrossSliding) {
                    //PDF文件禁止滑动
                    if (url.contains(".pdf")) {
                        refreshLayout.setIntercept(false);
                    } else {
                        refreshLayout.setIntercept(true);
                    }
                }
                if (isNeedMix) {
                    if (url.contains(addMixTag)) {
                        if (url.contains("mix=")) {
//                        view.loadUrl(url);
                        } else if (!url.contains("?")) {
                            view.loadUrl(url + "?mix=" + mixParam);
                            return true;
                        } else {
                            view.loadUrl(url + "&mix=" + mixParam);
                            return true;
                        }
                    }
                }
                return false;
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                UpdateUI.doOnUI(new UpdateBack() {
                    @Override
                    public void doInUI() {
                        showEror();
                    }
                });
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                currentUrlMethod = WebStringUtils.getLastUrlMethodName(url);
                startLoad(view, url);
                if (firstIn) {
                    urlBean = new BaseUrlBean();
                    urlModelMap.put(currentUrlMethod, urlBean);
                    inMethod = currentUrlMethod;
                    firstIn = false;
                    return;
                }
                //用来判断首页的机制
                if (WebStringUtils.isAlike(currentUrlMethod, inMethod)) {
                    isMain = true;
                } else {
                    isMain = false;
                }
                isMain(isMain);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (enableRefresh) {
                    refreshLayout.finishRefreshing();
                }
                finishUrlBean = urlModelMap.get(WebStringUtils.getLastUrlMethodName(url));
                finishLoad(view, url);
                if (backClick) {
                    if (finishUrlBean == null)
                        return;
                    if (finishUrlBean.isReload) {//返回重新加载的操作
                        jsInterface.isBackClick = false;
                        UpdateUI.doOnUI(new UpdateBack() {
                            @Override
                            public void doInUI() {
                                mWebView.reload();
                            }
                        });
                        finishUrlBean.isReload = false;
                    }

                    if (!TextUtils.isEmpty(finishUrlBean.pageFinishDoJs)) {//返回页面加载结束执行js方法的操作
                        UpdateUI.doOnUI(new UpdateBack() {
                            @Override
                            public void doInUI() {
                                mWebView.loadUrl("javascript:" + finishUrlBean.pageFinishDoJs);
                                finishUrlBean.pageFinishDoJs = null;
                            }
                        });
                    }
                    backClick = false;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                //接收ssl证书
                handler.proceed();
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                if (needClearHistory) {
                    needClearHistory = false;
                    view.clearHistory();//清除历史记录
                }
            }

            //拦截广告使用
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                url = url.toLowerCase();
                if (!ADFilterTool.hasAd(getActivity(), url)) {
                    return super.shouldInterceptRequest(view, url);//正常加载
                } else {
                    return new WebResourceResponse(null, null, null);//含有广告资源屏蔽请求
                }
            }
        });
//        mWebView.loadUrl(setWebUrl());//Home.aspx
        mWebView.loadUrl(setWebUrl());
//        mWebView.postUrl(setWebUrl(), data.getBytes());
    }

    public void isMain(boolean isMain) {
    }

    /**
     * webview加载结束
     *
     * @param view
     * @param url
     */
    private void finishLoad(WebView view, String url) {
    }

    /**
     * webview加载开始
     *
     * @param view
     * @param url
     */
    public void startLoad(WebView view, String url) {
    }

    /**
     * webview加载之前
     *
     * @param view
     * @param url
     */
    public abstract void beforeLoad(WebView view, String url);


    public abstract T getJsInterface();

    public abstract String getJsName();

    public abstract String setWebUrl();

    protected <T extends View> T findView(int id) {
        return (T) rootView.findViewById(id);
    }

    /**
     * 展示加载失败
     */
    public void showEror() {
        mWebView.removeAllViews();
        if (imView != null) {
            imView.setVisibility(View.VISIBLE);
            return;
        }
        imView = vsError.inflate();
        LinearLayout llRefresh = (LinearLayout) imView.findViewById(R.id.ll_refresh);
        llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imView != null) {
                    imView.setVisibility(View.GONE);
                }
                if (mWebView != null) {
                    mWebView.setVisibility(View.VISIBLE);
                }
                mWebView.reload();
            }
        });
    }

    /**
     * webview返回操作
     */
    private BaseUrlBean backModel;

    public void webBack() {
        if (!canClickBack()) {
            return;
        }
        if (beforeBack()) {
            return;
        }
        if (mWebView != null && mWebView.canGoBack()) {
            webList = mWebView.copyBackForwardList();
            currentIndex = webList.getCurrentIndex();
            //加载失败的返回处理,直接将加载失败的界面隐藏
            if (isLoadFail && imView != null && imView.getVisibility() == View.VISIBLE) {
                imView.setVisibility(View.GONE);
                if (mWebView != null) {
                    mWebView.setVisibility(View.VISIBLE);
                }
                isLoadFail = false;
                return;
            }
            jsInterface.isBackClick = true;
            backModel = urlModelMap.get(currentUrlMethod);
            if (backModel == null) {
                mWebView.goBack();
                cancelDoBehindBackUrl();
                successsBack();
                return;
            }
            if (!backModel.isBackDoJs) {//点击返回时在当前页面执行的js方法
                if (!TextUtils.isEmpty(backModel.backDoJs)) {
                    mWebView.loadUrl("javascript:" + backModel.backDoJs);
                    backModel.backDoJs = null;
                }
            }
            commonBack(backModel);
            backClick = true;
        }
    }

    private void commonBack(BaseUrlBean backModel) {
        cancelDoBehindBackUrl();
        if (backModel.status == 6) {//返回原生上级
            getActivity().finish();
        } else if (backModel.status == 7) {//开启原生指定activity
            if (!TextUtils.isEmpty(backModel.nativeVCName)) {
                try {
                    Class activityClass = Class.forName(backModel.nativeVCName);
                    getActivity().startActivity(new Intent(getActivity(), activityClass));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            successsBack();
            if (!TextUtils.isEmpty(backModel.backUrl)) {//返回指定页面
                for (int i = 0; i < currentIndex; i++) {
                    if (webList.getItemAtIndex(i).getUrl().contains(backModel.backUrl)) {
                        if (backModel.isBackDoJs) {
                            backToUrlBean = urlModelMap.get(backModel.backUrl);
                            backToUrlBean.pageFinishDoJs = backModel.backDoJs;//将返回到的页面model赋值页面完成js
                        }
                        mWebView.goBackOrForward(i - currentIndex);
                        return;
                    }
                }
            } else if (backModel.backCount > 0) {//执行跳过指定数值堆栈的页面(不包含不如堆栈的)
                int currentBackCount = backModel.backCount;
                for (int i = currentIndex - 1; i >= 0; i--) {
                    backToUrlBean = urlModelMap.get(WebStringUtils.getLastUrlMethodName(webList.getItemAtIndex(i).getUrl()));
                    if (backToUrlBean != null) {
                        if (!backToUrlBean.notAddStack) {
                            if (--currentBackCount <= 0) {
                                if (backModel.isBackDoJs) {
                                    backToUrlBean.pageFinishDoJs = backModel.backDoJs;//将返回到的页面model赋值页面完成js
                                }
                                mWebView.goBackOrForward(i - currentIndex);
                                return;
                            }
                        }
                    }
                }
                mWebView.goBackOrForward(-currentIndex);//如果给定的count大于堆栈,返回首页
            } else {//正常的返回,包含跳过不如堆栈的url
                for (int i = currentIndex - 1; i >= 0; i--) {
                    backToUrlBean = urlModelMap.get(WebStringUtils.getLastUrlMethodName(webList.getItemAtIndex(i).getUrl()));
                    if (backToUrlBean != null) {
                        if (!backToUrlBean.notAddStack) {
                            if (backModel.isBackDoJs) {
                                backToUrlBean.pageFinishDoJs = backModel.backDoJs;//将返回到的页面model赋值页面完成js
                            }
                            mWebView.goBackOrForward(i - currentIndex);
                            return;
                        }
                    }
                }
            }
            mWebView.goBack();
        }
    }

    /**
     * 返回true时拦截返回操作
     *
     * @return
     */
    public abstract boolean beforeBack();

    /**
     * 执行返回了
     */
    public abstract void successsBack();

    private WebBackForwardList webList;
    private int currentIndex;

    /**
     * 防止用户暴力返回
     */
    private long exitTime = 0l;

    public boolean canClickBack() {
        if ((System.currentTimeMillis() - exitTime) > 300) {
            exitTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * 记录并且处理后续返回指定的URL
     */
    private String behindToUrl;

    public void doBehindBackUrl(boolean isReload) {
        behindToUrl = currentUrlMethod;
        if (isReload) {
            urlBean.isReload = true;
        }
    }

    //取消后续返回指定URL
    public void cancelDoBehindBackUrl() {
        this.behindToUrl = null;
    }

    /**
     * 设置返回时,跳过含有通配符的URL
     */
    private WebBackForwardList checkwebList;

    public void checkStrJump(final String markUrl) {
        UpdateUI.doOnUI(new UpdateBack() {
            @Override
            public void doInUI() {
                checkwebList = mWebView.copyBackForwardList();
                currentIndex = checkwebList.getCurrentIndex();
                String currentCheckUrl = null;
                BaseUrlBean checkBean = null;
                for (int i = currentIndex; i >= 0; i--) {
                    currentCheckUrl = checkwebList.getItemAtIndex(i).getUrl();
                    if (!currentCheckUrl.contains(markUrl)) {
                        urlBean.backCount = currentIndex - i;
                        return;
                    }

                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releseData();
    }

    /**
     * 设置webview的背景颜色
     *
     * @param bgColor 颜色 R.color.bg_color
     */
    public void setBgColor(@ColorRes int bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * 设置控件滑动的背景颜色
     */
    public void setFragmentBgColor(@ColorRes int bgColor) {
        this.bgFragmentColor = bgColor;
    }

    /**
     * 释放webview资源
     */
    public void releseData() {
        if (jsInterface != null) {
            jsInterface.relese();
        }
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 设置页面是否可以越界滑动
     */
    public void setCrossSliding(boolean isCrossSliding) {
        this.isCrossSliding = isCrossSliding;
    }

    /**
     * 设置需要拼接的mix参数
     *
     * @param mixParam
     */
    public void setMixParam(String mixParam) {
        if (!TextUtils.isEmpty(mixParam)) {
            this.mixParam = mixParam;
            this.isNeedMix = true;
        }
    }

    /**
     * 重新加载页面
     */
    public void reloadUrl() {
        if (mWebView != null) {
            mWebView.reload();
        }
    }

    public void setEnableRefresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }

    /**
     * 重新设置url并加载,用于传入url为null时;
     */
    public void reInitUrl(String newUrl) {
        mWebView.loadUrl(newUrl);
    }
}
