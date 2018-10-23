package com.jone.gwgfproject.NetApi.NetBean.Request;

import java.io.Serializable;

/**
 * 验证验证码的请求bean
 */

public class UpCodeRequest implements Serializable{
    public UpCodeRequest(String userId, String email, String VCode) {
        this.userId = userId;
        Email = email;
        this.VCode = VCode;
    }

    public String userId;
    public String Email;
    public String VCode;
}
