package com.example.mymovielist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// LoginBaseActivity
public class RegistrationActivity extends LoginBaseActivity {

    Toolbar toolbar_register;
    Button button_enroll;

    EditText id;
    ImageView image;

    Uri uri;
    String profile_image = "", profile_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setContent();
        clickEvent();
    }

    void setContent() {
        // 툴바 생성 및 설정
        toolbar_register = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar_register);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기
            getSupportActionBar().setTitle("register user"); // 제목 설정
        }

        id = findViewById(R.id.editText);
        image = findViewById(R.id.imageView);
        button_enroll = findViewById(R.id.button_enroll);

        // 갤러리 접근
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

    }

    void clickEvent() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(RegistrationActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    launcher.launch(intent);
                }
            }
        });
        button_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_id = id.getText().toString();

//                if(profile_id.length() > 0 && profile_image.length() > 0) {
//                    addLogin(profile_id, profile_image);
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putExtra("refresh", true);
//                    startActivity(intent);
//                    finish();
//                }

                if(profile_id.length() > 0 && profile_image.length() > 0) {
                    if(getLoginId(profile_id)) {
                        addLogin(profile_id, profile_image);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("refresh", true);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "이미 사용중인 이름입니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "입력된 정보가 부족합니다.", Toast.LENGTH_SHORT).show();
                }
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
                        profile_image = getRealPathFromUri(uri);
                        Glide.with(getApplicationContext())
                                .load(profile_image)
                                .into(image);
                    }
                }
            }
    );
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