package com.jone.gwgfproject.NetApi.NetBean.Request;


/**
 * 验证验证码的请求bean
 */

public class ResetPwRequest {
    public ResetPwRequest(String userId, String email, String VCode,String pwd) {
        this.userId = userId;
        this.Email = email;
        this.VCode = VCode;
        this.Pwd = pwd;
    }

    public String userId;
    public String Email;
    public String VCode;
    public String Pwd;
}
