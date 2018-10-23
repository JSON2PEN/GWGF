package com.jone.gwgfproject.Receivers;

/**
 * 接收极光消息的数据模型
 */

public class ReceiverBean {

    /**
     * Type : 3  1系统,2审批 3异地登录
     * Content : 您的账号已在其他设备登录，如不是本人操作，请尽快修改密码
     * to : url需要拼接的参数
     */

    public int Type;
    public String Content;
    public String To;
}
