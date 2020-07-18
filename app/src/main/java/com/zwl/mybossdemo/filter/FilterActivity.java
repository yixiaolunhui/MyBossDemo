package com.zwl.mybossdemo.filter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.zwl.mybossdemo.R;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 筛选
 */
public class FilterActivity extends AppCompatActivity {
    private FilterLayout mFilterLayout;
    private TextView mFilterResultTV;
    private TextView mFilterResultNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        mFilterLayout = findViewById(R.id.filter_list);
        mFilterResultTV = findViewById(R.id.filter_result_tv);
        mFilterResultNum = findViewById(R.id.filter_result_num);
        mFilterLayout.setFilterData(FilterDataUtils.getFilterData());
        mFilterLayout.setOnFilterChangeListener(new FilterLayout.OnFilterChangeListener() {
            @Override
            public void result(Map<String, List<FilterBean>> result) {
                if (result != null) {
                    Iterator<Map.Entry<String, List<FilterBean>>> iterator = result.entrySet().iterator();
                    int num = 0;
                    while (iterator.hasNext()) {
                        List<FilterBean> value = iterator.next().getValue();
                        num += value.size();
                    }
                    if(num==0){
                        mFilterResultTV.setText("筛选");
                        mFilterResultNum.setVisibility(View.GONE);
                    }else{
                        mFilterResultTV.setText("筛选.");
                        mFilterResultNum.setVisibility(View.VISIBLE);
                        mFilterResultNum.setText(String.valueOf(num));
                    }

                }
            }
        });
    }


    /**
     * 重置
     *
     * @param view
     */
    public void reset(View view) {
        mFilterLayout.reset();
    }

    /**
     * 确定
     *
     * @param view
     */
    public void ok(View view) {
        List result = mFilterLayout.result();
        Log.e("FilterActivity", "" + result.toString());
    }

    /**
     * 关闭
     *
     * @param view
     */
    public void close(View view) {
        finish();
    }
}