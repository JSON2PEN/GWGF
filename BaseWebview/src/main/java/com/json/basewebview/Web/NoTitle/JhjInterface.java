package com.json.basewebview.Web.NoTitle;

import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.json.basewebview.Utils.SystemUtils;
import com.json.basewebview.Utils.UpdateUI;
import com.json.basewebview.Web.Base.BaseJsInterface;
import com.json.basewebview.bean.ShareBean;
import com.json.basewebview.bean.UserTagBean;

/**
 * Created by user on 2018/10/17.
 */

public class JhjInterface extends BaseJsInterface {

    protected NoTitleWebFrag rootFragment;
    private JsCallBack mCallBack;

    public JhjInterface(NoTitleWebFrag baseWebFragment, JsCallBack mCallBack) {
        super(baseWebFragment);
        this.rootFragment = baseWebFragment;
        this.mCallBack = mCallBack;
    }

    private String deviceId;
    private UserTagBean userTagBean;

    @JavascriptInterface
    public void setPageTitle(String title) {//没有走的方法
        mCallBack.setWebTitle(title);
    }

    @JavascriptInterface
    public void getUserInfo(String jsCode) {
        if (deviceId == null) {
            deviceId = SystemUtils.getDeviceID(rootFragment.mActivity);
        }
        userTagBean = new UserTagBean(deviceId, mCallBack.setUserId(), mCallBack.setMode());
        String s = new Gson().toJson(userTagBean);
        s = s.replace("\"", "\\" + "\"");
        s = jsCode + "(\"" + s + "\")";
        mCallBack.loadJsCode(s);
    }

    @JavascriptInterface
    public void setmode(String mode) {
    }

    @JavascriptInterface
    public void setUserId(String date, String userid, String md5Str) {
    }


    @JavascriptInterface
    public void share(String iconUrl, String title, String content, String url, String titleFriend) {
        ShareBean shareBean = new ShareBean(iconUrl, title, content, url, titleFriend);
        mCallBack.doShare(shareBean);
    }

    @JavascriptInterface
    public void setIsFavorite(int careTag) {//1关注 0未关注
    }

    @JavascriptInterface
    public void setFucntionButton(int buttonType, String buttonClickFunction) {// int类型      0=不显示任何按钮，1=分享按钮，2=菜单按钮，3=搜索按钮
        // @buttonClickFunction 字符串类型   按钮被点击时，需要执行的JS代码】
        mCallBack.showButton(buttonType, buttonClickFunction);
    }


    @JavascriptInterface
    public void setCalendarTrigger(String flag) {//0 -- 禁止滑动 1 --可以滑动
        if ("0".equals(flag + "")) {
            rootFragment.refreshLayout.setIntercept(false);
        } else if ("1".equals(flag + "")) {
            rootFragment.refreshLayout.setIntercept(true);
        }
    }

    @JavascriptInterface
    public void login() {
        mCallBack.login();
    }

    //给服务端返回 基金账户刷新URL
    @JavascriptInterface
    public void getFundAccountInfo(String jsMethod) {//返回形如{"AccountNotifyUrl":"","FundTradingAccount",""}
    }

    //保存服务器返回的基金交易账户
    @JavascriptInterface
    public void saveFundTradingAccount(String fundTradingAccount) {
    }

    /**
     * 执行webview返回
     */
    @JavascriptInterface
    public void clickBackBtn() {
        UpdateUI.doOnUI(() -> {
            rootFragment.webBack();
        });
    }

    @JavascriptInterface
    public void getAppVersion(String jsCode) {
    }

    @JavascriptInterface
    public void setTitleBG(String bgColor) {
        mCallBack.setTitleBg(bgColor);
    }

    public interface JsCallBack {
        void setWebTitle(String title);

        String setUserId();

        String setMode();

        void loadJsCode(String jsCode);

        void doShare(ShareBean shareBean);

        void showButton(int buttonType, String buttonClickFunction);

        void login();

        void setTitleBg(String color);
    }
}


