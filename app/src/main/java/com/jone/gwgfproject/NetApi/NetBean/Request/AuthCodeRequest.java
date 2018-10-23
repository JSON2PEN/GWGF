package com.jone.gwgfproject.NetApi.NetBean.Request;

/**
 * 请求验证码的request
 */

public class AuthCodeRequest {
    public AuthCodeRequest(String email) {
        this.email = email;
    }

    public String email;
}
