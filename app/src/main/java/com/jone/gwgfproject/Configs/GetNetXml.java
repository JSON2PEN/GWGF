package com.jone.gwgfproject.Configs;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 获取网络配置文件信息
 */

public class GetNetXml {

    public String dictKey;
    public String strVaule;

    /**
     * 获取网络上的XML
     */
    public void getXml(final NetCallBack netCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    HttpURLConnection conn = (HttpURLConnection) new
                            URL(H5Urls.configUrl).openConnection();
                    conn.setConnectTimeout(5000);//设置连接超时
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        pullXml(inputStream, netCallBack);
                    }else {
                        Log.i("TAG","获取xml失败");
                        netCallBack.onFail();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    netCallBack.onFail();
                    e.printStackTrace();
                }//和服务器建立连接
            }
        }).start();
    }

    /**
     * 解析Xml,并将其封装成
     *
     * @param inputStream
     */
    private String keyName;
    private boolean isED;
    private boolean isPN;

    protected void pullXml(InputStream inputStream, NetCallBack netCallBack) {
        try {
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inputStream, "utf-8");
            int eventCode = pullParser.getEventType();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                String targetName = pullParser.getName();
                switch (eventCode) {
                    case XmlPullParser.START_TAG:
                        if ("key".equals(targetName)) {
                            keyName = pullParser.nextText();
                        } else if ("string".equals(targetName)) {
                            strVaule = pullParser.nextText();
                            if (isPN){
                                if ("titleUrl".equals(keyName)){
                                    H5Urls.financialUrls.add(strVaule);
                                }else if ("titleName".equals(keyName)){
                                    H5Urls.financialTitles.add(strVaule);
                                }
                            }else if (isED){
                                if ("titleUrl".equals(keyName)){
                                    H5Urls.marketingUrls.add(strVaule);
                                }else if ("titleName".equals(keyName)){
                                    H5Urls.marketingTitles.add(strVaule);
                                }
                            }else if ("personalHtml".equals(keyName)) {//处理news的开始节点
                                H5Urls.CustomerUrl = strVaule;
                            } else if ("checkHtml".equals(keyName)) {
                                H5Urls.approvalUrl = strVaule;
                            }else if("userHtml".equals(keyName)){
                                H5Urls.userUrl =strVaule;
                            }else if ("messageHtml".equals(keyName)){
                                H5Urls.messagesUrl =strVaule;
                            }else if("isShareFriend".equals(keyName)){
                                H5Urls.isShareFriend=Boolean.valueOf(strVaule);
                            }
                        } else if ("dict".equals(targetName)) {
                            if ("EventDicary".equals(keyName)) {
                                isED = true;
                            } else if ("ProductNews".equals(keyName)) {
                                isPN = true;
                                isED =false;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                }
                eventCode = pullParser.next();//解析下一个节点（开始节点，结束节点）
            }
            netCallBack.onSuccess();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            netCallBack.onFail();
            e.printStackTrace();
        }
    }

    private boolean isKey;
    private boolean isString;

    protected void pullToJson(InputStream inputStream, NetCallBack netCallBack) {
        try {
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inputStream, "utf-8");
            int eventCode = pullParser.getEventType();
            StringBuffer myJson = new StringBuffer();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                String targetName = pullParser.getName();
                switch (eventCode) {
                    case XmlPullParser.START_TAG:
                        if ("key".equals(targetName)) {
                            isKey = true;
                            myJson.append("\"" + pullParser.nextText() + "\"" + ":");
                        } else if ("string".equals(targetName)) {
                            isKey = false;
                            myJson.append("\"" + pullParser.nextText() + "\"");
                        } else if ("dict".equals(targetName)) {
                            if (isKey) {
                                int index = myJson.lastIndexOf("[");
                                myJson = myJson.replace(index, index + 1, "{");
                            }
                            myJson.append("[");
                            isKey = false;
                        }
                        break;
                    case XmlPullParser.START_DOCUMENT:
                        if ("string".equals(targetName)) {
                            myJson.append(",");
                            isString = true;
                        } else if ("key".equals(targetName)) {
                            isString = false;
                        } else if ("dict".equals(targetName)) {
                            if (isString) {
                                myJson.append("}");
                            }else {
                                myJson.append("]");
                            }
                            isString =false;
                        }
                        break;
                }
                if ("string".equals(targetName)) {
                    myJson.append(",");
                    isString = true;
                } else if ("key".equals(targetName)) {
                    isString = false;
                } else if ("dict".equals(targetName)) {
                    if (isString) {
                        myJson.append("}");
                    }else {
                        myJson.append("]");
                    }
                    isString =false;
                }

                eventCode = pullParser.next();//解析下一个节点（开始节点，结束节点）
            }
            String s = myJson.toString();
            netCallBack.onSuccess();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            netCallBack.onFail();
            e.printStackTrace();
        }
    }

    public interface NetCallBack {
        void onSuccess();

        void onFail();
    }
}
