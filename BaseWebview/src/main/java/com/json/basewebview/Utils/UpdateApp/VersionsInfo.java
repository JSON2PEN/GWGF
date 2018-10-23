package com.json.basewebview.Utils.UpdateApp;

import com.json.basewebview.NetFrame.NetResponses.BaseResponse;

/**
 * Created by user on 2016/11/8.
 */
public class VersionsInfo extends BaseResponse{
    public int Id;
    /// 版本发布时间
    public String VersionTime;//发布时间
    /// 系统平台0：iOS（Apple）、1：iOS（Pad）、2：Android、3：其它
    public int PlatformType;//0：iOS（Apple）、1：iOS（Pad）、2：Android、3：其它

    public int MustFlag;//0：可选升级，1：必须升级

    public String AppVersion;//apk版本

    public String DownloadUrl;//下载地址

    public String Remark;//更新说明

    public long   fileTotalSize;//apk的大小
}

