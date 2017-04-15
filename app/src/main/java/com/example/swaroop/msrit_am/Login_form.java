package com.example.swaroop.msrit_am;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.swaroop.msrit_am.onlinebunk.Checkforvalidity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Login_form extends AppCompatActivity {

    EditText usn, dob;
    Button load_local;
    TextView tt;
    ProgressBar pb;
    CheckBox cb;

    String login_save = "login_save";
    String usn_list = "usn_list";
    String last_user = "last_user";
    String all_users_details = "all_user_details";

    static String swr="usless";
    Context context=this;


    public Boolean gotinfo=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        pb = (ProgressBar) findViewById(R.id.progressBar2) ;
        pb.setIndeterminate(true);
        usn = (EditText) findViewById(R.id.usn);
        dob = (EditText) findViewById(R.id.dob);
        cb=(CheckBox) findViewById(R.id.savecb);
        final Spinner spinner = (Spinner) findViewById(R.id.usnspinner);
        load_local = (Button) findViewById(R.id.offline);

        Checkforvalidity checkforvalidity=new Checkforvalidity(Login_form.this);

        //fills the spinner with previously logged in usns
        SharedPreferences sharedPref = getActivity().getSharedPreferences(all_users_details, Context.MODE_PRIVATE);
            if(sharedPref.contains(usn_list)) {
                Set<String> usns1 = sharedPref.getStringSet(usn_list, null);
                int i=1;
                String usns[] = new String[usns1.size()+1];
                usns[0] = "Select USN";
                for (String s : usns1) {
                    usns[i] = s;i++;
                }
                ArrayAdapter<String> spinneradapter = new ArrayAdapter<>(Login_form.this, android.R.layout.simple_spinner_item, usns);
                spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinneradapter);
            }
            else
            {
                Toast.makeText(this,"no usn list",Toast.LENGTH_SHORT).show();
            }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selusn = adapterView.getItemAtPosition(i).toString();
                setusnanddob(selusn);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Load last known data button
        load_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    load_local_data();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public Context getActivity() {
        return this;
    }

    //Login button ...makes the volly request
    public void stt(View view) {
        String u = usn.getText().toString();
        String d = dob.getText().toString();



        if (((u.length() != 0) && (d.length() != 0))) {




            if (isNetworkAvailable()) {

                try{
                    Firebase_sis_data firebase_sis_data=new Firebase_sis_data(u,d,context);
                }
                catch (Exception e)
                {
                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                     finish();
                }
                pb.setVisibility(View.VISIBLE);


            } else {
                Toast.makeText(this, "NO NETWORK", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(
                    getApplicationContext(),
                    "Please enter both USN and DOB!", Toast.LENGTH_LONG).
                    show();



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //returns to main activity on response
    public void localWrite(JSONObject obj,String u,String d) throws IOException, JSONException {

        Log.d(u,d);
        Log.d("info3",obj.toString());
        SharedPreferences sharedPref = this.getSharedPreferences(all_users_details, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(u+d,obj.toString());
        editor.apply();

        saveusn(u);
        createsubjectlist(obj);
        if(cb.isChecked())
        {
            save_login_details();
        }

        //saves last login user details
        JSONArray arr = new JSONArray();
        arr.put(0,u);arr.put(1,obj.getString("name"));arr.put(2,d);
        editor.putString(last_user,arr.toString());
        editor.apply();

        Toast.makeText(this,obj.getString("name"),Toast.LENGTH_LONG).show();

        //create bunks jsonarray
        //create_bunks_array(obj.getJSONArray("courses"));
        Intent intent=new Intent();
        intent.putExtra("name",obj.getString("name"));
        intent.putExtra("dob",d);
        intent.putExtra("usn",u);
        setResult(RESULT_OK,intent);

        finish();
    }

    //saves usn to list of logged in usns
    public void saveusn(String usn)
    {
        SharedPreferences sharedPref = this.getSharedPreferences(all_users_details, Context.MODE_PRIVATE);
        if(!sharedPref.contains(usn_list))
        {
            HashSet<String> usns= new HashSet<>();
            usns.add(usn);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putStringSet(usn_list,usns);
            editor.apply();
        }
        else
        {
            Set<String> usns = sharedPref.getStringSet(usn_list,null);
            if(usns.contains(usn))
            {
                return;
            }
            else
            {
                usns.add(usn);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putStringSet(usn_list,usns);
                editor.apply();
            }
        }
    }

    //creates the list of subjects of this student
    public void createsubjectlist(JSONObject obj) throws JSONException {
        JSONArray subject_list = new JSONArray();
        JSONObject temp;
        JSONArray courses = obj.getJSONArray("courses");

        for(int i=0;i<courses.length();i++)
        {
            temp  = courses.getJSONObject(i);
            String sub  = temp.getString("name");
            subject_list.put(i,sub);
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences(all_users_details, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("subject_list",subject_list.toString());

        editor.apply();
    }

    //saves usn and dob if needed
    public void setusnanddob(String usn1) {
        usn = (EditText) findViewById(R.id.usn);
        dob = (EditText) findViewById(R.id.dob);

        if(!usn1.equalsIgnoreCase("Select USN")) {
            usn.setText(usn1);
        }
        SharedPreferences sharedPref = this.getSharedPreferences(all_users_details, Context.MODE_PRIVATE);
        if(sharedPref.contains(login_save))
        {
            try {
                JSONObject obj = new JSONObject(sharedPref.getString(login_save,null));
                String dob1 = obj.getString(usn1);
                dob.setText(dob1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //reads old data if found
    public void load_local_data() throws JSONException {
        usn = (EditText) findViewById(R.id.usn);
        dob = (EditText) findViewById(R.id.dob);



        String u = usn.getText().toString();
        String d = dob.getText().toString();

        SharedPreferences sharedPref = this.getSharedPreferences(all_users_details, Context.MODE_PRIVATE);
        if(!sharedPref.contains(u+d))
        {
            Toast.makeText(this,"NO PREVIOUS LOGIN FOUND",Toast.LENGTH_LONG).show();
        }
        else
        {
            JSONObject obj = new JSONObject(sharedPref.getString(u+d,null));
            Toast.makeText(this,obj.getString("name"),Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.putExtra("name",obj.getString("name"));
            intent.putExtra("dob",d);
            intent.putExtra("usn",u);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    //creates the bunks array for bunk suggestion and online bunk

    //saves login data
    public void save_login_details()
    {
        String usn1 = usn.getText().toString();
        String dob1 = dob.getText().toString();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(all_users_details, Context.MODE_PRIVATE);
        if(!sharedPref.contains(login_save))
        {
            JSONObject obj = new JSONObject();
            try {
                obj.put(usn1,dob1);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(login_save,obj.toString());
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                JSONObject obj = new JSONObject(sharedPref.getString(login_save,null));
                obj.put(usn1,dob1);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(login_save,obj.toString());
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //alert dialog for calender picker
    public void opencalender(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)Login_form.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View theView = inflater.inflate(R.layout.calender_view, null);

        final NumberPicker npy = (NumberPicker) theView.findViewById(R.id.npy);
        final NumberPicker npm = (NumberPicker) theView.findViewById(R.id.npm);
        final NumberPicker npd = (NumberPicker) theView.findViewById(R.id.npd);

        builder.setTitle("Choose DOB");
        builder.setView(theView)
                .setPositiveButton("Set",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = npy.getValue();
                        int month = npm.getValue();
                        int day = npd.getValue();
                        dob = (EditText) findViewById(R.id.dob);
                        if(month<10&day<10)
                            dob.setText(year+"-0"+month+"-0"+day);
                        else if(day<10)
                            dob.setText(year+"-"+month+"-0"+day);
                        else if(month<10)
                            dob.setText(year+"-0"+month+"-"+day);
                        else
                            dob.setText(year+"-"+month+"-"+day);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        npy.setMinValue(1990);
        npy.setMaxValue(2000);
        npy.setValue(1997);

        npd.setMinValue(1);
        npd.setMaxValue(31);
        npd.setValue(15);

        npm.setMinValue(1);
        npm.setMaxValue(12);
        npm.setValue(5);

        builder.show();
    }
    public class Firebase_sis_data {

        DatabaseReference userslink=FirebaseDatabase.getInstance().getReferenceFromUrl("https://msritam-d58b1.firebaseio.com/sisdata/users");
        DatabaseReference attendancelink=FirebaseDatabase.getInstance().getReferenceFromUrl("https://msritam-d58b1.firebaseio.com/sisdata/attendance_data");
        DatabaseReference requestslink=FirebaseDatabase.getInstance().getReferenceFromUrl("https://msritam-d58b1.firebaseio.com/sisdata/current_requests");

        String usn,dob;
        Context context;



        public Firebase_sis_data(String usn, String dob, Context context) {
            this.dob = dob;
            this.usn = usn;
            this.context =context;
            new getsisdatafirebase().execute();


        }



        public class getsisdatafirebase extends AsyncTask<String,String,String>
        {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


            }

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

                userslink.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(usn))
                        {
                            String pass=dataSnapshot.child(usn).getValue(String.class);
                            if(pass.equals(dob))
                            {
                                Toast.makeText(context,"User identified....",Toast.LENGTH_SHORT).show();
                                attendancelink.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(usn))
                                        {


                                            Login_form.swr = dataSnapshot.child(usn).getValue(String.class);
                                            Log.d("info",Login_form.swr);

                                            try {
                                                JSONObject jsonObject = new JSONObject(Login_form.swr);
                                                Log.d("info2",jsonObject.toString());
                                                localWrite(jsonObject,usn,dob);

                                            }
                                            catch (Exception e)
                                            {
                                                Log.d( "df",e.toString());
                                            }


                                            Toast.makeText(context,Login_form.swr,Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(context,"error loading data please try again..",Toast.LENGTH_SHORT).show();
                                            makearequest(usn,dob);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(context,"Check credentials...",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {                   Toast.makeText(context,"If you are a first time user please try again later",Toast.LENGTH_SHORT).show();
                            makearequest(usn,dob);
                            Log.d("invalid","nouser");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }) ;





                return "succes";
            }


            void makearequest(String usn ,String dob)
            {
                requestslink.child(usn).setValue(dob);
            }
        }
    }



}

/*
if (((u.length() != 0) && (d.length() != 0))) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://sismsrit.azurewebsites.net/api/login?usn=" + u + "&dob=" + d;

        if (isNetworkAvailable()) {
        pb.setVisibility(View.VISIBLE);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

@Override
public void onResponse(JSONObject response) {

        try {
        String u = usn.getText().toString();
        String d = dob.getText().toString();

        localWrite(response,u,d);
        Checkforvalidity checkforvalidity=new Checkforvalidity(Login_form.this);
        } catch (JSONException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }

        }
        }, new Response.ErrorListener() {

@Override
public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        }
        });
        queue.add(jsObjRequest);
        } else {
        Toast.makeText(this, "NO NETWORK", Toast.LENGTH_SHORT).show();
        }
        }
        else
        Toast.makeText(
        getApplicationContext(),
        "Please enter both USN and DOB!", Toast.LENGTH_LONG).
        show();
*/
