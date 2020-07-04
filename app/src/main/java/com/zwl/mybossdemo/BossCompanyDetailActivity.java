package com.zwl.mybossdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * BottomSheetBehavior
 */
public class BossCompanyDetailActivity extends AppCompatActivity {

    BottomSheetBehavior bottomSheetBehavior;
    Toolbar toolbar;
    NestedScrollView contentLayout;
    private int mTouchSlop;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);

        contentLayout = findViewById(R.id.content_view);

        //设置标题
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Boss");
        toolbar.setTitleTextColor(Color.WHITE);


        //设置底部
        View bottomSheet = findViewById(R.id.boss_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            /**
             * 状态的改变
             * @param bottomSheet
             * @param newState
             */
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            /**
             * 拖拽回调
             * @param bottomSheet
             * @param slideOffset
             */
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        contentLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                /**
                 * 上滑
                 */
                if (scrollY - oldScrollY > mTouchSlop) {
                    Log.e("onScrollChange", "上滑");
                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                }

                /**
                 * 下滑
                 */
                else if (scrollY - oldScrollY < -mTouchSlop) {
                    Log.e("onScrollChange", "下滑");
                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }
        });
    }

    public void changeBottom(View view) {
        /**
         * 根据当前状态切换 开关
         */
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    public boolean isShow() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            return false;
        }
        return true;
    }
}