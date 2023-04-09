package com.example.mymovielist;

import static com.example.mymovielist.ApplicationClass.LOGIN;
import static com.example.mymovielist.ApplicationClass.SEP;
import static com.example.mymovielist.ApplicationClass.editor;
import static com.example.mymovielist.ApplicationClass.sharedPreferences;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class LoginBaseActivity extends AppCompatActivity {
    ArrayList<Login> loginList;

    // Login 항목 추가
    void addLogin(String id, String image) {
        int newIndex = getLoginSize() + 1;
        if(newIndex > 0) {
            String key = String.format(Locale.KOREA, "%s%03d", LOGIN, newIndex);
            String value = id +  SEP + image;
            editor.putString(key, value);
            editor.apply();
        }
    }

    // Login 항목 수정
    void editLogin(String key, String id, String image) {
        String value = id + SEP + image;
        editor.putString(key, value);
        editor.apply();
    }

    // Login 항목 삭제
    void deleteLogin(Login item) {
        // 선택한 리스트 객체의 키부터 마지막 키까지 하나씩 당기기. 마지막 키는 삭제
        int index = Integer.parseInt(item.getKey().replace(LOGIN, "")); // if item.key == list001 -> index = 1
        while(true) {
            String key = String.format(Locale.KOREA, "%s%03d", LOGIN, index); // 현재 키
            String nextKey = String.format(Locale.KOREA, "%s%03d", LOGIN, index + 1); // 다음 키
            String value = sharedPreferences.getString(nextKey, null); // 다음 키의 데이터

            if(value == null) { // 마지막 키인 경우 삭제
                editor.remove(key);
                break;
            } else { // 다음 키가 있을 경우 현재 키에 다음 키의 값을 넣어주기
                editor.putString(key, value);
                index++;
            }
        }
        editor.apply();
    }

    // Login 순서 변경
    void changeLogin(Login item) {
        int index = Integer.parseInt(item.getKey().replace(LOGIN, "")); // if item.key == list001 -> index = 1
        String temp_key = String.format(Locale.KOREA, "%s%03d", LOGIN, index); // 현재 키
        String temp_value = sharedPreferences.getString(temp_key, null); // 다음 키의 데이터

        while(index > 1) {
            String key = String.format(Locale.KOREA, "%s%03d", LOGIN, index); // 현재 키
            String prev_key = String.format(Locale.KOREA, "%s%03d", LOGIN, index - 1); // 이전 키
            String prev_value = sharedPreferences.getString(prev_key, null); // 이전 키의 데이터

            editor.putString(key, prev_value);
            index--;
        }

        temp_key = String.format(Locale.KOREA, "%s%03d", LOGIN, index);
        editor.putString(temp_key, temp_value);
        editor.apply();
    }

    // 특정 index user 정보 가져오기
    Login getLoginItem(int index) {
        String key = String.format(Locale.KOREA, "%s%03d", LOGIN, index + 1);
        Log.d("key: ", key);
        String value = sharedPreferences.getString(key, null);
        if(value == null) return null; // 키에 대한 데이터가 null -> null return
        else {
            String[] saveData = value.split(SEP);
            String id = saveData[0];
            String image = saveData[1];

            return new Login(key, id, image);
        }
    }

    // 저장된 모든 login 추가
    void getLogin(ArrayList<Login> items) {
        for(int i = 0; i < getLoginSize(); i++) {
            items.add(getLoginItem(i));
        }
    }

    // 저장된 리스트 아이템의 개수
    int getLoginSize() {
        int i = 0;
        while(true) {
            if(getLoginItem(i) == null) return i;
            i++;
        }
    }

    // id 중복 체크 추가 코드
   Boolean getLoginId(String profile_id) {
        for(int i = 0; i < getLoginSize(); i++) {
            String key = String.format(Locale.KOREA, "%s%03d", LOGIN, i + 1);
            Log.d("key: ", key);
            String value = sharedPreferences.getString(key, null);

            String[] saveData = value.split(SEP);
            String id = saveData[0];

            if(profile_id.equals(id)) return false;
        }
        return true;
   }
}
