package com.example.swaroop.msrit_am.timetable;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swaroop.msrit_am.R;
import com.example.swaroop.msrit_am.timetable.addTimeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TimeTable extends AppCompatActivity{

    TextView heading,day,hr1,hr2,hr3,hr4,hr5,hr6,hr7;

    Button newtt,next,previous;

    ArrayList<String> days = new ArrayList<String>(
            Arrays.asList("monday","tuesday","wednesday","thursday","friday","saturday"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        heading = (TextView) findViewById(R.id.heading);
        day = (TextView) findViewById(R.id.day);
        hr1 = (TextView) findViewById(R.id.hr1);
        hr2 = (TextView) findViewById(R.id.hr2);
        hr3 = (TextView) findViewById(R.id.hr3);
        hr4 = (TextView) findViewById(R.id.hr4);
        hr5 = (TextView) findViewById(R.id.hr5);
        hr6 = (TextView) findViewById(R.id.hr6);
        hr7 = (TextView) findViewById(R.id.hr7);

        next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.previous);

        next.setEnabled(false);
        previous.setEnabled(false);

        try {
            setTimetable();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    set_next_day();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    set_prev_day();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        check_sunday();
    }

    public void setTimetable() throws JSONException {
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        if(sharedPref.contains("TimeTable"))
        {
            heading.setText("ACTIVE TIME TABLE");
            next.setEnabled(true);
            previous.setEnabled(true);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);

            JSONObject obj = new JSONObject(sharedPref.getString("TimeTable",null));

            JSONArray arr = obj.getJSONArray(dayOfTheWeek.toLowerCase());

            day.setText(dayOfTheWeek);
            hr1.setText(arr.getString(0));
            hr2.setText(arr.getString(1));
            hr3.setText(arr.getString(2));
            hr4.setText(arr.getString(3));
            hr5.setText(arr.getString(4));
            hr6.setText(arr.getString(5));
            hr7.setText(arr.getString(6));
        }
        else
        {
            heading.setText("NO TIME TABLE HAS BEEN SET");
        }
    }

    public void set_next_day() throws JSONException {
        previous.setEnabled(true);
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        String d = day.getText().toString();
        int co = days.indexOf(d.toLowerCase());

        if(++co<6)
        {
            JSONObject obj = new JSONObject(sharedPref.getString("TimeTable",null));
            JSONArray arr = obj.getJSONArray(days.get(co));

            Toast.makeText(this,days.get(co),Toast.LENGTH_SHORT).show();
                day.setText(days.get(co));
                hr1.setText(arr.getString(0));
                hr2.setText(arr.getString(1));
                hr3.setText(arr.getString(2));
                hr4.setText(arr.getString(3));
                hr5.setText(arr.getString(4));
                hr6.setText(arr.getString(5));
                hr7.setText(arr.getString(6));
            }
        else
        {
            next.setEnabled(false);
        }
        }


    public void set_prev_day() throws JSONException {
        next.setEnabled(true);
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        String d = day.getText().toString();
        int co = days.indexOf(d.toLowerCase());

        if(--co>-1)
        {
            JSONObject obj = new JSONObject(sharedPref.getString("TimeTable",null));

            JSONArray arr = obj.getJSONArray(days.get(co));

            day.setText(days.get(co));
            hr1.setText(arr.getString(0));
            hr2.setText(arr.getString(1));
            hr3.setText(arr.getString(2));
            hr4.setText(arr.getString(3));
            hr5.setText(arr.getString(4));
            hr6.setText(arr.getString(5));
            hr7.setText(arr.getString(6));
        }
        else
        {
            previous.setEnabled(false);
        }
    }

    public void check_sunday()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if(dayOfTheWeek.equalsIgnoreCase("sunday"))
        {
            hr1.setText("Chill");
            hr2.setText("bro");
            hr3.setText("today");
            hr4.setText("is");
            hr5.setText("SUNDAY");
            hr6.setText("");
            hr7.setText("");
            previous.setEnabled(false);
        }
    }
}
