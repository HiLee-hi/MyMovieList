package com.example.mymovielist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchAdapter adapter = new SearchAdapter();
    ArrayList<Search> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Button button = findViewById(R.id.button);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = getIntent();
                    String keyword = intent.getStringExtra("keyword");
                    String str = getNaverSearch(keyword);
                    parseData(str);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setSearchList(items);
                        }
                    });
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }); thread.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(SearchMovieActivity.this, InformationActivity.class);
//                startActivity(intent);
            }
        });
    }

    public String getNaverSearch(String keyword) {
        String clientId = "BRtC876XnJFwlTNFBiFK";
        String clientSecret = "B8q7RgXUFf";
        StringBuffer stringBuffer = new StringBuffer();
        String body;

        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
//            String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text;
            String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text + "&display=20";
            // 검색 키워드 입력하면 그에 대한 결과가 20개만 나오도록 설정

            URL url  = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            InputStream inputStream = con.getInputStream();

            InputStreamReader streamReader = new InputStreamReader(inputStream);
            try(BufferedReader lineReader = new BufferedReader(streamReader)) {
                StringBuilder responseBody = new StringBuilder();
                String line;
                while((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }
                body = responseBody.toString();
            }

        } catch(Exception e) {
            return e.toString();
        }
        return body;
    }

    public void parseData(String body) {
        String title, link, image, subtitle, pubDate, director, actor, userRating;
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(body);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                title = item.getString("title").replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                link = item.getString("link");
                image = item.getString("image");
                subtitle = item.getString("subtitle");
                pubDate = item.getString("pubDate");
                director = item.getString("director").replaceAll("[|]", ", ");
                actor = item.getString("actor").replaceAll("[|]", ", ");
                userRating = String.valueOf(item.getDouble("userRating"));

                items.add(new Search(title, link, image, subtitle, pubDate, director, actor, userRating));

//                System.out.println("title: " + title);
//                System.out.println("link: " + link);
//                System.out.println("image: " + image);
//                System.out.println("subtitle: " + subtitle);
//                System.out.println("pubDate: " + pubDate);
//                System.out.println("director: " + director);
//                System.out.println("actor: " + actor);
//                System.out.println("userRating: " + userRating);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}