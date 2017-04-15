package com.example.swaroop.msrit_am.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swaroop.msrit_am.Login_form;
import com.example.swaroop.msrit_am.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class addTimeTable extends AppCompatActivity{

    String[]  days={"monday","tuesday","wednesday","thursday","friday","saturday"};
    Spinner hr1,hr2,hr3,hr4,hr5,hr6,hr7;
    TextView dotw;
    Button submit,nextday,upload,ok;
    JSONObject timet = new JSONObject();
    int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addtimetable);
        count = 0;
        nextday = (Button) findViewById(R.id.nextday);
        submit = (Button) findViewById(R.id.submit);
        upload = (Button) findViewById(R.id.upload);

        nextday.setEnabled(true);
        submit.setEnabled(false);
        upload.setEnabled(false);

        dotw = (TextView) findViewById(R.id.dotw);

        hr1 = (Spinner) findViewById(R.id.hr1);
        hr2 = (Spinner) findViewById(R.id.hr2);
        hr3 = (Spinner) findViewById(R.id.hr3);
        hr4 = (Spinner) findViewById(R.id.hr4);
        hr5 = (Spinner) findViewById(R.id.hr5);
        hr6 = (Spinner) findViewById(R.id.hr6);
        hr7 = (Spinner) findViewById(R.id.hr7);

        /////////////////subject spinners
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sharedPref.getString("subject_list", null));
            String[] sub = new String[arr.length()+1];
            sub[0] = "No Class";
            for (int i = 1; i < arr.length()+1; i++) {
                sub[i] = arr.getString(i-1);
                //Toast.makeText(this,sub[i],Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adap21 = new ArrayAdapter<>(addTimeTable.this, android.R.layout.simple_spinner_item, sub);
            adap21.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hr1.setAdapter(adap21);
            hr2.setAdapter(adap21);
            hr3.setAdapter(adap21);
            hr4.setAdapter(adap21);
            hr5.setAdapter(adap21);
            hr6.setAdapter(adap21);
            hr7.setAdapter(adap21);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dotw.setText(days[count]);

        nextday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    get_data();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    count++;
                    if(count<=5)
                        dotw.setText(days[count]);

                    if (count==5)
                    {
                        nextday.setEnabled(false);
                        submit.setEnabled(true);
                        upload.setEnabled(true);}
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    upload_tt();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void get_data() throws JSONException {
        JSONArray arr = new JSONArray();
        String first = hr1.getSelectedItem().toString();
        arr.put(0,first);

        String second = hr2.getSelectedItem().toString();
        arr.put(1,second);

        String third = hr3.getSelectedItem().toString();
        arr.put(2,third);

        String four = hr4.getSelectedItem().toString();
        arr.put(3,four);

        String fifth = hr5.getSelectedItem().toString();
        arr.put(4,fifth);

        String sixth = hr6.getSelectedItem().toString();
        arr.put(5,sixth);

        String seventh = hr7.getSelectedItem().toString();
        arr.put(6,seventh);

        timet.put(days[count],arr);
    }

    public void done() {
        try {
            get_data();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,timet.toString(),Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TimeTable",timet.toString());
        editor.commit();
        Toast.makeText(this, "timetable saved", Toast.LENGTH_LONG).show();
    }

    public  void upload_tt() throws JSONException {

        AlertDialog.Builder builder = new AlertDialog.Builder(addTimeTable.this);
        LayoutInflater inflater = (LayoutInflater)addTimeTable.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View theView = inflater.inflate(R.layout.upload_tt_details_selection, null);

        String[]  dept={"ise","cse","ec"};
        String[]  sem={"1","2","3","4","5","6","7","8"};
        String[]  sec={"a","b","c"};

        final Spinner sdept = (Spinner) theView.findViewById(R.id.dept);
        final Spinner ssec = (Spinner) theView.findViewById(R.id.sec);
        final Spinner ssem = (Spinner) theView.findViewById(R.id.sem);

        ArrayAdapter<String> adap = new ArrayAdapter<>(addTimeTable.this, android.R.layout.simple_spinner_item, dept);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sdept.setAdapter(adap);

        ArrayAdapter<String> adap1 = new ArrayAdapter<>(addTimeTable.this, android.R.layout.simple_spinner_item, sec);
        adap1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssec.setAdapter(adap1);

        ArrayAdapter<String> adap2 = new ArrayAdapter<>(addTimeTable.this, android.R.layout.simple_spinner_item, sem);
        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssem.setAdapter(adap2);

        builder.setTitle("Time Table details");
        builder.setView(theView)
                .setPositiveButton("Set",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dept = sdept.getSelectedItem().toString();
                        String sec = ssec.getSelectedItem().toString();
                        String sem = ssem.getSelectedItem().toString();
                        try {
                            start_upload_to_fibase(dept,sem,sec);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void start_upload_to_fibase(String deptf,String semf,String secf) throws JSONException {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://msritam-d58b1.firebaseio.com/timetable");

        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        JSONArray arr = new JSONArray(sharedPref.getString("last_user",null));
        String name  = arr.getString(1);

        databaseReference.child(deptf).child(semf).child(secf).child(name).setValue(timet.toString());
        Toast.makeText(this, "timetable uploaded", Toast.LENGTH_LONG).show();
    }
}
