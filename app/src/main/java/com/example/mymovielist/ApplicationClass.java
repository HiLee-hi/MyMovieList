package com.example.mymovielist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ApplicationClass extends Application {
    static SharedPreferences sharedPreferences = null;
    static SharedPreferences.Editor editor = null;
    static final String APP_DATA = "Movie"; // 데이터 저장할 xml 파일명
    static final String LOGIN = "login"; // 로그인에 사용할 String key
    static final String LIST = "list"; // 목록에 사용할 String key
    static final String SEP = "@#!~"; // split separator

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("sharedPreference: ", "ok");
        if(sharedPreferences == null) {
            sharedPreferences = getApplicationContext().
                    getSharedPreferences(APP_DATA, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }
}
