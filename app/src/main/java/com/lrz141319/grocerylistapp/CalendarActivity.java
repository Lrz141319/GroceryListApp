package com.lrz141319.grocerylistapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity
{
    CalendarView m_CalendarView;
    String m_SelectedDate;
    Calendar m_Calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        m_CalendarView = (CalendarView) findViewById(R.id.calendarView);
        m_Calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        m_SelectedDate = format.format(date);
        this.SetupSelectedDayChangeListner();
    }

    private void SetupSelectedDayChangeListner()
    {
        m_CalendarView.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener()
                {
                    @Override
                    public void onSelectedDayChange(
                            @NonNull CalendarView calendarView,
                            int year,
                            int month,
                            int day)
                    {
                       m_SelectedDate = year + "/" + (month+1) + "/" + day;
                    }
                }
        );
    }

    public void onNextBtnClick(View view)
    {
        Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
        if(intent != null)
        {
            intent.putExtra("date", m_SelectedDate);
            startActivity(intent);
        }
    }
}