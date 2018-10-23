package com.jone.gwgfproject.Configs;

import android.text.TextUtils;


import com.jone.gwgfproject.NetApi.BaseURL;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存H5加载的URL路径
 */

public class H5Urls {
    public static String configUrl= BaseURL.URL_LOGIN+"/ABC.plist";//所有网络plist配置参数地址 https://wsfp.cytx360.com/Chinameng.plist
    public static String CustomerUrl ;//客户管理tab
    public static String approvalUrl ;//审批tab
    public static String userUrl;//个人中心,补充信息的url
    public static String messagesUrl;//首页用户信息的url
    public static boolean isShareFriend =true;//是否可分享到朋友圈

    public static List<String> marketingTitles =new ArrayList<>();
    public static List<String> marketingUrls =new ArrayList<>();
    public static List<String> financialTitles =new ArrayList<>();
    public static List<String> financialUrls =new ArrayList<>();

    public static String getCustomerUrl() {
        return TextUtils.isEmpty(CustomerUrl)? LocalH5Urls.CustomerUrl:CustomerUrl;
    }

    public static String getApprovalUrl() {
        return TextUtils.isEmpty(approvalUrl)? LocalH5Urls.approvalUrl:approvalUrl;
    }

    public static String getUserUrl() {
        return TextUtils.isEmpty(userUrl)? LocalH5Urls.userUrl:userUrl;
    }

    public static String getMessagesUrl() {
        return TextUtils.isEmpty(messagesUrl)? LocalH5Urls.messagesUrl:messagesUrl;
    }

    public static List<String> getMarketingTitles() {
        return marketingTitles.size()==0? LocalH5Urls.marketingTitles:marketingTitles;
    }

    public static List<String> getMarketingUrls() {
        return marketingUrls.size()==0? LocalH5Urls.marketingUrls:marketingUrls;
    }

    public static List<String> getFinancialTitles() {
        return financialTitles.size()==0? LocalH5Urls.financialTitles:financialTitles;
    }

    public static List<String> getFinancialUrls() {
        return financialUrls.size()==0? LocalH5Urls.financialUrls:financialUrls;
    }
    public static boolean getIsShareFriend(){
        return isShareFriend;
    }
}
