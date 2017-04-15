package com.example.swaroop.msrit_am.voting;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by swaroop on 11/15/2016.
 */

public class verify_group extends Activity {

    Context context;

    String grp_name,password;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.main_vote_database_link).child("groups");

    public String string;

    public static boolean pu;
    public DataSnapshot data;




    public verify_group(String grp_name, String password) {
        this.grp_name = grp_name;
        this.password = password;


    }

    public Boolean checker()

    {   databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.main_vote_database_link).child("groups");


        new group().execute();


        return true;
    }








    class group extends AsyncTask<String,String,DataSnapshot>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DataSnapshot dataSnapshot) {
            //super.onPostExecute(map);
            if(dataSnapshot.hasChild(grp_name))
            {   if(dataSnapshot.child(grp_name).getValue(String.class).equals(password))
            {
                Votepage_intro_page.setSuccesfull_login(true);





            }

                else
            {

                Votepage_intro_page.setSuccesfull_login(false);

            }

            }
            else
                Votepage_intro_page.setSuccesfull_login(false);




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




}
