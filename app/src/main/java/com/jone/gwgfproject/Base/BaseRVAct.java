package com.jone.gwgfproject.Base;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;

import com.json.basewebview.AppHelper;
import com.json.basewebview.Base.Bind.BaseBindAct;
import com.json.basewebview.R;
import com.json.basewebview.databinding.ActBaseRvBinding;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * Created by user on 2018/10/19.
 */

public abstract class BaseRVAct extends BaseBindAct<ActBaseRvBinding> {
    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected int getResID() {
        return R.layout.act_base_rv;
    }

    @Override
    protected void init() {
        bv.refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                },2000);
            }
        });
        //增加header和footer需要些在setLayoutManager之前
        addHeaderOrFooter();
        bv.rvBase.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //如果Item高度固定  增加该属性能够提高效率
        bv.rvBase.setHasFixedSize(true);
        if (AppHelper.isConnected()){
            initAdapter();
        }else {
            showError();
        }
    }

    protected abstract void addHeaderOrFooter();

    public abstract void initAdapter();

}
