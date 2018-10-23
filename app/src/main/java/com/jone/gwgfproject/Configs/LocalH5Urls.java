package com.jone.gwgfproject.Configs;

import com.jone.gwgfproject.NetApi.BaseURL;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地静态保存的urls,在网络没有获取成功时使用
 */

public class LocalH5Urls {
    public static String configUrl= BaseURL.URL_LOGIN+"/ABC.plist";//所有网络plist配置参数地址 https://wsfp.cytx360.com/Chinameng.plist
    public static String CustomerUrl = BaseURL.URL_LOGIN+"/Customer/Customer";//客户管理tab
    public static String approvalUrl =BaseURL.URL_LOGIN+"/Audit/Audit";//审批tab
    public static String userUrl=BaseURL.URL_LOGIN+"/User";//个人中心,补充信息的url
    public static String messagesUrl=BaseURL.URL_LOGIN+"/Information/Message";//首页用户信息的url

    public static List<String> marketingTitles =new ArrayList<>();
    public static List<String> marketingUrls =new ArrayList<>();
    public static List<String> financialTitles =new ArrayList<>();
    public static List<String> financialUrls =new ArrayList<>();
    static {
        marketingTitles.add("定投模拟");
        marketingTitles.add("基金");
        marketingTitles.add("保险");
        marketingTitles.add("理财产品");

        marketingUrls.add(BaseURL.URL_LOGIN+"/Marketing/Fixed");
        marketingUrls.add(BaseURL.URL_LOGIN+"/Marketing/Product/Fund");
        marketingUrls.add(BaseURL.URL_LOGIN+"/Marketing/Product/Insurance");
        marketingUrls.add(BaseURL.URL_LOGIN+"/Marketing/Product");

        financialUrls.add(BaseURL.URL_LOGIN+"/Information/News");
        financialUrls.add(BaseURL.URL_LOGIN+"/Information/Education");
        financialUrls.add(BaseURL.URL_LOGIN+"/Information/Immigrants");

        financialTitles.add("资讯");
        financialTitles.add("教育");
        financialTitles.add("移民");
    }
}
