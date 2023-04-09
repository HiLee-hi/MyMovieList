package com.example.mymovielist;

import static com.example.mymovielist.ApplicationClass.LIST;
import static com.example.mymovielist.ApplicationClass.SEP;
import static com.example.mymovielist.ApplicationClass.sharedPreferences;

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

import java.util.ArrayList;
import java.util.Locale;

// ListBaseActivity
public class SearchActivity extends ListBaseActivity {
    String page = "Information";

    RecyclerView recyclerView;
    ArrayList<List> items = new ArrayList<>();
    ListAdapter adapter;
//    ListAdapter adapter = new ListAdapter(items, this);
    String search="";
    Button button_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setContent();
    }

    void setContent() {
        Intent intent = getIntent();
        search = intent.getStringExtra("search");
        Log.d("search: ", search);

//        adapter = new ListAdapter(items, this);
        adapter = new ListAdapter(items, this, page, search);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        button_check = findViewById(R.id.button_check);

        for(int i = 0; i < getListSize(); i++) {
            String key = String.format(Locale.KOREA, "%s%03d", USER, i + 1);
            String value = sharedPreferences.getString(key, null);
            if(value != null) {
                String[] saveData = value.split(SEP);
                String title = saveData[0];
                if(title.contains(search))
                    items.add(getListItem(i));
            }
        }

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
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