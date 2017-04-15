package com.example.swaroop.msrit_am.onlinebunk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.swaroop.msrit_am.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class online_bunk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis);

        Intent yes = getIntent();
        String usn = yes.getExtras().getString("usn");
        String dob = yes.getExtras().getString("dob");

        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);

        try {
            JSONObject response = new JSONObject(sharedPref.getString(usn + dob, null));
            display(response);
        } catch (JSONException e) {
            Log.d("error ",e.toString());
            e.printStackTrace();
        }
    }

    public void display(JSONObject obj) throws JSONException {

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        JSONArray arr = obj.getJSONArray("courses");
        JSONArray arr1 = sort(arr);
        writedata(arr1);

        RecyclerView.Adapter adapter = new online_bunk_card_adapter(arr1);
        recyclerView.setAdapter(adapter);
    }

    public JSONArray sort(JSONArray jsonArr) throws JSONException {

        JSONArray data = new JSONArray();
        for(int i =0 ;i<jsonArr.length();i++)
        {
            JSONObject obj = new JSONObject();
            JSONObject temp = jsonArr.getJSONObject(i);

            obj.put("name",temp.getString("name"));
            obj.put("code",temp.getString("code"));

            JSONObject att = temp.getJSONObject("attendance");

            //int p = att.getInt("percentage");
            int a,ab,r;
            try {
                a = att.getInt("attended");
            }
            catch (Exception e)
            {
                a=0;
            }
            try {
                 ab = att.getInt("absent");
            }
            catch (Exception e)
            {
                ab=0;
            }
            try {
                r = att.getInt("remaining");
            }catch (Exception e)
            {
                r=0;
            }

            //Toast.makeText(getApplicationContext(),"a/(a+ab+1)="+(double)a/(a+ab+1),Toast.LENGTH_LONG).show();
            int oneMoreBunkP=(int)(((double)a/(a+ab+1.0))*100);
            //Toast.makeText(getApplicationContext(),"Onemorebunkp="+oneMoreBunkP,Toast.LENGTH_LONG).show();
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
        return sortedJsonArray;
    }

    public void writedata(JSONArray arr)
    {
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bunk_details",arr.toString());
        editor.apply();
    }

}
