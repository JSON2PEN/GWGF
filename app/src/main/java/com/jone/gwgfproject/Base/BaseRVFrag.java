package com.jone.gwgfproject.Base;

import android.support.v7.widget.LinearLayoutManager;

import com.json.basewebview.AppHelper;
import com.json.basewebview.Base.Bind.BaseBindFrag;
import com.json.basewebview.R;
import com.json.basewebview.databinding.ActBaseRvBinding;

/**
 * Created by user on 2018/10/19.
 */

public abstract class BaseRVFrag extends BaseBindFrag<ActBaseRvBinding> {
    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    public int setContent() {
        return R.layout.act_base_rv;
    }

    @Override
    protected void init() {
        //增加header和footer需要些在setLayoutManager之前
        addHeaderOrFooter();
        bv.rvBase.setLayoutManager(new LinearLayoutManager(mRootAct, LinearLayoutManager.VERTICAL, false));
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
