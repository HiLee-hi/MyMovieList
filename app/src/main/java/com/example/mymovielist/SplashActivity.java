package com.example.mymovielist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Splash Screen 화면이 시작되고, 2초 딜레이를 준 후에 run() 안의 코드 동작
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.beginMainActivity();
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        // Splash Screen 화면이 클릭되면 바로 MainActivity 실행
        beginMainActivity();
    }

    private void beginMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}