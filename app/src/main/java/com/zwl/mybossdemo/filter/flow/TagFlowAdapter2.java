package com.zwl.mybossdemo.filter.flow;

/**
 * @author zwl
 * @date on 2020/7/18
 */
public abstract class TagFlowAdapter2<T> extends TagFlowAdapter<T, Integer> {
    @Override
    public Integer isCheckContent(T item, int position) {
        return position;
    }
}
