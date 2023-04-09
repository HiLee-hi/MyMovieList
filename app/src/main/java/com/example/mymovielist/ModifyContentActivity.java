package com.example.mymovielist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

// ListBaseActivity
public class ModifyContentActivity extends ListBaseActivity {
    String page="";
    String search="";

    EditText editText_title, editText_platform, editText_date;
    RatingBar ratingBar;
    ImageView imageView;
    EditText editText_content;

    Button button_calendar;
    Button button_modify, button_cancel;

    String title, platform, date, rating, content;
    Uri uri;
    String imageUrl = "";
    DatePickerDialog datePickerDialog;
    List item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_content);

        setContent();
        clickEvent();
    }

    void setContent() {
        editText_title = findViewById(R.id.editText_title);
        editText_platform = findViewById(R.id.editText_platform);
        editText_date = findViewById(R.id.editText_date);
        ratingBar = findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.imageView);
        editText_content = findViewById(R.id.editText_content);

        button_calendar = findViewById(R.id.button_calendar);
        button_modify = findViewById(R.id.button_modify);
        button_cancel = findViewById(R.id.button_cancel);
        // 갤러리 접근
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        Intent intent = getIntent();
        item = (List) intent.getSerializableExtra("item");
        if(item != null) Log.d("Modify 에 ", "item 들어옴");
        page = intent.getStringExtra("page");
        if(page!="") Log.d("page", page);
        search = intent.getStringExtra("search");
        if(search!="") Log.d("search", search);

        title = item.getTitle();
        platform = item.getPlatform();
        date = item.getDate();
        rating = item.getRating();
        content = item.getContent();
        imageUrl = item.getImage();

        editText_title.setText(title);
        editText_platform.setText(platform);
        editText_date.setText(date);
        ratingBar.setRating(Float.parseFloat(rating));
        editText_content.setText(content);
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .into(imageView);
    }
    void clickEvent() {
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜 변수에 담기
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ModifyContentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                m = m + 1;
                                String date = y + "-" + m + "-" + d;

                                editText_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ModifyContentActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    launcher.launch(intent);
                }
            }
        });

        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = editText_title.getText().toString();
                platform = editText_platform.getText().toString();
                date = editText_date.getText().toString();
                rating = String.valueOf(ratingBar.getRating());
                content = editText_content.getText().toString();
                Log.d("imageUrl 길이: ", String.valueOf(imageUrl.length()));

                if(title.length() > 0 && platform.length() > 0 && date.length() > 0
                        && rating.length() > 0 && content.length() > 0 && imageUrl.length() > 0) {
                    editList(item.getKey(), title, platform, date, rating, content, imageUrl);
//                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    //===========
                    Intent intent;
                    if(search.equals("")) {
                        if(page.equals("Calendar")) {
                            intent = new Intent(getApplicationContext(), CalendarActivity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), ListActivity.class);
                        }
                    } else {
                        intent = new Intent(getApplicationContext(), SearchActivity.class);
                        intent.putExtra("search", search);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return url;
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        imageUrl = getRealPathFromUri(uri);
                        Glide.with(getApplicationContext())
                                .load(imageUrl)
                                .into(imageView);
                    }
                }
            }
    );
}