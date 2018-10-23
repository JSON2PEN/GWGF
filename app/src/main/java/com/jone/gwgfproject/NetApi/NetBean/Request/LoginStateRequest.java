package com.jone.gwgfproject.NetApi.NetBean.Request;

/**
 * 隐式登录验证参数
 */

public class LoginStateRequest {
    public LoginStateRequest(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String id;
    public String token;
    public int systemType =1;
}
