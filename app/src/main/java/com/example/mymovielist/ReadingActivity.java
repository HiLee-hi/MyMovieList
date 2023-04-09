package com.example.mymovielist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

// ListBaseActivity
public class ReadingActivity extends ListBaseActivity {
    TextView textView_title, textView_platform, textView_date;
    RatingBar ratingBar;
    ImageView imageView;
    TextView textView_content;

    String title, platform, date, rating, content;
    String imageUrl;
    Bitmap bitmap;
    byte[] byteArray;

    Button button_check;
    List item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        textView_title = findViewById(R.id.textView_title);
        textView_platform = findViewById(R.id.textView_platform);
        textView_date = findViewById(R.id.textView_date);
        ratingBar = findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.imageView);
        textView_content = findViewById(R.id.textView_content);

        button_check = findViewById(R.id.button_check);

        item = (List) getIntent().getSerializableExtra("item");
        if(item != null) Log.d("Reading 에 ", "item 들어옴");

        title = item.getTitle();
        platform = item.getPlatform();
        date = item.getDate();
        rating = item.getRating();
        content = item.getContent();
        imageUrl = item.getImage();

        textView_title.setText(title);
        textView_platform.setText(platform);
        textView_date.setText(date);
        ratingBar.setRating(Float.parseFloat(rating));
        textView_content.setText(content);
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .into(imageView);

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }


}