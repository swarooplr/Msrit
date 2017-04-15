package com.example.swaroop.msrit_am;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class bunkSuggestions extends Activity {

    ArrayList<String> periodsofday=new ArrayList<>();
    ArrayList<HashMap<String,Integer>> sisdetails=new ArrayList<>();
    String emptyclass="NO CLASS";

    JSONArray jsonArray;

    LinearLayout max,last;
    TextView heading,warning;

    TextView subject;
    TextView bunks;
    TextView risky;
    TextView oneMore;
    TextView percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bunksuggestions);

        Intent yes = getIntent();
        String usn = yes.getExtras().getString("usn");
        String dob = yes.getExtras().getString("dob");

        max=(LinearLayout)findViewById(R.id.max);
        last=(LinearLayout)findViewById(R.id.last);

        heading=(TextView)findViewById(R.id.heading);
        warning=(TextView)findViewById(R.id.warning);

        warning.setVisibility(View.INVISIBLE);

        try {create_bunks_arrays(usn,dob);}
        catch (JSONException e) {e.printStackTrace();}

        try {getTimetable();}
        catch (Exception e)
        {

            warning.setVisibility(View.VISIBLE);
            max.setVisibility(View.INVISIBLE);
            last.setVisibility(View.INVISIBLE);
        }

        try {getsisdetails();}
        catch (Exception e)
        {
            warning.setVisibility(View.VISIBLE);
            warning.setText("Attendence information not avaliable");
            max.setVisibility(View.INVISIBLE);
            last.setVisibility(View.INVISIBLE);
        }

        try{
            int result=findmaxattendce();
            if(!(result==-1))
            {
                subject = (TextView)findViewById(R.id.subject1);
                bunks = (TextView)  findViewById(R.id.bunks1);
                percent=(TextView)  findViewById(R.id.percentage1);
                risky=(TextView)    findViewById(R.id.risky1);
                oneMore=(TextView)  findViewById(R.id.oneMore1);

                JSONObject child=jsonArray.getJSONObject(result);

                subject.setText(child.getString("name"));
                percent.setText("Current Percentage:"+child.getString("percentage")+"%");
                bunks.setText("Classes you can skip:"+child.getString("bunks"));
                risky.setText("MAX. classes you can skip(75%):"+child.getString("risky"));
                oneMore.setText("If you skip one class % will become:"+child.getString("OneMoreBunkP")+"%");
            }
            else
            {
                max.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e)
        {
            max.setVisibility(View.INVISIBLE);
        }

        try{
            int result=findlastperoidstobunk();
            if(!(result==-1))
            {
                subject = (TextView)findViewById(R.id.subject2);
                bunks = (TextView)  findViewById(R.id.bunks2);
                percent=(TextView)  findViewById(R.id.percentage2);
                risky=(TextView)    findViewById(R.id.risky2);
                oneMore=(TextView)  findViewById(R.id.oneMore2);

                JSONObject child=jsonArray.getJSONObject(result);

                subject.setText(child.getString("name"));
                percent.setText("Current Percentage:"+child.getString("percentage")+"%");
                bunks.setText("Classes you can skip:"+child.getString("bunks"));
                risky.setText("MAX. classes you can skip(75%):"+child.getString("risky"));
                oneMore.setText("If you skip one class % will become:"+child.getString("OneMoreBunkP")+"%");
            }
            else
            {
                max.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e)
        {
            max.setVisibility(View.INVISIBLE);
        }
    }

    public void getTimetable() throws JSONException {
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        if(sharedPref.contains("TimeTable"))
        {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);

            JSONObject obj = new JSONObject(sharedPref.getString("TimeTable",null));

            JSONArray arr = obj.getJSONArray(dayOfTheWeek.toLowerCase());

            for(int i=0;i<arr.length();i++)
            {
                if(!arr.getString(i).equals(emptyclass))
                    periodsofday.add(arr.getString(i));

            }
        }
    }

    public void getsisdetails()
    {
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        try {
            jsonArray=new JSONArray(sharedPref.getString("bunk_details",null));
        }
        catch (Exception e)
        {        }
    }




    public boolean hasperiod(String sub,int start)
    {
        for(int i=start;i<periodsofday.size();i++)
        {
            if(sub.equals(periodsofday.get(i)))
                return true;
        }
        return false;
    }

    public int findmaxattendce()
    {   JSONObject child;
        int riskybunks= -1;
        for(int i=0;i<jsonArray.length();i++)
        {
            try {
                child = jsonArray.getJSONObject(i);
                riskybunks=child.getInt("risky");
                if(riskybunks<1)
                {
                    return -1;
                }
                if(hasperiod(child.getString("name"),0))
                    return i;
            }
            catch (Exception e){};
        }
        return -1;
    }

    public int findlastperoidstobunk()
    {
        int setlimitcount=0;

        if(periodsofday.size()<4)
            setlimitcount=1;
        else if(periodsofday.size()==5||periodsofday.size()==6)
            setlimitcount=2;
        else if(periodsofday.size()>6)
            setlimitcount=3;
        else
            setlimitcount=0;

        JSONObject child;
        int riskybunks= -1;

        for(int i=0;i<jsonArray.length();i++)
        {
            try {
                child = jsonArray.getJSONObject(i);
                riskybunks=child.getInt("risky");

                if(riskybunks<1)
                {
                    return -1;
                }

                if(hasperiod(child.getString("name"),periodsofday.size()-setlimitcount))
                    return i;
            }
            catch (Exception e){};
        }
        return -1;
    }

    public void create_bunks_arrays(String usn,String dob) throws JSONException {

        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        JSONObject obj1 = new JSONObject(sharedPref.getString(usn+dob,null));
        JSONArray jsonArr = obj1.getJSONArray("courses");

        JSONArray data = new JSONArray();
        for(int i =0 ;i<jsonArr.length();i++)
        {
            JSONObject obj = new JSONObject();
            JSONObject temp = jsonArr.getJSONObject(i);

            obj.put("name",temp.getString("name"));
            obj.put("code",temp.getString("code"));

            JSONObject att = temp.getJSONObject("attendance");

            int a = att.getInt("attended");
            int ab = att.getInt("absent");
            int r = att.getInt("remaining");
            int oneMoreBunkP=(int)(((double)a/(a+ab+1.0))*100);
            int tb = (int) ((r+a+ab)*0.15);
            int bunks = tb - ab ;
            int risky=(int)((r+a+ab)*0.25)-ab;
            obj.put("bunks",bunks);
            obj.put("risky",risky);
            obj.put("OneMoreBunkP",oneMoreBunkP);
            if(bunks>0)
                obj.put("possible",1);
            else
                obj.put("possible",0);

            obj.put("percentage",att.getString("percentage"));
            data.put(i,obj);
        }

        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            jsonValues.add(data.getJSONObject(i));
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "bunks";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = String.valueOf(a.get(KEY_NAME));
                    valB = String.valueOf(b.get(KEY_NAME));
                }
                catch (JSONException e) {
                }
                //return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                return -valA.compareTo(valB);
            }
        });
        for (int i = 0; i < data.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bunk_details",sortedJsonArray.toString());
        editor.apply();
    }


}



