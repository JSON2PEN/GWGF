package com.json.basewebview.Utils.UpdateApp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.json.basewebview.R;
import com.json.basewebview.Utils.MyToast;
import com.json.basewebview.Utils.UpdateUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 版本更新的工具类
 */

public class UpdateAppUtil {
    /**
     * 入口
     * @param mAct
     * @param versionsInfo
     */
    private UpadteCallBack mUpadteCallBack;
    public void checkAppVersion(final Activity mAct, final VersionsInfo versionsInfo,UpadteCallBack upadteCallBack) {
        this.mUpadteCallBack =upadteCallBack;
        if (versionsInfo==null||TextUtils.isEmpty(versionsInfo.AppVersion)){
            mUpadteCallBack.onFinish();
            return;
        }
        if (canUpdate(mAct, versionsInfo.AppVersion)) {
            if (versionsInfo.MustFlag == 0) {
                final UpdataDialog hd = new UpdataDialog("有新版本是否升级？", mAct, versionsInfo.Remark);
                hd.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                hd.setListener(new UpdataDialog.HintDialogListener() {
                    @Override
                    public void onConfirm(UpdataDialog dialog) {
                        hd.dismiss();
                        downLoadNewVistion(mAct,versionsInfo);
                    }

                    @Override
                    public void onCancel(UpdataDialog dialog) {
                        hd.dismiss();
                        mUpadteCallBack.onFinish();
                    }
                });
                hd.show();
            } else {
                mustUpdateDialog(mAct,"有必须的升级版本，请升级", versionsInfo);
            }
        }else {
            mUpadteCallBack.onFinish();
        }
    }

    /**
     * 获取版本号名称
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean canUpdate(Context context, String netVersion) {
        String currentVersion = getVersionName(context);
        if (!TextUtils.isEmpty(currentVersion) && !TextUtils.isEmpty(netVersion)) {
            String[] netVisition = netVersion.split("\\.");
            String[] localVisition = currentVersion.split("\\.");
            if (netVisition.length > 0 && netVisition.length >= localVisition.length) {
                for (int i = 0; i < localVisition.length; i++) {
                    int netNum = Integer.parseInt(netVisition[i]);
                    int localNum = Integer.parseInt(localVisition[i]);
                    if (netNum > localNum) {
                        return true;
                    } else if (netNum < localNum) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private void mustUpdateDialog(final Activity mAct, String content, final VersionsInfo versionsInfo) {
        final Dialog dialog = new Dialog(mAct, R.style.MyDialog_3);
        View dialog_view = View.inflate(mAct,
                R.layout.updata_must_dialog, null);
        dialog.setContentView(dialog_view);
        TextView tv_content = (TextView) dialog_view.findViewById(R.id.tv_content);
        TextView tvDes = (TextView) dialog_view.findViewById(R.id.tv_updata_des);
        View v =dialog_view.findViewById(R.id.v_area);
        if (TextUtils.isEmpty(versionsInfo.Remark)){
            v.setVisibility(View.VISIBLE);
            tvDes.setVisibility(View.GONE);
        }else {
            tvDes.setText(versionsInfo.Remark);
        }
        TextView tv_ok = (TextView) dialog_view.findViewById(R.id.tv_ok);
        tv_ok.setText("升 级");
        RelativeLayout rl_ok = (RelativeLayout) dialog_view.findViewById(R.id.rl_ok);
        tv_content.setText(content);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        rl_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadNewVistion(mAct,versionsInfo);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    /**
     * 下载新版本APK；
     */
    private void downLoadNewVistion(final Activity mAct, final VersionsInfo versionsInfo) {
        String CACHE_FILE_PATH = "";
        //下载位置
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            CACHE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ApkFile";
        } else {
            Toast.makeText(mAct, "SD卡不可用",Toast.LENGTH_SHORT).show();
            mUpadteCallBack.onFinish();
            return;
        }


        //下载后的APK的命名

        String APK_NAME = "GD_ABC.apk";

      /*  new DownloadFilesTask().execute(apk_url);*/
        //下载

        new InstallUtils(mAct, versionsInfo.DownloadUrl, CACHE_FILE_PATH, APK_NAME, new InstallUtils.DownloadCallBack() {

            private Message msg;
            private List<Long> list = new ArrayList<>();

            @Override
            public void onStart() {
                //开始下载.....
                showProgressBar(mAct,versionsInfo);
            }

            @Override
            public void onComplete(String path) {
                UpdateUI.doOnUI(()->{
                    //完成下载.....
                    //安装APK
                    InstallUtils.installAPK(mAct, path);
                    mProgressDialog.dismiss();
                    mAct.finish();//由于暂时无法监听到用户安装时点击取消,无奈下载完成就把activity关闭
                });
            }


            @Override
            public void onLoading(long total, long current) {
                UpdateUI.doOnUI(()->{
                mProgressDialog.setProgressText((int) (current * 100.0f / total));});
            }

            @Override
            public void onFail(Exception e) {
                UpdateUI.doOnUI(()->{
                    //下载失败...
                    mProgressDialog.dismiss();
                    mUpadteCallBack.onFinish();
                    MyToast.showToast("下载失败,请稍后重试");
                });

            }
        }).downloadAPK();
    }
    private  DoUpdateDialog mProgressDialog;
    private  void showProgressBar(Activity mAct, VersionsInfo versionsInfo) {
        //正在下载...
        String title =getAppName(mAct)+"版本号:"+versionsInfo.AppVersion.substring(0,  versionsInfo.AppVersion.lastIndexOf("."));
        mProgressDialog = new DoUpdateDialog(mAct,title,"正在下载中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    public interface UpadteCallBack{
       void onFinish();//非安装的结束
    }
    //释放一些资源,防止内存泄露
    public void relese(){
        if (mProgressDialog!=null){
            if ( mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            mProgressDialog=null;
        }
    }
    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
