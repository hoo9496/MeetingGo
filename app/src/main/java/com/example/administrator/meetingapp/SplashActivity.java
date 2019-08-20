package com.example.administrator.meetingapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1000); // 1초간 화면 띄움
    }

    private class splashhandler implements Runnable {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        // 초반 플래시 화면에서 넘어갈 때 뒤로가기 버튼 못누르게 함.
    }
}
