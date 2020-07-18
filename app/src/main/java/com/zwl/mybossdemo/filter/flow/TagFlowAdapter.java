package com.zwl.mybossdemo.filter.flow;

import android.view.View;

import java.util.HashSet;
import java.util.List;

/**
 * @author zwl
 * @describe tag 适配器
 * @date on 2020/6/26
 */
public abstract class TagFlowAdapter<T, S> {
    private List<T> datas;

    private HashSet<S> checkedList = new HashSet();

    private S mutexCheck;

    private OnDataChangedListener onDataChangedListener;

    public TagFlowAdapter() {
    }

    /**
     * 构造
     * @param datas 数据
     */
    public TagFlowAdapter(List<T> datas) {
        this.datas = datas;
    }

    /**
     * 构造
     * @param datas   数据
     * @param checkes 选中数据
     */
    public TagFlowAdapter(List<T> datas, S... checkes) {
        this.datas = datas;
        addChecked(checkes);
    }

    /**
     * 构造
     * @param datas      数据
     * @param mutexCheck 互斥数据（不限）设置后这个数据就和其他数据互斥
     */
    public TagFlowAdapter(List<T> datas, S mutexCheck) {
        this.datas = datas;
        setMutexCheck(mutexCheck);
    }

    /**
     * 构造
     * @param datas      数据
     * @param mutexCheck 互斥数据（不限）设置后这个数据就和其他数据互斥
     * @param checkes    选中数据
     */
    public TagFlowAdapter(List<T> datas, S mutexCheck, S... checkes) {
        this.datas = datas;
        setMutexCheck(mutexCheck);
        addChecked(checkes);
    }

    /**
     * 设置互斥数据
     * @param mutexCheck
     */
    public void setMutexCheck(S mutexCheck) {
        this.mutexCheck = mutexCheck;
    }


    /**
     * 添加选中数据
     * @param checkes
     */
    public void addChecked(S... checkes) {
        for (S s : checkes) {
            checkedList.add(s);
        }
    }

    /**
     * 判断数据是否被选中
     * @param s
     * @return
     */
    public boolean isChecked(S s) {
        return checkedList.contains(s);
    }

    /**
     * 数据的数量
     * @return
     */
    public int getCount() {
        return this.datas == null ? 0 : this.datas.size();
    }

    /**
     * 获取数据
     * @return
     */
    public List<T> getDatas() {
        return datas;
    }

    /**
     * 获取指定位置的数据
     * @param position
     * @return
     */
    public T getItem(int position) {
        return this.datas.get(position);
    }

    /**
     * 获取互斥（不限）数据
     * @return
     */
    public S getMutexCheck() {
        return mutexCheck;
    }

    /**
     * 获取当前选中的数据
     * @return
     */
    public HashSet getCheckedList() {
        return checkedList;
    }

    /**
     * 移除选中数据
     * @param o
     * @return
     */
    public boolean removeChecked(S o) {
        boolean b = checkedList.remove(o);
        return b;
    }

    /**
     * 移除互斥数据
     */
    public void removeMutexCheck() {
        if (mutexCheck != null && containsChecked(mutexCheck)) {
            checkedList.remove(mutexCheck);
        }
    }

    /**
     * 移除选中数据
     * @param checkes
     */
    public void removeChecked(S... checkes) {
        for (S s : checkes) {
            checkedList.remove(s);
        }
    }

    /**
     * 选中数据会否包含数据
     * @param s
     * @return
     */
    public boolean containsChecked(S s) {
        return checkedList.contains(s);
    }


    /**
     * 移除所有数据
     */
    public void removeAllChecked() {
        checkedList.clear();
    }

    public void setCheckedList(HashSet<S> checkedList) {
        this.checkedList = checkedList;
    }


    public abstract View getView(TagFlowContainer parent, T item, int position);

    public abstract S isCheckContent(T item, int position);

    public void notifyDataChanged() {
        if (this.onDataChangedListener != null) {
            this.onDataChangedListener.onChanged();
        }

    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        this.onDataChangedListener = listener;
    }

    interface OnDataChangedListener {
        void onChanged();
    }

}
