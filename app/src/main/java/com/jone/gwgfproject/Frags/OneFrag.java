package com.jone.gwgfproject.Frags;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jone.gwgfproject.R;

/**
 * Created by user on 2018/10/16.
 */

public class OneFrag extends TestFrag {
    @Override
    public void init() {
        LayoutInflater.from(mParentAct).inflate(R.layout.view_title,mFlTitle,true);
        WebView mWebview =new WebView(mParentAct);
        ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebview.setLayoutParams(params);
        mFlFragContent.addView(mWebview);
        mWebview.setWebViewClient(new WebViewClient() {});
        mWebview.loadUrl("http://www.baidu.com");


    }
}
