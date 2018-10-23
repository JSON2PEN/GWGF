package com.json.basewebview.Web.Base;

/**
 * 高效率的比较两个字符串相同
 */

public class WebStringUtils {
    //取出?号之前的字符串
    public static String getBeforeAskSign(String str){
        if (str!=null){
            int askIndex=str.indexOf("?");
            if (askIndex!=-1){
                return str.substring(0,askIndex);
            }
        }
        return str;
    }
    //取出url最后一个方法名

    /**
     * 将形如http://192.168.2.203:8017/Marketing/Product/Fund?dkasd字符截取Marketing/Product/Fund部分
     * @param url
     * @return
     */
    public static String getLastUrlMethodName(String url){
       url= getBeforeAskSign(url);
        if (url!=null){
            int lastIndex=  url.indexOf("/",9);//url.lastIndexOf("/");
            if (url.length()-1>lastIndex){
                return   url.substring(lastIndex+1);
            }else {
                url = url.substring(0, url.length()-1);
               int twoIndex= url.lastIndexOf("/");
                return url.substring(twoIndex+1,lastIndex);
            }
        }
        return url;
    }
    //高效率判断两个串是否相同
    public static boolean isAlike(String str1, String str2){
        if (str1==null){
            if (str2==null){
                return true;
            }
        }else {
            if (str2!=null){
                if (str1.length()==str2.length()){
                    if (str1.equals(str2)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
