package com.json.basewebview.Utils.UpdateApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.json.basewebview.Utils.MyToast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by maning on 16/8/15.
 * 下载更新APK的工具
 */
public class InstallUtils {

    //任务定时器
    private Timer mTimer;
    //定时任务
    private TimerTask mTask;
    //文件总大小
    private int fileLength = 1;
    //下载的文件大小
    private int fileCurrentLength;

    private Context context;
    private String httpUrl;
    private String savePath;
    private String saveName;
    private DownloadCallBack downloadCallBack;
    private File saveFile;


    public interface DownloadCallBack {
        void onStart();

        void onComplete(String path);

        void onLoading(long total, long current);

        void onFail(Exception e);
    }

    public InstallUtils(Context context, String httpUrl, String savePath, String saveName, DownloadCallBack downloadCallBack) {
        this.context = context;
        this.httpUrl = httpUrl;
        this.savePath = savePath;
        this.saveName = saveName;
        this.downloadCallBack = downloadCallBack;
    }


    public void downloadAPK() {
        if (TextUtils.isEmpty(httpUrl)) {
            return;
        }
        saveFile = new File(savePath);
        if (!saveFile.exists()) {
            boolean isMK = saveFile.mkdirs();
            if (!isMK) {
                //创建失败
                return;
            }
        }

        File apkFile = new File(savePath + File.separator + saveName );/*+ ".apk"*/
        if(apkFile.exists()){
            apkFile.delete();
        }
        saveFile = new File(savePath + File.separator + saveName );

        if (downloadCallBack != null) {
            //下载开始
            downloadCallBack.onStart();
        }

        new Thread(() -> {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            HttpURLConnection connection = null;
            BufferedInputStream bis =null;
            BufferedOutputStream bos =null;
            try {
                URL url = new URL(httpUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept", "application/*");
                connection.setConnectTimeout(10 * 1000);
                connection.setReadTimeout(10 * 1000);
                connection.connect();
                inputStream = connection.getInputStream();
                fileLength = connection.getContentLength();
                //采用高效输入流获取网络上的资源
                 bis=new BufferedInputStream(inputStream);
                outputStream = new FileOutputStream(saveFile);
                bos= new BufferedOutputStream(outputStream);
                initTimer();
                byte[] b=new byte[1024];
                int current = 0;
                int len=0;
                //将数据写入文件
                while((len=bis.read(b))!=-1)
                {
                    bos.write(b,0,len);
                    current += len;
                    fileCurrentLength =current;
                    //刷新资源------在前面IO部分提到过，write方法使用完需要刷新
                    bos.flush();
                }

//                    outputStream = new FileOutputStream(saveFile);
                //fileLength = connection.getContentLength();

                //计时器
//                    initTimer();

//                    byte[] buffer = new byte[1024];
//                    int current = 0;
//                    int len;
//                    while ((len = inputStream.read(buffer)) > 0) {
//                        outputStream.write(buffer, 0, len);
//                        current += len;
//                        if (fileLength > 0) {
//                            fileCurrentLength = current;
//                        }
//                    }
                if (downloadCallBack != null) {
                    downloadCallBack.onComplete(saveFile.getPath());
                }
            } catch (final Exception e) {
                e.printStackTrace();
                if (downloadCallBack != null) {
                    downloadCallBack.onFail(e);
                }
            } finally {
                try {
                    if (bos !=null)
                        bos.close();
                    if (outputStream != null)
                        outputStream.close();
                    if (bis!=null)
                        bis.close();
                    if (inputStream != null)
                        inputStream.close();
                    if (connection != null)
                        connection.disconnect();
                } catch (IOException e) {
                }
                //销毁Timer
                destroyTimer();
            }
        }).start();

    }

    private void initTimer() {
        mTimer = new Timer();
        mTask = new TimerTask() {//在run方法中执行定时的任务
            @Override
            public void run() {
                        if (downloadCallBack != null) {
                            downloadCallBack.onLoading(fileLength, fileCurrentLength);
                        }
            }
        };
        //任务定时器一定要启动
        mTimer.schedule(mTask, 0, 200);
    }


    private void destroyTimer() {
        if (mTimer != null && mTask != null) {
            mTask.cancel();
            mTimer.cancel();
            mTask = null;
            mTimer = null;
        }
        if (downloadCallBack != null) {
            downloadCallBack = null;
        }
    }

    public static void installAPK(Activity activity, String filePath) {
        File apkFile =new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(activity, "com.caiyun.gdnhBank.fileprovider", apkFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

       /* Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivityForResult(intent,1000);*/

    }

}
