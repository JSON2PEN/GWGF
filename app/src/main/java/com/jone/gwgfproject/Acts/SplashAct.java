package com.jone.gwgfproject.Acts;

import android.content.Intent;
import android.widget.TextView;

import com.jone.gwgfproject.R;
import com.json.basewebview.Base.BaseAct;
import com.json.basewebview.Utils.CommonUtils;
import com.json.basewebview.Utils.SteepStatusBarUtils;
import com.json.basewebview.Web.WebAct;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 启动页面
 */

public class SplashAct extends BaseAct {

    private TextView tvVersion;
    public Disposable disposable;

    @Override
    protected boolean getIsDropBG() {
        return true;
    }

    @Override
    protected boolean getIsRegister() {
        return false;
    }

    @Override
    protected int getResID() {
        return R.layout.act_splash;
    }

    @Override
    protected void init() {
        tvVersion = findViewById(R.id.tv_version);
        //沉浸式状态栏
        SteepStatusBarUtils.setImmerseLayout(tvVersion, this);
        String verCode = CommonUtils.getVersionName(this);
        tvVersion.setText(verCode);
        startCountDown();
    }

    private void startCountDown() {
        disposable = Observable.timer(2, TimeUnit.SECONDS).subscribe(aLong -> {
             doJump();
         });
    }

    private void doJump() {
        Intent intent =new Intent(this, MainActivity.class);
        intent.putExtra(WebAct.FULL_URL,"http://gdnh.cytx360.com/Customer/Customer");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
