package com.json.basewebview.Utils;

import com.json.basewebview.Utils.UICallBack.UpdateBack;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 子线程更新UI的工具类
 */

public class UpdateUI {
    public static void doOnUI(final UpdateBack mBack){
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> mBack.doInUI());
    }

}
