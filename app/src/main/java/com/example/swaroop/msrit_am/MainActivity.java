package com.example.swaroop.msrit_am;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.swaroop.msrit_am.miscellaneous.feedback;
import com.example.swaroop.msrit_am.offlinebunk.offline_bunk;
import com.example.swaroop.msrit_am.onlinebunk.online_bunk;
import com.example.swaroop.msrit_am.timetable.TimeTable;
import com.example.swaroop.msrit_am.timetable.addTimeTable;
import com.example.swaroop.msrit_am.timetable.getTimeTable;
import com.example.swaroop.msrit_am.voting.Votepage_intro_page;
import com.example.swaroop.msrit_am.webviewforsis.sisweb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String name="",usn="",dob="";
    Button offline,vote,online,timetable,refresh,suggestion;

    ProgressBar pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        online=(Button)findViewById(R.id.sis);
        offline=(Button)findViewById(R.id.offline);
        vote=(Button)findViewById(R.id.vote);
        timetable=(Button)findViewById(R.id.tt);
        suggestion=(Button) findViewById(R.id.suggestion);
        refresh=(Button) findViewById(R.id.refresh);
        pro=(ProgressBar) findViewById(R.id.progressBar);
        pro.setIndeterminate(true);

        //floating bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,sisweb.class);
                startActivity(intent);


                /*if(isNetworkAvailable())
                { Snackbar.make(view, "INTERNET CONNECTION AVALIABLE", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();}
                else
                {Snackbar.make(view, "INTERNET CONNECTION NOT AVALIABLE", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();}*/
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /////////////////////////////////////////////

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(usn.length()>4){
                    Intent intent =new Intent(MainActivity.this,online_bunk.class);
                    intent.putExtra("usn",usn);
                    intent.putExtra("dob",dob);
                    startActivity(intent);}
                else {
                        Toast.makeText(MainActivity.this,"Please login ...",Toast.LENGTH_SHORT).show();
                    }

            }
        });

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,Votepage_intro_page.class);
                String error_less_name=name.replace('.',' ');          //for the cursious case of brunda.l

                intent.putExtra("person_name",error_less_name);
                startActivity(intent);


            }
        });

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,offline_bunk.class);
                startActivity(intent);
            }
        });

        offline.setVisibility(View.GONE);

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,TimeTable.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!usn.equalsIgnoreCase("") && !dob.equalsIgnoreCase("")) {
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String url = "http://sismsrit.azurewebsites.net/api/login?usn=" + usn + "&dob=" + dob;
                    if (isNetworkAvailable()) {
                        pro.setVisibility(View.VISIBLE);

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {


                                        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString(usn + dob, response.toString());
                                        editor.apply();

                                        pro.setVisibility(View.INVISIBLE);
                                        Toast.makeText(MainActivity.this, "REFRESHED", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO Auto-generated method stub

                                    }
                                });


                        queue.add(jsObjRequest);
                    } else {
                        Toast.makeText(MainActivity.this, "NO NETWORK", Toast.LENGTH_SHORT).show();
                    }



                }
                else
                {
                    Intent intent = new Intent(MainActivity.this,Login_form.class);
                    startActivityForResult(intent,1);
                }

            }
        });

        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,bunkSuggestions.class);
                if(usn.length()>4) {

                    intent.putExtra("usn", usn);
                    intent.putExtra("dob", dob);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please login again...",Toast.LENGTH_SHORT).show();
                }


            }
        });


        try {
            login_past();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            Intent intent = new Intent(MainActivity.this,Login_form.class);
            startActivityForResult(intent,1);


        }

        else if(id==R.id.addtt){
            Intent tt = new Intent(MainActivity.this, addTimeTable.class);
            startActivity(tt);

            //Toast.makeText(MainActivity.this,"Roopak S\nSwarooop  L R\nVaibhav G N\nRakshith Gowda\nShreyansh H Desai",Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.gettt){
            Intent tt = new Intent(MainActivity.this, getTimeTable.class);
            startActivity(tt);

            //Toast.makeText(MainActivity.this,"Roopak S\nSwarooop  L R\nVaibhav G N\nRakshith Gowda\nShreyansh H Desai",Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.credits)
        {

        }
        else if(id==R.id.feedback)
        {
           if(usn.length()>4)
           {
               Intent intent=new Intent(MainActivity.this,feedback.class);
               intent.putExtra("usn",usn);
               startActivity(intent);
           }
        }
        else if(id==R.id.help)
        {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                {
                    String name1 = data.getExtras().getString("name");
                    String usn1 = data.getExtras().getString("usn");
                    String dob1 = data.getExtras().getString("dob");
                    setActivityvalues(name1,usn1,dob1);
                    set_nav_header_main(name1, usn1);
                    Toast.makeText(MainActivity.this, "successfully logged in ", Toast.LENGTH_LONG).show();


                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    //Write your code if there's no result
                }
            }

        }
    }

    public void setActivityvalues(String n,String u,String d)
    {
        name = n;
        usn = u;
        dob = d;
    }

    public  void  set_nav_header_main(String name1,String usn1)
    {
        try{

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerview=navigationView.getHeaderView(0);
            TextView nameinnav_header=(TextView)headerview.findViewById(R.id.name_display);
            TextView usninnav_header=(TextView)headerview.findViewById(R.id.usn_display);

            nameinnav_header.setText(name1);
            usninnav_header.setText(usn1);
        }
        catch (Exception e){
        }
    }

    public void login_past() throws JSONException {
        SharedPreferences sharedPref = this.getSharedPreferences("all_user_details", Context.MODE_PRIVATE);
        if(!sharedPref.contains("last_user"))
        {
            Intent intent = new Intent(MainActivity.this,Login_form.class);

            startActivityForResult(intent,1);
        }
        else
        {
            JSONArray arr = new JSONArray(sharedPref.getString("last_user",null));
            String u = arr.getString(0);
            String d = arr.getString(2);
            JSONObject obj = new JSONObject(sharedPref.getString(u+d,null));
            String n = obj.getString("name");
            setActivityvalues(n,u,d);
            set_nav_header_main(n,u);
        }
    }

}
