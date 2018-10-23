package com.json.basewebview.NetFrame.Dialog;

/**
 * Created by user on 2018/5/9.
 */

public abstract class BaseBack<T> implements OnNextListener<T>{
    //请求网络结束,无论联网成功失败
    public void onEnd(){}
    //请求网络成功,请求结果失败
    public void onFail(){}
    //请求网络失败
    public void onError(){}
}
