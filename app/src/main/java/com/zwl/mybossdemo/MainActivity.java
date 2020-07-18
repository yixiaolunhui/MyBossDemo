package com.zwl.mybossdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zwl.mybossdemo.company.BossCompanyDetailActivity;
import com.zwl.mybossdemo.dialog.CommonBottomSheetDialog;
import com.zwl.mybossdemo.filter.FilterActivity;
import com.zwl.mybossdemo.mine.BossMineActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /**
     * 我的界面
     *
     * @param view
     */
    public void minePage(View view) {
        startActivity(new Intent(this, BossMineActivity.class));
    }


    /**
     * 公司详情
     *
     * @param view
     */
    public void companyDetail(View view) {
        startActivity(new Intent(this, BossCompanyDetailActivity.class));
    }


    /**
     * 弹出框（分享，待遇等）
     *
     * @param view
     */
    public void commonDialog(View view) {
        View content = LayoutInflater.from(this).inflate(R.layout.dialog_view, null);

        //默认的效果
//        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(content);
//        bottomSheetDialog.show();


        //自定义的BottomSheetDialog 主要是解决高度问题
        final float scale = getResources().getDisplayMetrics().density;
        int height = (int) (500 * scale + 0.5f);
        CommonBottomSheetDialog bottomSheetDialog = new CommonBottomSheetDialog(this, height, height);
        bottomSheetDialog.setContentView(content);
        bottomSheetDialog.show();


    }

    /**
     * 筛选界面
     *
     * @param view
     */
    public void filter(View view) {
        startActivity(new Intent(this, FilterActivity.class));
    }
}
