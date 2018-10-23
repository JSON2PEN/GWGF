package com.jone.gwgfproject.Adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * 结合DataBinding实现通用的ViewHolder
 * Created by HeDongDong on 2018/6/1.
 */

public class BaseSQViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{

    private T mBinding;

    public BaseSQViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding(){
        return mBinding;
    }
}
