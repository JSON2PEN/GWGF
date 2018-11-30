package com.jone.gwgfproject.Adapter.Base;

/**
 * 增加头布局,脚布局的模型
 * layoutId 填充布局的layouID
 * variableId 填充布局的变量
 * T 填充布局的bindingview
 */
public class RecyclerModel<T> {
    private Integer layoutId;
    private Integer variableId;
    private T data;


    public RecyclerModel(Integer layoutId, Integer variableId, T data) {
        this.layoutId = layoutId;
        this.variableId = variableId;
        this.data = data;
    }

    public Integer getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Integer layoutId) {
        this.layoutId = layoutId;
    }

    public Integer getVariableId() {
        return variableId;
    }

    public void setVariableId(Integer variableId) {
        this.variableId = variableId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
