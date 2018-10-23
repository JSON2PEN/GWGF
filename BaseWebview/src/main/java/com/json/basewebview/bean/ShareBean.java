package com.json.basewebview.bean;

/**
 * Created by user on 2017/3/29.
 */

public class ShareBean {
    public String iconUrl;
    public String title;
    public String content;
    public String url;
    public String titleFriend;

    public ShareBean(String iconUrl, String title, String content, String url, String titleFriend) {
        this.iconUrl = iconUrl;
        this.title = title;
        this.content = content;
        this.url = url;
        this.titleFriend =titleFriend;
    }
}
