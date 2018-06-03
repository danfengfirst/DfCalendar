package com.df.dfcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.df.dfcalendar.widget.CalendarView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    CalendarView mCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCalendarView=findViewById(R.id.appoint_calendar);
        initCalendarView();
    }
    private void initCalendarView() {
        mCalendarView.setETimeSelListener(new CalendarView.CalendatEtimSelListener() {
            @Override
            public void onETimeSelect(Date date) {
                if (date != null) {
                    String etimestr = (date.getYear() + 1900) + getString(R.string.year) + (date.getMonth() + 1) + getString(R.string.month) + date.getDate() + getString(R.string.day);
                    Toast.makeText(getApplicationContext(),"结束时间"+etimestr,Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCalendarView.setSTimeSelListener(new CalendarView.CalendarSTimeSelListener() {
            @Override
            public void onSTimeSelect(Date date) {
                if (date != null) {
                    String stimestr = (date.getYear() + 1900) + getString(R.string.year) + (date.getMonth() + 1) + getString(R.string.month) + date.getDate() + getString(R.string.day);
                    Toast.makeText(getApplicationContext(),"开始时间"+stimestr,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
