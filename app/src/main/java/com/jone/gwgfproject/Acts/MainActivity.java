package com.jone.gwgfproject.Acts;

import android.Manifest;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jone.gwgfproject.AppConfigs;
import com.jone.gwgfproject.Configs.H5Urls;
import com.jone.gwgfproject.R;
import com.jone.gwgfproject.widgets.tab.MenuTab;
import com.json.basewebview.Base.BaseAct;
import com.json.basewebview.Utils.CommonUtils;
import com.json.basewebview.Utils.MyToast;
import com.json.basewebview.Utils.RxBus.RxBus;
import com.json.basewebview.Utils.RxBus.Subscribe;
import com.json.basewebview.Utils.RxBus.ThreadMode;
import com.json.basewebview.Utils.UpdateApp.UpdateAppUtil;
import com.json.basewebview.Utils.UpdateApp.VersionsInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.functions.Consumer;


public class MainActivity extends BaseAct implements UpdateAppUtil.UpadteCallBack {


    public FragmentTransaction fragmentTransaction;
    public int currentTab = -1;
    private boolean showRedPoint;
    public UpdateAppUtil updateAppUtil;
    public MenuTab mMenuTab;

    @Override
    protected boolean getIsDropBG() {
        return false;
    }

    @Override
    protected boolean getIsRegister() {
        return true;
    }

    @Override
    protected int getResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
//        AndroidBug5497Workaround.assistActivity(this);
        initView();
        canUpdate();

    }

    private void initView() {
        initCustomTab();
    }

    private void initCustomTab() {
        mMenuTab = findViewById(R.id.menu_tab);
        mMenuTab.setTabs(AppConfigs.getTabSelBgs(),
                AppConfigs.getTabNorBgs(),
                AppConfigs.getTabTexts(),
                new int[]{getResources().getColor(R.color.menu_text_selecter), getResources().getColor(R.color.menu_text_normal)},
                currentIndex -> Toast.makeText(MainActivity.this, "当前点击"+currentIndex, Toast.LENGTH_SHORT).show());
    }

    private void canUpdate() {
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {
                   /* VersionsInfo mInfo = new VersionsInfo();
                    mInfo.AppVersion = "1.0.0";
                    mInfo.DownloadUrl = "http://imtt.dd.qq.com/16891/1DFF4F2B276BD2038394A4D99F6AAA68.apk?fsname=com.haocaitou.investproject_1.3.3_8.apk&csr=1bbd";
                    mInfo.MustFlag = 0;
                    mInfo.Remark = "我是更新的数据";
                    new UpdateAppUtil().checkAppVersion(MainActivity.this, mInfo, MainActivity.this);*/
                } else {
                    MyToast.showToast("不给权限");
                }
            }
        });
    }

    private <T extends View> T findView(int viewId) {
        return (T) findViewById(viewId);
    }






    private Fragment addFrag;

    public void showTabWeb(String fundURL) {
       /* fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAll(fragmentTransaction);
        switch (currentTab) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            default:
                break;
        }
        fragmentTransaction.add(R.id.fl_web_content, addFrag).commitAllowingStateLoss();*/

    }

    private void hideAll(FragmentTransaction fragmentTransaction) {
        /*if (approvalWeb != null) {
            fragmentTransaction.hide(approvalWeb);
        }
        if (financialWeb != null) {
            fragmentTransaction.hide(financialWeb);
        }
        if (marketingWeb != null) {
            fragmentTransaction.hide(marketingWeb);
        }
        if (managementWeb != null) {
            fragmentTransaction.hide(managementWeb);
        }*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean canFinish = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (currentTab) {
                case 1:
                   /* if (managementWeb != null) {
                        if (managementWeb.isHome()) {
                            canFinish = true;
                        } else {
                            managementWeb.goBack();
                        }
                    }*/
                    break;
                case 2:
                   /* if (approvalWeb != null) {
                        if (approvalWeb.isHome()) {
                            canFinish = true;
                        } else {
                            approvalWeb.goBack();
                        }
                    }*/
                    break;
                case 3:
                   /* if (marketingWeb != null) {
                        if (marketingWeb.isHome()) {
                            canFinish = true;
                        } else {
                            marketingWeb.goBack();
                        }
                    }*/
                    break;
                case 4:
                    /*if (financialWeb != null) {
                        if (financialWeb.isHome()) {
                            canFinish = true;
                        } else {
                            financialWeb.goBack();
                        }
                    }*/
                    break;
                    default:
                        break;
            }
            if (!canFinish || !CommonUtils.doubleBack()) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFinish() {
        if (updateAppUtil != null) {
            updateAppUtil.relese();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().removeAllStickyEvents();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(VersionsInfo event) {
        updateAppUtil = new UpdateAppUtil();
        updateAppUtil.checkAppVersion(this, event, this);
        RxBus.get().removeStickyEvent(VersionsInfo.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Main_Page");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Main_Page");
        MobclickAgent.onPause(this);
    }

}
