package com.example.swaroop.msrit_am.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.swaroop.msrit_am.MainActivity;
import com.example.swaroop.msrit_am.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Roopak on 2/8/2017.
 */

public class getTimeTable extends AppCompatActivity {

    Spinner ssem,ssec,sdept;

    String[]  days={"monday","tuesday","wednesday","thursday","friday","saturday"};
    String[]  dept={"ise","cse","ec"};
    String[]  sem={"1","2","3","4","5","6","7","8"};

    String[]  sec={"a","b","c"};

    String seletectedsem,selecteddept,selectedsec;

    Button go;
    ListView listofuploader;

    DataSnapshot alltimetable;
    ArrayList<String> listofalluploaders=new ArrayList<>();

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(timetablefirebaselink.timetable_database_link);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gettimetable);

        //gets all timetable information
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alltimetable=dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                alltimetable=null;
            }
        });



        ssem  = (Spinner) findViewById(R.id.sem);
        ssec  = (Spinner) findViewById(R.id.sec);
        sdept  = (Spinner) findViewById(R.id.dept);

        listofuploader =(ListView)findViewById(R.id.timetablenames);

        go = (Button) findViewById(R.id.get);

        ssem.setEnabled(true);
        ssec.setEnabled(true);
        sdept.setEnabled(true);

        ArrayAdapter<String> adap = new ArrayAdapter<>(getTimeTable.this, android.R.layout.simple_spinner_item, dept);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sdept.setAdapter(adap);

        ArrayAdapter<String> adap1 = new ArrayAdapter<>(getTimeTable.this, android.R.layout.simple_spinner_item, sec);
        adap1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssec.setAdapter(adap1);

        ArrayAdapter<String> adap2 = new ArrayAdapter<>(getTimeTable.this, android.R.layout.simple_spinner_item, sem);
        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssem.setAdapter(adap2);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                selecteddept = sdept.getSelectedItem().toString();
                selectedsec = ssec.getSelectedItem().toString();
                seletectedsem = ssem.getSelectedItem().toString();


                boolean result = hastimetable(selecteddept, seletectedsem, selectedsec);

                if (result) {
                    listofalluploaders = getuploaders(selecteddept, seletectedsem, selectedsec);

                    if (!listofalluploaders.isEmpty()) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getTimeTable.this, android.R.layout.simple_list_item_1, listofalluploaders);
                        listofuploader.setAdapter(arrayAdapter);
                        listofuploader.setVisibility(View.VISIBLE);


                        listofuploader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                getTimetableformuploader(selecteddept, seletectedsem, selectedsec, listofalluploaders, position);
                                Toast.makeText(getTimeTable.this,"Select one of the uploader's timetable",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getTimeTable.this, "Time table not avaliable", Toast.LENGTH_SHORT).show();
                    }


                } else {

                }


            }catch (Exception e)
                {
                    Toast.makeText(getTimeTable.this,"Error loading timetable ,please try again later",Toast.LENGTH_SHORT).show();
                }

            }
        });







    }

    private  boolean hastimetable(String dept,String sem ,String sec)
    {
        if(alltimetable.hasChild(dept)&&alltimetable.child(dept).hasChild(sem)&&alltimetable.child(dept).child(sem).hasChild(sec))
        {
            return true;
        }


        return false;
    }

    private ArrayList<String> getuploaders(String dept, String sem , String sec)
    {
        ArrayList<String> listofuploaders=new ArrayList<>();

        DataSnapshot ds=alltimetable.child(dept).child(sem).child(sec);

        for(DataSnapshot itr:ds.getChildren())
        {
            listofuploaders.add(itr.getKey());
        }

        return listofuploaders;
    }

    private void getTimetableformuploader(String department,String sem, String sec, ArrayList<String> listofuploaders,int positon)
    {
        String timetable=alltimetable.child(department).child(sem).child(sec).child(listofuploaders.get(positon)).getValue().toString();

        Toast.makeText(getTimeTable.this,timetable,Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TimeTable",timetable);
        editor.commit();
        Toast.makeText(this, "timetable obtained", Toast.LENGTH_LONG).show();



    }


}
