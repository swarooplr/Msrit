package com.example.swaroop.msrit_am.voting;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by swaroop on 2/13/2017.
 */

public class Get_list_of_groups  {

    DataSnapshot data;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.main_vote_database_link).child("groups");

    public void getlist()
    {
        new group().execute();
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

          Votepage_intro_page.allgroupsdata=dataSnapshot;


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
