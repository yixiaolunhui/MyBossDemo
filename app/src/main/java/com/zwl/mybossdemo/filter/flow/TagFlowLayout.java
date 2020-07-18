package com.zwl.mybossdemo.filter.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zwl.mybossdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwl
 * @describe TAG 布局
 * @date on 2020/6/26
 */
public class TagFlowLayout extends FlowLayout implements TagFlowAdapter.OnDataChangedListener {

    public final static int TAG_MODE_SHOW = 0; //标签
    public final static int TAG_MODE_SINGLE = 1;//单选
    public final static int TAG_MODE_MULTIPLE = 2;//多选
    private int mTagMode = TAG_MODE_SHOW;

    private Context mContext;

    private TagFlowAdapter tagAdapter;

    private OnCheckChangeListener onCheckChangeListener;
    private OnTagClickListener onTagClickListener;
    private TagFlowContainer mutexTagFlowView;//互斥选项

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mTagMode = typedArray.getInt(R.styleable.TagFlowLayout_flow_mode, TAG_MODE_SHOW);
        typedArray.recycle();
    }


    public void setTagAdapter(TagFlowAdapter tagAdapter) {
        this.tagAdapter = tagAdapter;
        this.tagAdapter.setOnDataChangedListener(this);
        updateTagView();
    }

    /**
     * 设置模式（单选，标签，多选）
     *
     * @param mTagMode
     */
    public void setTagMode(int mTagMode) {
        this.mTagMode = mTagMode;
    }

    @Override
    public void onChanged() {
        updateTagView();
    }


    private void updateTagView() {
        removeAllViews();
        if (null == tagAdapter) return;
        int count = tagAdapter.getCount();

        TagFlowContainer tagContainer;
        for (int i = 0; i < count; i++) {
            tagContainer = new TagFlowContainer(mContext);
            View tagView = tagAdapter.getView(tagContainer, tagAdapter.getItem(i), i);
            //允许我们的CHECKED状态向下传递
            tagView.setDuplicateParentStateEnabled(true);
            tagContainer.addView(tagView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tagContainer.setTag(tagAdapter.isCheckContent(tagAdapter.getItem(i), i));
            tagContainer.setChecked(tagAdapter.isChecked(tagAdapter.isCheckContent(tagAdapter.getItem(i), i)));
            tagContainer.setOnClickListener(new TagClickListener(i));
            addView(tagContainer);
        }
        mutexTagFlowView = getViewByTag(tagAdapter.getMutexCheck());
    }


    /**
     * 设置互斥选中还是不选中
     *
     * @param isCheck
     */
    public void setMutexTagFlowView(boolean isCheck) {
        if (tagAdapter.getMutexCheck() != null && mutexTagFlowView != null) {
            if (isCheck) {
                tagAdapter.addChecked(tagAdapter.getMutexCheck());
            } else {
                tagAdapter.removeMutexCheck();
            }
            mutexTagFlowView.setChecked(isCheck);
        }
    }


    /**
     * 根据tag获取对应的View
     *
     * @param o
     * @return
     */
    public TagFlowContainer getViewByTag(Object o) {
        if (o == null) return null;
        int count = tagAdapter.getCount();
        for (int i = 0; i < count; i++) {
            TagFlowContainer childAt = (TagFlowContainer) getChildAt(i);
            if (childAt.getTag() != null && childAt.getTag() == o) {
                return childAt;
            }
        }
        return null;
    }


    /**
     * 点击事件处理
     */
    private class TagClickListener implements View.OnClickListener {
        private int position;

        public TagClickListener(int i) {
            this.position = i;
        }

        @Override
        public void onClick(View v) {
            TagFlowContainer tagFlowContainer = (TagFlowContainer) v;
            if (onTagClickListener != null) {
                if (onTagClickListener.onTagClick(tagFlowContainer, position)) {
                    return;
                }
            }
            //单选
            if (mTagMode == TAG_MODE_SINGLE) {
                //如果已经是选中的就不需要操作
                if (tagFlowContainer.isChecked()) {
                    return;
                } else {
                    clearAllCheckedState();//清空所有view上的状态
                    tagAdapter.removeAllChecked();//清空选中的集合
                    tagAdapter.addChecked(tagAdapter.isCheckContent(tagAdapter.getItem(position), position));
                }
            }
            //多选
            else if (mTagMode == TAG_MODE_MULTIPLE) {
                boolean isMutex = tagAdapter.getMutexCheck() != null &&
                        tagAdapter.isCheckContent(tagAdapter.getItem(position), position) == tagAdapter.getMutexCheck();
                //之前是选中状态
                if (tagFlowContainer.isChecked()) {
                    if (isMutex) return;//如果是互斥项而且是已经选中了直接return
                    tagAdapter.removeChecked(tagAdapter.isCheckContent(tagAdapter.getItem(position), position));
                    if (tagAdapter.getCheckedList().size() == 0) {
                        setMutexTagFlowView(true);
                    }
                }
                //之前是未选中状态
                else {
                    //如果是点击了互斥的
                    if (isMutex) {
                        tagAdapter.removeAllChecked();//清空选中的集合
                        clearAllCheckedState();//清空所有view上的状态
                    }
                    //点击的不是互斥的
                    else {
                        setMutexTagFlowView(false);
                    }
                    tagAdapter.addChecked(tagAdapter.isCheckContent(tagAdapter.getItem(position), position));
                }
            }
            //纯展示
            else {
                return;
            }
            tagFlowContainer.toggle();
            if (onCheckChangeListener != null)
                onCheckChangeListener.onCheckChange(tagFlowContainer.isChecked(), position);

        }
    }


    /**
     * 单选模式 清空所有选中状态
     */
    private void clearAllCheckedState() {
        if (mTagMode == TAG_MODE_SINGLE || mTagMode == TAG_MODE_MULTIPLE) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                TagFlowContainer childAt = (TagFlowContainer) getChildAt(i);
                childAt.setChecked(false);
            }
        }
    }

    /**
     * 重置
     */
    public void reset() {
        clearAllCheckedState();
        if (tagAdapter != null) {
            tagAdapter.removeAllChecked();
        }
        setMutexTagFlowView(true);
    }

    /**
     * 设置选中
     *
     * @param pos
     */
    public void setChecked(Object... pos) {
        tagAdapter.addChecked(pos);
    }


    /**
     * 删除
     *
     * @param pos
     */
    public void removeChecked(Object... pos) {
        tagAdapter.removeChecked(pos);
    }


    /**
     * 是否包含
     *
     * @param pos
     */
    public boolean containsChecked(Object... pos) {
        return tagAdapter.containsChecked(pos);
    }


    /**
     * 后去所有选中的对象  按照对象数据顺序取
     *
     * @return
     */
    public List getCheckedItems() {
        ArrayList items = new ArrayList();
        for (int i = 0; i < tagAdapter.getCount(); i++) {
            if (tagAdapter.isChecked(tagAdapter.isCheckContent(tagAdapter.getItem(i), i))) {
                items.add(tagAdapter.getItem(i));
            }
        }
        return items;
    }

    /**
     * 去掉设置的互斥数据
     *
     * @return
     */
    public List getCheckedItemsFilter() {
        ArrayList items = new ArrayList();
        for (int i = 0; i < tagAdapter.getCount(); i++) {
            if (tagAdapter.isCheckContent(tagAdapter.getItem(i), i) != tagAdapter.getMutexCheck() &&
                    tagAdapter.isChecked(tagAdapter.isCheckContent(tagAdapter.getItem(i), i))) {
                items.add(tagAdapter.getItem(i));
            }
        }
        return items;
    }


    public TagFlowAdapter getAdapter() {
        return tagAdapter;
    }

    public Object getItem(int position) {
        return tagAdapter.getItem(position);
    }


    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }


    public interface OnCheckChangeListener {
        void onCheckChange(boolean isCheck, int position);
    }

    public interface OnTagClickListener {
        boolean onTagClick(View view, int position);
    }
}
