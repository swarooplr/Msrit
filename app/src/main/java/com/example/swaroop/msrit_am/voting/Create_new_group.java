package com.example.swaroop.msrit_am.voting;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by swaroop on 11/20/2016.
 */

public class Create_new_group {

    DatabaseReference databaseReference;
    String name,admin_name,pass,admin_pass;

    DataSnapshot data;
    Context context;

    public Create_new_group(DatabaseReference databaseReference, String name, String pass, String admin_name, String admin_pass, Context context) {

        this.databaseReference = databaseReference;
        this.name = name;
        this.pass = pass;
        this.admin_name = admin_name;
        this.admin_pass = admin_pass;
        this.context=context;
    }

    public int new_group()
    {
          try{
            new  check_if_group_exists().execute();
              return 1;

          }
          catch (Exception e)
          {
              return 0;
          }
    }

    class check_if_group_exists extends AsyncTask<String,String,DataSnapshot>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DataSnapshot dataSnapshot) {
           // super.onPostExecute(dataSnapshot);
            Log.d("hmm","--");

            if(!dataSnapshot.child("groups").hasChild(name))
            {     Log.d("hmm","--oo--");
                try {
                    DatabaseReference grp =  databaseReference.child("vote_groups").child(name);
                    grp.child(votepage_info_setter.bunk).setValue("0");
                    grp.child(votepage_info_setter.donotbunk).setValue("0");
                    grp.child(votepage_info_setter.admin_name).setValue(admin_name);
                    grp.child(votepage_info_setter.admin_password).setValue(admin_pass);
                    grp.child(votepage_info_setter.bunkers).child("List of bunkers").setValue("default");
                    grp.child(votepage_info_setter.nonbunkers).child("List of non bunkers").setValue("default");
                    grp.child(votepage_info_setter.grp_members_name).child(admin_name).setValue("voter");
                    grp.child(votepage_info_setter.subject).setValue("subject here");
                    grp.child(votepage_info_setter.total_members).setValue("1");

                    databaseReference.child("groups").child(name).setValue(pass);

                    Toast.makeText(context,"Created",Toast.LENGTH_SHORT).show();

                    Get_list_of_groups get_list_of_groups=new Get_list_of_groups();
                    get_list_of_groups.getlist();

                }
                catch (Exception e)
                {
                    Toast.makeText(context,"NOT Created",Toast.LENGTH_SHORT).show();
                }

            }
            else
                    Toast.makeText(context,"Group name alredy taken",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected DataSnapshot doInBackground(String... params) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    data=dataSnapshot;
                    //string=dataSnapshot.getValue(String.class);

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

