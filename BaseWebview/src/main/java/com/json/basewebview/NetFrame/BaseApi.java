package com.json.basewebview.NetFrame;


import android.app.Activity;

import com.json.basewebview.NetFrame.Dialog.ProgressObserver;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 调用入口
 */

public class BaseApi {
    protected Activity mAct;//该activity为了展示progressDialog
    protected Observable mObservable;

    public void execute(ProgressObserver observer) {
        if(mAct!=null){
            observer.setContext(mAct);
        }
        this.mObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
