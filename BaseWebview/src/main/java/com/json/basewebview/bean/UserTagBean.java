package com.json.basewebview.bean;

/**
 * H5获取移动端用户信息的bean
 */

public class UserTagBean {
    public String deviceId;
    public String userId;
    public String mode;

    public UserTagBean(String device, String userid, String mode) {
        this.deviceId = device;
        this.userId = userid;
        this.mode = mode;
    }


}
