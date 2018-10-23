package com.json.basewebview.NetFrame.Dialog;

import android.app.Activity;

import com.google.gson.JsonParseException;
import com.json.basewebview.NetFrame.NetResponses.BaseResponse;
import com.json.basewebview.Utils.MyToast;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 带有progressdialog的结果回调
 */

public class ProgressObserver<T extends BaseResponse> implements Observer<T>, ProgressDialogHandler.ProgressCancelListener {
    private BaseBack listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable d;
    private boolean showProgress = true;

    public ProgressObserver(BaseBack listener) {
        this.listener = listener;
    }

    public ProgressObserver(BaseBack listener, boolean showProgress) {
        this.listener = listener;
        this.showProgress = showProgress;
    }


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (!showProgress) {
            return;
        }
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (showProgress) {
            showProgressDialog();
        }
    }

    @Override
    public void onNext(T t) {
        if (t != null) {
            if (t.Code == 0) {
                MyToast.showToast(t.Message);
                listener.onFail();
            } else {
                listener.onSuccess(t);
            }
        }else {//请求对象为空
            listener.onFail();
        }

    }


    @Override
    public void onComplete() {
        dismissProgressDialog();
        listener.onEnd();
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        listener.onError();
        dismissProgressDialog();
        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                MyToast.showToast("网络连接失败,请检查网络");
                break;

            case CONNECT_TIMEOUT:
                MyToast.showToast("连接超时,请稍后再试");
                break;

            case BAD_NETWORK:
                MyToast.showToast("服务器异常");
                break;

            case PARSE_ERROR:
                MyToast.showToast("解析服务器响应数据失败");
                break;

            case UNKNOWN_ERROR:
            default:
                MyToast.showToast("未知错误");
                break;
        }
    }

    public void setContext(Activity mAct) {
        mProgressDialogHandler = new ProgressDialogHandler(mAct, this, true);
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }

}
