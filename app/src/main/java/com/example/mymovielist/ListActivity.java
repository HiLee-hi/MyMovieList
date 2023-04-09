package com.example.mymovielist;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

// ListBaseActivity
public class ListActivity extends ListBaseActivity {
    String page = "List";
    String search="";

    RecyclerView listRecyclerView;
    ArrayList<List> items = new ArrayList<>();
    ListAdapter adapter;
//    ListAdapter adapter = new ListAdapter(items, this);

    Button button_list, button_calendar, button_info;
    Button button_search, button_writing;

    EditText editText_search;

    Login item;
    Toolbar toolbar;
    static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setContent();
        getList(items);
        clickEvent();
    }

    void setContent() {
        item = (Login) getIntent().getSerializableExtra("item");
        if(item != null) {
            Log.d("List 에 ", "item 들어옴");
            user_id = item.getId();
        }
        if(user_id != null) setUserId(user_id);

        // 툴바 생성 및 세팅
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // 액티비티의 앱바로 지정
        if(getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setTitle(user_id);
            getSupportActionBar().setTitle(USER);
        }

        adapter = new ListAdapter(items, this, page, search);

        listRecyclerView = findViewById(R.id.recyclerView);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setAdapter(adapter);

        button_list = findViewById(R.id.button_list);
        button_calendar = findViewById(R.id.button_calendar);
        button_info = findViewById(R.id.button_info);
        button_search = findViewById(R.id.button_search);
        button_writing = findViewById(R.id.button_writing);

        editText_search = findViewById(R.id.editText_search);
    }

    void clickEvent() {
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart();
            }
        });
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                intent.putExtra("item", item);
//                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
                intent.putExtra("item", item);
//                intent.putExtra("user_id", user_id);
                startActivity(intent);            }
        });
        button_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WritingActivity.class);
                intent.putExtra("page", page);
                intent.putExtra("search", search);
                startActivity(intent);
            }
        });
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = editText_search.getText().toString();
                if(search.length() > 0) {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//                    intent.putExtra("user_id", user_id);
                    intent.putExtra("search", search);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if(intent.getBooleanExtra("refresh", false)) {
            items.clear();
            getList(items);
            adapter.notifyDataSetChanged();
        }
//        user_id = intent.getStringExtra("user_id");
    }

    // 메뉴 리소스 xml 내용을 앱바에 반영
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // 앱바에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    // 액티비티의 onOptionsItemSelected() method 호출
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.select_note) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        else if(item.getItemId() == R.id.add_note){
            startActivity(new Intent(this, RegistrationActivity.class));
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}