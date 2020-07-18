package com.zwl.mybossdemo.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.zwl.mybossdemo.R;
import com.zwl.mybossdemo.filter.flow.FlowLayout;
import com.zwl.mybossdemo.filter.flow.TagFlowAdapter;
import com.zwl.mybossdemo.filter.flow.TagFlowContainer;
import com.zwl.mybossdemo.filter.flow.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zwl
 * @date on 2020/7/18
 */
public class FilterLayout extends NestedScrollView {

    private final LinearLayout parantRoot;

    private Map<String, List<FilterBean>> filterBeans = new HashMap<>();

    public FilterLayout(@NonNull Context context) {
        this(context, null);
    }

    public FilterLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parantRoot = new LinearLayout(context);
        parantRoot.setOrientation(LinearLayout.VERTICAL);
        parantRoot.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(parantRoot);
    }

    public void setFilterData(List<FilterGrop> filterData) {
        parantRoot.removeAllViews();
        filterBeans.clear();
        for (int i = 0; i < filterData.size(); i++) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_view, null);
            TextView filterName = itemView.findViewById(R.id.filter_name);
            final TagFlowLayout filterList = itemView.findViewById(R.id.filter_flow);
            final ImageView filterMore = itemView.findViewById(R.id.filter_more);
            final FilterGrop filterGrop = filterData.get(i);
            if (filterGrop != null) {
                filterName.setText(filterGrop.gropName);
                filterList.setTagMode(filterGrop.filterType);
                filterList.setTagAdapter(new TagFlowAdapter<FilterBean, String>(filterGrop.filters, FilterBean.UNLIMITED, new String[]{FilterBean.UNLIMITED}) {
                    @Override
                    public View getView(TagFlowContainer parent, FilterBean item, int position) {
                        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_tag, parent, false);
                        textView.setText("" + getItem(position).name);
                        textView.setGravity(Gravity.CENTER);
                        return textView;
                    }

                    @Override
                    public String isCheckContent(FilterBean item, int position) {
                        return item.id;
                    }
                });

                filterList.setOnFoldChangedListener(new FlowLayout.OnFoldChangedListener() {
                    @Override
                    public void onFoldChanged(boolean canFold, boolean fold) {
                        filterMore.setVisibility(canFold ? View.VISIBLE : View.GONE);
                        filterMore.setImageResource(fold ? R.drawable.boss_icon_down : R.drawable.boss_icon_up);
                    }

                    @Override
                    public void onFoldChanging(boolean canFold, boolean fold) {
                    }
                });
                filterMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterList.toggleFold();
                    }
                });

                filterList.setOnCheckChangeListener(new TagFlowLayout.OnCheckChangeListener() {
                    @Override
                    public void onCheckChange(boolean isCheck, int position) {
                        filterBeans.put(filterGrop.key, filterList.getCheckedItemsFilter());
                        if (listener != null) {
                            listener.result(filterBeans);
                        }
                    }
                });
            }
            parantRoot.addView(itemView);
        }
    }


    public void reset() {
        int count = parantRoot.getChildCount();
        for (int i = 0; i < count; i++) {
            View childAt = parantRoot.getChildAt(i);
            TagFlowLayout tagFlowLayout = childAt.findViewById(R.id.filter_flow);
            tagFlowLayout.reset();
        }
        if (listener != null) {
            filterBeans.clear();
            listener.result(filterBeans);
        }
    }

    public List result() {
        int count = parantRoot.getChildCount();
        List list = new ArrayList();
        for (int i = 0; i < count; i++) {
            View childAt = parantRoot.getChildAt(i);
            TagFlowLayout tagFlowLayout = childAt.findViewById(R.id.filter_flow);
            list.addAll(tagFlowLayout.getCheckedItems());
        }
        return list;
    }

    OnFilterChangeListener listener;

    public void setOnFilterChangeListener(OnFilterChangeListener listener) {
        this.listener = listener;
    }

    interface OnFilterChangeListener {
        void result(Map<String, List<FilterBean>> result);
    }

}
