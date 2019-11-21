package com.example.homeactivity.Share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.homeactivity.Filter.FilterActivity;
import com.example.homeactivity.R;

import java.util.logging.Filter;

public class LodingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), FilterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5500);

    }
}
