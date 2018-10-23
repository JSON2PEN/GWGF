package com.jone.gwgfproject.NetApi;

import android.app.Activity;

import com.json.basewebview.NetFrame.ApiStrategy;
import com.json.basewebview.NetFrame.BaseApi;


/**
 * Created by user on 2018/5/9.
 */

public class ApiMethods extends BaseApi {
    //空参构造针对不需要显示dialog
    public ApiMethods() {}
    //有残构造必须要是activity,用于展示dialog
    public ApiMethods(Activity act) {
        mAct = act;
    }

    private static ApiService loginApi = ApiStrategy.getApiService(ApiService.class, BaseURL.URL_LOGIN);

    /**
     * 这里写所有的调用方法
     *
     * @return
     */



}
