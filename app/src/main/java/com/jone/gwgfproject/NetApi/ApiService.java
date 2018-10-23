package com.jone.gwgfproject.NetApi;



import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shiqiang on 2016/7/11.
 * <p>
 * 1.导入retrofit的依赖库
 */
public interface ApiService {
   /* *//**
     * 登陆接口
     *//*
    @POST("/user/login")
    Observable<CommonResponse> login(@Body LoginUpBean loginUpBean);

    *//**
     * 隐式登录接口
     *//*
    @POST("/user/LoginStatus")
    Observable<CommonResponse> silentLogin(@Body LoginStateRequest bean);

    *//**
     * 获取修改密码验证码
     *
     * @param request
     * @return
     *//*

    @POST("/user/GetForgetPwdCode")
    Observable<CommonResponse> getAuthCode(@Body AuthCodeRequest request);

    *//**
     * 验证修改密码验证码
     *
     * @param request
     * @return
     *//*

    @POST("/user/VerificationCode")
    Observable<CommonResponse> verifyAuthCode(@Body UpCodeRequest request);

    *//**
     * 修改新密码
     *//*
    @POST("/user/SaveForgetPwd")
    Observable<CommonResponse> reSetPw(@Body ResetPwRequest request);
    *//**
     * 获取未读消息个数
     *//*
    @POST("/user/GetNoReadCount")
    Observable<MsgNumResponse> getMsgNum(@Body MsgNumRequest request);
    *//**
     * 版本更新接口
     *//*
    @POST("/user/GetAppVersions")
    Observable<VersionsResponse> getAppVersion(@Body VersionsUpdateRequest request);*/


}
