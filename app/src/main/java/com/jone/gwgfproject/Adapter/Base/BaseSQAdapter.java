package com.jone.gwgfproject.Adapter.Base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * DataBinding和Recycler相结合的Adapter
 * Created by HeDongDong on 2018/6/1.
 */

public abstract class BaseSQAdapter<T,D extends ViewDataBinding>  extends RecyclerView.Adapter<BaseSQViewHolder>{

    protected List<T> data = new ArrayList<>();
    protected OnItemClickListener<D> listener;
    protected OnItemLongClickListener<D> onItemLongClickListener;

    @Override
    public BaseSQViewHolder<D> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        D binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutId(), parent, false);
        return new BaseSQViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseSQViewHolder holder, final int position) {
       final T obj = data.get(position);
        bindData((D)holder.getBinding(),obj, position );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    /**
     * 获取子item
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 绑定数据
     *
     * @param binding   具体的ViewDataBinding
     * @param bean      具体的数据Bean
     * @param position  对应的索引
     */
    protected abstract void bindData(D binding,T bean, int position);


    /**
     * 刷新数据
     *
     * @param datas
     */
    public void refresh(List<T> datas) {
        this.data.clear();
        this.data.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    public void addData(List<T> datas) {
        this.data.addAll(datas);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        this.data.addAll(data);
    }

    public void add(T object) {
        data.add(object);
    }

    public void clear() {
        data.clear();
    }

    public void remove(T object) {
        data.remove(object);
    }
    public void remove(int position) {
        data.remove(position);
    }
    public void removeAll(List<T> data) {
        this.data.retainAll(data);
    }

    public void setOnItemClickListener(OnItemClickListener<D> listener) {
        this.listener = listener;
    }

    public List<T> getData() {
        return data;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<D> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


   public interface OnclickListener<P>{
        void onclick(P p, int position);
   }
   private OnclickListener onclickListener;
   public void setListener(OnclickListener onclickListener){
        this.onclickListener = onclickListener;
    }


}
