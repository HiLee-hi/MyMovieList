package com.example.mymovielist;

import static com.example.mymovielist.ApplicationClass.LIST;
import static com.example.mymovielist.ApplicationClass.SEP;
import static com.example.mymovielist.ApplicationClass.editor;
import static com.example.mymovielist.ApplicationClass.sharedPreferences;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class ListBaseActivity extends AppCompatActivity {
    static String USER = null;

    void setUserId(String user_id) {
        if(!user_id.equals("")) {
            USER = user_id;
            Log.d("ListBase user: ", USER);
        }
    }
    // LIST -> USER
    // List 항목 추가
    void addList(String title, String platform, String date, String rating, String content, String image) {
        int newIndex = getListSize() + 1;
        while (newIndex > 1) {
            String key = String.format(Locale.KOREA, "%s%03d", USER, newIndex);
            String prevKey = String.format(Locale.KOREA, "%s%03d", USER, newIndex - 1);
            String prevValue = sharedPreferences.getString(prevKey, null);

            editor.putString(key, prevValue);
            newIndex--;
        }
        String key = String.format(Locale.KOREA, "%s%03d", USER, newIndex); // key = list001
        String value = title + SEP + platform + SEP + date + SEP + rating + SEP + content + SEP + image;
        editor.putString(key, value);
        editor.apply();
    }

    // List 항목 수정
    void editList(String key, String title, String platform, String date, String rating, String content, String image) {
        String value = title + SEP + platform + SEP + date + SEP + rating + SEP + content + SEP + image;
        editor.putString(key, value);
        editor.apply();
    }

    // List 항목 삭제
    void deleteList(List item) {
        // 선택한 리스트 객체의 키부터 마지막 키까지 하나씩 당기기. 마지막 키는 삭제
        int index = Integer.parseInt(item.getKey().replace(USER, "")); // if item.key == list001 -> index = 1
        while(true) {
            String key = String.format(Locale.KOREA, "%s%03d", USER, index); // 현재 키
            String nextKey = String.format(Locale.KOREA, "%s%03d", USER, index + 1); // 다음 키
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

    // List key 변경
    void changeUserName(String changed_id) {
        for(int i = 1; i <= getListSize(); i++) {
            String prevKey = String.format(Locale.KOREA, "%s%03d", USER, i); // 현재 키
            String newKey = String.format(Locale.KOREA, "%s%03d", changed_id, i); // 변경될 키
            String prevValue = sharedPreferences.getString(prevKey, null);

            editor.putString(newKey, prevValue);
            editor.remove(prevKey);
        }
        editor.apply();
    }

    // List 항목 전체 삭제
    void deleteAll(Login item) {
        String user = item.getId();
        setUserId(user);
        for(int i = 1; i <= getListSize(); i++) {
            String key = String.format(Locale.KOREA, "%s%03d", USER, i);
            editor.remove(key);
        }
        editor.apply();
    }

    // 특정 index 멤버 정보 가져오기
    List getListItem(int index) {
        String key = String.format(Locale.KOREA, "%s%03d", USER, index + 1);
//        Log.d("key: ", key);
        String value = sharedPreferences.getString(key, null);
        if(value == null) return null; // 키에 대한 데이터가 null -> null return
        else {
            String[] saveData = value.split(SEP);
            String title = saveData[0];
            String platform = saveData[1];
            String date = saveData[2];
            String rating = saveData[3];
            String content = saveData[4];
            String image = saveData[5];

            return new List(key, title, platform, date, rating, content, image);
        }
    }

    // 저장된 모든 리스트 추가
    void getList(ArrayList<List> items) {
        for(int i = 0; i < getListSize(); i++) {
            items.add(getListItem(i));
        }
    }

    // 저장된 리스트 아이템의 개수
    int getListSize() {
        int i = 0;
        while(true) {
            if(getListItem(i) == null) return i;
            i++;
        }
    }
}
