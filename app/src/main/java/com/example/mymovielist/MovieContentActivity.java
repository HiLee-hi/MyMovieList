package com.example.mymovielist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieContentActivity extends AppCompatActivity {
    TextView textView_title, textView_subtitle, textView_pubDate, textView_userRating;
    TextView textView_director, textView_actor, textView_link;
    ImageView imageView;

    String title, subtitle, pubDate, userRating, director, actor, link, image;
    Search item;
    SpannableString content;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_content);

        // setting
        setting();
        // 선택된 item 받아오기
        getItem();
        // 보여주기
        showContent();

        textView_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void setting() {
        textView_title = findViewById(R.id.textView_title);
        textView_subtitle = findViewById(R.id.textView_subtitle);
        textView_pubDate = findViewById(R.id.textView_pubDate);
        textView_userRating = findViewById(R.id.textView_userRating);
        textView_director = findViewById(R.id.textView_director);
        textView_actor = findViewById(R.id.textView_actor);
        textView_link = findViewById(R.id.textView_link);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
    }

    void getItem()  {
        item = (Search) getIntent().getSerializableExtra("item");
        if(item != null) Log.d("Reading: ", "item 들어옴");

        title = item.getTitle();
        link = item.getLink();
        content = new SpannableString(link);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        image = item.getImage();
        subtitle = item.getSubtitle();
        pubDate = "제작연도: " + item.getPubDate();
//        director = "감독: " + item.getDirector();
//        actor = "배우: " + item.getActor();
        director = "감독: " + removeLastChar(item.getDirector());
        actor = "배우: " + removeLastChar(item.getActor());
        // 감독, 배우 마지막 ',' 제거
        userRating = "평점: " + item.getUserRating();
    }

    void showContent() {
        textView_title.setText(title);
        textView_subtitle.setText(subtitle);
        textView_pubDate.setText(pubDate);
        textView_userRating.setText(userRating);
        textView_director.setText(director);
        textView_actor.setText(actor);
//        textView_link.setText(link);
        textView_link.setText(content);
        Glide.with(getApplicationContext())
                .load(image)
                .into(imageView);
    }

    String removeLastChar(String str) {
        if(str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, str.length() - 2);
    }

}