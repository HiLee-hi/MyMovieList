package com.example.mymovielist;

import static com.example.mymovielist.ApplicationClass.LIST;
import static com.example.mymovielist.ApplicationClass.SEP;
import static com.example.mymovielist.ApplicationClass.sharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

// ListBaseActivity
public class CalendarActivity extends ListBaseActivity {
    String page = "Calendar";
    String search = "";

    RecyclerView listRecyclerView;
    ArrayList<List> items;
    ListAdapter adapter;
    Context mContext;

    Button button_calendar, button_list, button_info;
    Button button_writing;

    Toolbar toolbar;
    static String user_id;
    String selected_date;
    Login item;

    MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Log.d("calendar: ", "onCreate");
        mContext = this;

        setCalendarActivity();

        getNoteName();
        setToolbar();

        setListRecyclerView();

        setCalendar();
        clickEvent();
    }
    void setCalendarActivity() {
        toolbar = findViewById(R.id.toolbar);

        button_list = findViewById(R.id.button_list);
        button_calendar = findViewById(R.id.button_calendar);
        button_info = findViewById(R.id.button_info);

        calendarView = findViewById(R.id.calendarView);
        listRecyclerView = findViewById(R.id.recyclerView);

        button_writing = findViewById(R.id.button_writing);
    }
    void getNoteName() {
        // 선택된 Login Item 받기
        item = (Login) getIntent().getSerializableExtra("item");
        // 들어온 Item 있으면 이름을 가져와 설정 (ListBaseActivity -> setUserId)
        if(item != null) {
//            Log.d("Calendar 에 ", "item 들어옴");
            user_id = item.getId();
        }
        if(user_id != null) setUserId(user_id);
    }
    void setToolbar() {
        // toolbar 설정 -> title 선택된 Item 이름으로 설정
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar(); // 앱바 제어를 위해 툴바 엑세스
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(USER);
        }
    }
    void setListRecyclerView() {
        items = new ArrayList<>();
        adapter = new ListAdapter(items, this, page, search);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setAdapter(adapter);
    }

    void setCalendar() {
        calendarView.addDecorators( // 토요일, 일요일 색깔 지정
                new SaturdayDecorator(),
                new SundayDecorator()
        );
        // 특정한 날 dot 표시 적용, 오늘 날짜에 표시
        // calendarView.addDecorator(new EventDecorator(Collections.singleton(CalendarDay.today())));
        dotEvent(); // 글이 있는 날에 dot 표시

        calendarView.setSelectedDate(CalendarDay.today()); // 처음 선택되는 날짜를 당일로 지정
        getSelectedRecyclerViewItem();
    }
    void clickEvent() {
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                setListRecyclerView();
                selected_date = getSelectedDate(date);
//                Log.d("선택된 날짜: ", selected_date);
                getSelectedDateItem(selected_date);
            }
        });
        button_list.setOnClickListener(view -> {
            // 달력에서 목록으로 화면전환
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
//            intent.putExtra("user_id", user_id);
            intent.putExtra("item", item);
            startActivity(intent);
        });
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart();
            }
        });
        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
//                intent.putExtra("user_id", user_id);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });
        button_writing.setOnClickListener(view -> {
            // 달력에서 writing 화면전환
            Intent intent = new Intent(getApplicationContext(), WritingActivity.class);
            intent.putExtra("page", page);
            intent.putExtra("search", search);
            startActivity(intent);
        });
    }

    void getSelectedRecyclerViewItem() {
        // 오늘 날짜의 글이 있으면 리사이클러뷰에 표시
        CalendarDay date = CalendarDay.today();
        selected_date = getSelectedDate(date);
        getSelectedDateItem(selected_date);
    }

    String getSelectedDate(CalendarDay date) {
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonth() + 1) ;
        String day = String.valueOf(date.getDay());
        String selected_date = year + "-" + month + "-" + day;

        return selected_date;
    }

    void getSelectedDateItem(String selected_date) {
        for(int i = 0; i < getListSize(); i++) {
            String key = String.format(Locale.KOREA, "%s%03d", USER, i + 1);
            String value = sharedPreferences.getString(key, null);
            if(value != null) {
                String[] saveData = value.split(SEP);
                String date = saveData[2];
                if(date.equals(selected_date))
                    items.add(getListItem(i));
            }
        }
    }
    void dotEvent() {
        HashSet<CalendarDay> set = new HashSet<>();
        for(int i = 0; i < getListSize(); i++) {
            String key = String.format(Locale.KOREA, "%s%03d", USER, i + 1);
            String value = sharedPreferences.getString(key, null);
            if(value != null) {
                String[] saveData = value.split(SEP);
                String date = saveData[2];
                Log.d("글에 작성된 날짜: ", date);

                // 날짜 CalendarDay 형식에 맞게 변환
                String[] splitDate = date.split("-");
                int y = Integer.parseInt(splitDate[0]);
                int m = Integer.parseInt(splitDate[1]) - 1;
                int d = Integer.parseInt(splitDate[2]);
                CalendarDay listDay = new CalendarDay(y, m, d);
                set.add(listDay);
            }
        }
        calendarView.addDecorator(new EventDecorator(set));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("calendar: ", "onStart");
        if(getIntent().getBooleanExtra("refresh", false)) {
            Log.d("calendar: ", "수행됨");
            items.clear();
            getSelectedRecyclerViewItem();
            adapter.notifyDataSetChanged();
        }
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