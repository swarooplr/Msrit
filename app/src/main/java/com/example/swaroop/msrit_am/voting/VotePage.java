package com.example.swaroop.msrit_am.voting;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swaroop.msrit_am.R;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by swaroop on 11/13/2016.
 */

public class VotePage extends AppCompatActivity {

    TextView subject,group_name;
    Button bunk,do_not_bunk,new_subject;
    PieChart bunk_info;
    ListView listView;
    public  int bunk_vote_count,donot_bunk_vote_count,total_members_count;

    Map<String,String> map;

    public String group_title,current_user_name;

    DatabaseReference databaseReference;
    public DataSnapshot data;
    public Activity activity=this;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.votepage);

        do_not_bunk=(Button)findViewById(R.id.notbunk);
        new_subject=(Button)findViewById(R.id.add_new);
        bunk=(Button)findViewById(R.id.bunk);


        Bundle bundle=getIntent().getExtras();
        final String group_name_to_open =bundle.getString("grp_name");
        current_user_name=bundle.getString("person_name");

        group_name=(TextView)findViewById(R.id.Group_name);
        group_name.setText(group_name_to_open);

        group_title=group_name_to_open;

        subject=(TextView)findViewById(R.id.subject);

        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.each_group_link+"/"+group_title);
        new fetch_group_info().execute();

        Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();

        //subject.setText(subject_palce);




        bunk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(data.child(votepage_info_setter.bunkers).hasChild(current_user_name))
                {
                    Toast.makeText(VotePage.this,"Already voted",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(data.child(votepage_info_setter.nonbunkers).hasChild(current_user_name))
                {
                    new Voting_event(data, databaseReference,current_user_name).bunk_second_response();

                    Toast.makeText(VotePage.this, "Your previous response has been changed", Toast.LENGTH_SHORT).show();

                    new fetch_group_info().execute();
                }
                else
                {

                    new Voting_event(data, databaseReference,current_user_name).bunk_for_first_time();

                    Toast.makeText(VotePage.this, "Voted", Toast.LENGTH_SHORT).show();

                    new fetch_group_info().execute();
                }


            }
        });

        do_not_bunk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(data.child(votepage_info_setter.nonbunkers).hasChild(current_user_name))
                {
                    Toast.makeText(VotePage.this,"Already voted",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(data.child(votepage_info_setter.bunkers).hasChild(current_user_name))
                {
                    new Voting_event(data, databaseReference,current_user_name).donot_bunk_second_response();

                    Toast.makeText(VotePage.this, "Your previous response has been changed", Toast.LENGTH_SHORT).show();

                    new fetch_group_info().execute();
                }
                else
                {

                    new Voting_event(data, databaseReference,current_user_name).donot_bunk_for_first_time();

                    Toast.makeText(VotePage.this, "Voted", Toast.LENGTH_SHORT).show();

                    new fetch_group_info().execute();
                }



            }
        });

        new_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(VotePage.this);

                final View view = LayoutInflater.from(VotePage.this).inflate(R.layout.new_subject,null);

                builder.setView(view)
                        .setTitle("Create new Group")
                        .setCancelable(false)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText name= (EditText)view.findViewById(R.id.new_sub);
                                EditText admin_pass=(EditText)view.findViewById(R.id.admin_password1);

                                if(admin_pass.getText().toString().equals(data.child(votepage_info_setter.admin_password).getValue(String.class)))
                                {
                                    try {
                                        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.each_group_link+"/"+group_title);
                                        DatabaseReference grp =  databaseReference;
                                        grp.child(votepage_info_setter.bunk).setValue("0");
                                        grp.child(votepage_info_setter.donotbunk).setValue("0");

                                        grp.child(votepage_info_setter.bunkers).setValue(null);
                                        grp.child(votepage_info_setter.nonbunkers).setValue(null);

                                        grp.child(votepage_info_setter.bunkers).child("List of bunkers").setValue("default");
                                        grp.child(votepage_info_setter.nonbunkers).child("List of non bunkers").setValue("default");

                                        grp.child(votepage_info_setter.subject).setValue(name.getText().toString());
                                        grp.child(votepage_info_setter.total_members).setValue("1");







                                    }
                                    catch (Exception e)
                                    {
                                            Log.d("here","ksam");
                                    }
                                }


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();



            }
        });









    }

    class fetch_group_info extends AsyncTask<String,String,DataSnapshot>
    {
        @Override
        protected void onPreExecute() {
            databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.each_group_link+"/"+group_title);

        }

        @Override
        protected void onPostExecute(DataSnapshot dataSnapshot) {
            //super.onPostExecute(dataSnapshot);
            votepage_info_setter vi=new votepage_info_setter(VotePage.this,data);
            vi.setinformation(VotePage.this,dataSnapshot);
            if(!data.child(votepage_info_setter.grp_members_name).hasChild(current_user_name))
            {
                databaseReference.child(votepage_info_setter.grp_members_name).child(current_user_name).setValue("voter");
            }




        }

        @Override
        protected DataSnapshot doInBackground(String... params) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data=dataSnapshot;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            while (data==null)
            {

            }


            return data;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_list,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.refresh:
            {
                new fetch_group_info().execute();
                break;
            }
            case R.id.gm:
            {
                AlertDialog.Builder builder =new AlertDialog.Builder(VotePage.this);
                final View view = LayoutInflater.from(VotePage.this).inflate(R.layout.alert_diagloue_for_list,null);
                builder.setView(view)
                        .setTitle("Group members")
                        .setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                ArrayAdapter arrayAdapter=new ArrayAdapter(VotePage.this,android.R.layout.simple_list_item_1,votepage_info_setter.grp_members_name_list);
                ListView listView=(ListView)view.findViewById(R.id.list1);
                listView.setAdapter(arrayAdapter);

                builder.show();
                break;
            }
            case R.id.bu:
            {
                AlertDialog.Builder builder =new AlertDialog.Builder(VotePage.this);
                final View view = LayoutInflater.from(VotePage.this).inflate(R.layout.alert_diagloue_for_list,null);
                builder.setView(view)
                        .setTitle("Bunkers")
                        .setCancelable(false)

                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                ArrayAdapter arrayAdapter=new ArrayAdapter(VotePage.this,android.R.layout.simple_list_item_1,votepage_info_setter.bunkers_list);
                ListView listView=(ListView)view.findViewById(R.id.list1);
                listView.setAdapter(arrayAdapter);
                builder.show();
                break;

            }
            case R.id.nb:
            {
                AlertDialog.Builder builder =new AlertDialog.Builder(VotePage.this);
                final View view = LayoutInflater.from(VotePage.this).inflate(R.layout.alert_diagloue_for_list,null);
                builder.setView(view)
                        .setTitle("Non Bunkers")
                        .setCancelable(false)

                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                ArrayAdapter arrayAdapter=new ArrayAdapter(VotePage.this,android.R.layout.simple_list_item_1,votepage_info_setter.nonbunkers_list);
                ListView listView=(ListView)view.findViewById(R.id.list1);
                listView.setAdapter(arrayAdapter);
                        builder.show();
                break;
            }

        }


        return true;

    }





}
