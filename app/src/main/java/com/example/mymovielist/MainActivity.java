package com.example.mymovielist;

import static com.example.mymovielist.ApplicationClass.editor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

// LoginBaseActivity
public class MainActivity extends LoginBaseActivity {
    final String TAG = "Main ";

    RecyclerView loginRecyclerView;
    ArrayList<Login> items = new ArrayList<>();
    LoginAdapter loginAdapter = new LoginAdapter(items, this);
    Button button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        clearPref();

        setContent();
        getLogin(items);
    }

    void setContent() {
        loginRecyclerView = findViewById(R.id.recyclerView);
        loginRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loginRecyclerView.setAdapter(loginAdapter);

        button_add = findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) { // launchMode = "singleTask"
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getBooleanExtra("refresh", false)) {
            items.clear();
            getLogin(items);
            loginAdapter.notifyDataSetChanged();
        }
    }

    void clearPref() {
        editor.clear();
        editor.apply();
    }
}