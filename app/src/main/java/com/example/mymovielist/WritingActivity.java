package com.example.mymovielist;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

// ListBaseActivity
public class WritingActivity extends ListBaseActivity {
    String page;
    String search="";

    EditText editText_title, editText_platform, editText_date;
    RatingBar ratingBar;
    ImageView imageView;
    EditText editText_content;

    Button button_calendar;
    Button button_save, button_cancel;

    String title, platform, date, rating, content;
    Uri uri;
    String imageUrl = "";
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        setContent();
        clickEvent();
    }

    void setContent() {
        Intent intent = getIntent();
        page = intent.getStringExtra("page");
        search = intent.getStringExtra("search");

        Log.d("이전 페이지", page);
        if(!search.equals("")) Log.d("검색어", search);

        editText_title = findViewById(R.id.editText_title);
        editText_platform = findViewById(R.id.editText_platform);
        editText_date = findViewById(R.id.editText_date);
        ratingBar = findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.imageView);
        editText_content = findViewById(R.id.editText_content);

        button_calendar = findViewById(R.id.button_calendar);
        button_save = findViewById(R.id.button_save);
        button_cancel = findViewById(R.id.button_cancel);
        // 갤러리 접근
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    void clickEvent(){
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜 변수에 담기
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(WritingActivity.this,
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
                if (ActivityCompat.checkSelfPermission(WritingActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    launcher.launch(intent);
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = editText_title.getText().toString();
                platform = editText_platform.getText().toString();
                date = editText_date.getText().toString();
                rating = String.valueOf(ratingBar.getRating());
                content = editText_content.getText().toString();

                if(title.length() > 0 && platform.length() > 0 && date.length() > 0
                        && rating.length() > 0 && content.length() > 0 && imageUrl.length() > 0) {
                    addList(title, platform, date, rating, content, imageUrl);
                    Intent intent;
                    if(page.equals("Calendar")) {
                        intent = new Intent(getApplicationContext(), CalendarActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), ListActivity.class);
                    }
                    intent.putExtra("refresh", true);
                    startActivity(intent);
                    finish();
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(page.equals("Calendar")) {
                    intent = new Intent(getApplicationContext(), CalendarActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), ListActivity.class);
                }
                startActivity(intent);
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