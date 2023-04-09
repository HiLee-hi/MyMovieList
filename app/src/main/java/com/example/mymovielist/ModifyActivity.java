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
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// LoginBaseActivity
public class ModifyActivity extends LoginBaseActivity {
    Toolbar toolbar_modify;
    Button button_modify;

    EditText id;
    ImageView image;

    Uri uri;
    String profile_image, profile_id;
    String changed_id;

    Login item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        setContent();
        clickEvent();
    }

    void setContent() {
        toolbar_modify = findViewById(R.id.toolbar_modify);
        setSupportActionBar(toolbar_modify);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("modify user");
        }

        id = findViewById(R.id.editText);
        image = findViewById(R.id.imageView);
        button_modify = findViewById(R.id.button_modify);

        // 갤러리 접근
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        item = (Login) getIntent().getSerializableExtra("item");
        if(item != null) Log.d("Modify 에 ", "item 들어옴");

        profile_id = item.getId();
        profile_image = item.getImage();

        id.setText(profile_id);
        Glide.with(getApplicationContext())
                .load(profile_image)
                .into(image);
    }
    void clickEvent() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ModifyActivity.this,
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
//                profile_id = id.getText().toString();
                changed_id = id.getText().toString();

//                if(!profile_id.equals(changed_id)) {
//                    ListBaseActivity activity = new ListBaseActivity();
//                    activity.changeUserName(changed_id);
//                    activity.setUserId(changed_id);
//                    profile_id = changed_id;
//                }
//
//                if(profile_id.length() > 0 && profile_image.length() > 0) {
//                    editLogin(item.getKey(), profile_id, profile_image);
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putExtra("refresh", true);
//                    startActivity(intent);
//                    finish();
//                }

                if(profile_id.length() > 0 && profile_image.length() > 0) {
                    if(getLoginId(profile_id)) {
                        if(!profile_id.equals(changed_id)) {
                            ListBaseActivity activity = new ListBaseActivity();
                            activity.changeUserName(changed_id);
                            activity.setUserId(changed_id);
                            profile_id = changed_id;
                        }

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