package com.jone.gwgfproject.NetApi;

/**
 * 用户公共的请求头，图片请求头等
 */
public interface BaseURL {
    /**
     * 生产:http://gdnh.cytx360.com
     * 测试:http://gdnh.licai-space.com
     * http://192.168.2.203:8017
     * 立翔 http://192.168.1.96:8080
     */
    String URL_LOGIN = "http://gdnh.cytx360.com";
    /**
     * 进入首页前的同步数据页面URL
     */
    String URL_CustomerSyn=URL_LOGIN+"/Customer/Customer/CustomerSyn";
}
