package com.jone.gwgfproject.NetApi.NetBean.Request;

/**
 * 登录上传参数
 */

public class LoginUpBean {
    public LoginUpBean(String account, String pwd, String token) {
        this.account = account;
        this.pwd = pwd;
        this.token = token;
    }

    public String account;
    public String pwd;
    public String token;
    public int systemType =1;
}
