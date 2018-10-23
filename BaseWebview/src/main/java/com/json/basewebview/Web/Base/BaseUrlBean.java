package com.json.basewebview.Web.Base;

/**
 * 用来保存单个Url页面的相关属性
 */

public class BaseUrlBean {
    //1,代表返回指定路径 2,跳过层级 3,通配符跳过 4,返回执行Js方法 5,不如堆栈 6返回原生上级页面 7跳转原生指定页面
    public int status;//用来确认当前页面的返回优先级
    //返回到指定url
    public String backUrl;
    //是否刷新
    public boolean isReload;
    //跳过页面数
    public int backCount;
    //页面不如堆栈
    public boolean notAddStack;
    //是否是通配符页面
    public boolean isBackSkip;
    //返回原生上级页面
    public boolean isBackNative;
    //跳转原生指定页面(页面名称)
    public String nativeVCName;
    //返回执行JS的方法名和参数
//    public String jsCode;
    //页面的title
    public String title;
    //判断是web自带的title还是原生自己设置的title
    public boolean isWebTitle;
    //页面返回时需要执行的js方法
    public String backDoJs;//页面点击返回时执行js
    public String pageFinishDoJs;//当前页面加载完毕时执行的js
    public boolean isBackDoJs;//是否返回到上级页面完毕时执行,true是,false否
//    public boolean isFirstPage;//当前页面是否为首页
}
