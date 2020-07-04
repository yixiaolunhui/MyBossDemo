package com.zwl.mybossdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void minePage(View view) {
        startActivity(new Intent(this,BossMineActivity.class));
    }

    public void companyDetail(View view) {
        startActivity(new Intent(this,BossCompanyDetailActivity.class));
    }
}
