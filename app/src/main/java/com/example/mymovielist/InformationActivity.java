package com.example.mymovielist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// ListBaseActivity
public class InformationActivity extends ListBaseActivity {

    RecyclerView recyclerView;
    MovieAdapter adapter = new MovieAdapter();
    ArrayList<MovieData> items = new ArrayList<>();

    Button button_list, button_calendar, button_info;
    Button button_search;
    EditText editText_search;

    Login item;
    Toolbar toolbar;
    static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        setContent();
        buttonEvent();
    }

    void setContent() {
        item = (Login) getIntent().getSerializableExtra("item");
        if(item != null) {
            Log.d("Information 에 ", "item 들어옴");
            user_id = item.getId();
        }
        if(user_id != null) setUserId(user_id);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar(); // 앱바 제어를 위해 툴바 엑세스
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(USER);
        }

        recyclerView = findViewById(R.id.recyclerView);
        // RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        button_list = findViewById(R.id.button_list);
        button_calendar = findViewById(R.id.button_calendar);
        button_info = findViewById(R.id.button_info);
        button_search = findViewById(R.id.button_search);
        editText_search = findViewById(R.id.editText_search);

        editText_search.setText(null);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        time.setTime(time.getTime() - (1000*60*60*24)); // 오늘 날짜에서 하루 전
        String today = format.format(time);

        String apiURL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
        String key = "06b4de74d3e2d8c75a3eda682561e5dd";
        String url = apiURL + "?key=" + key + "&targetDt=" + today;
        new Thread() {
            @Override
            public void run() {
                try {
                    URL urlAddress = new URL(url);
                    InputStream in = urlAddress.openStream();

                    InputStreamReader streamReader = new InputStreamReader(in);
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();

                    while(line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }
                    String jsonData = buffer.toString();

                    // jsonData -> JSONObject
                    JSONObject jsonObject = new JSONObject(jsonData);
                    // jsonObject "boxOfficeResult" 추출
                    JSONObject boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");
                    // boxOfficeResult "dailyBoxOfficeList" 추출
                    JSONArray dailyBoxOfficeList = (JSONArray) boxOfficeResult.get("dailyBoxOfficeList");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for(int i = 0; i < dailyBoxOfficeList.length(); i++) {
                                    JSONObject item = dailyBoxOfficeList.getJSONObject(i);
                                    items.add(new MovieData(item.getString("rank"), item.getString("movieNm"), item.getString("openDt"), item.getString("movieCd")));
                                    Log.d("item movieCd: ", item.getString("movieCd"));
//                                    Log.d("items 길이", String.valueOf(items.size()));
                                    // adapter 적용
                                    adapter.setMovieList(items);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start(); // Thread
    }

    void buttonEvent() {
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editText_search.getText().toString();

                Intent intent = new Intent(getApplicationContext(), SearchMovieActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });
        button_list.setOnClickListener(view -> {
            // 달력에서 목록으로 화면전환
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        editText_search.setText(null);
        Log.d("onStart:", "시작");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}