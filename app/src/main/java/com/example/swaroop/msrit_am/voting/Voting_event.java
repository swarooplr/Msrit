package com.example.swaroop.msrit_am.voting;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by swaroop on 11/20/2016.
 */

public class Voting_event {
    DataSnapshot data;
    DatabaseReference databaseReference;
    String current_user;

    private   int bunk_vote_count,donot_bunk_vote_count;
    String bunk_count_string,non_bunk_count_string;

    public Voting_event(DataSnapshot data, DatabaseReference databaseReference, String current_user) {
        this.data = data;
        this.databaseReference = databaseReference;
        this.current_user=current_user;
    }

    void bunk_for_first_time()
    {

        bunk_count_string=data.child("bunk").getValue(String.class);
        non_bunk_count_string=data.child("donotbunk").getValue(String.class);
        Log.d("see",bunk_count_string);

        bunk_vote_count=Integer.parseInt(bunk_count_string)+1;
        //donot_bunk_vote_count=Integer.parseInt(non_bunk_count_string)-1;

        databaseReference.child("bunk").setValue(String.valueOf(bunk_vote_count));
        //databaseReference.child("donotbunk").setValue(String.valueOf(donot_bunk_vote_count));

        databaseReference.child(votepage_info_setter.bunkers).child(current_user).setValue("voted");

        try{
            votepage_info_setter.nonbunkers_list.remove(current_user);
        }
        catch (Exception e)
        {

        }

    }

    void bunk_second_response()
    {

        bunk_count_string=data.child("bunk").getValue(String.class);
        non_bunk_count_string=data.child("donotbunk").getValue(String.class);
        Log.d("see",bunk_count_string);

        bunk_vote_count=Integer.parseInt(bunk_count_string)+1;
        donot_bunk_vote_count=Integer.parseInt(non_bunk_count_string)-1;

        databaseReference.child("bunk").setValue(String.valueOf(bunk_vote_count));
        databaseReference.child("donotbunk").setValue(String.valueOf(donot_bunk_vote_count));

        databaseReference.child(votepage_info_setter.nonbunkers).child(current_user).setValue(null);
        databaseReference.child(votepage_info_setter.bunkers).child(current_user).setValue("voted again");

        try{
            votepage_info_setter.nonbunkers_list.remove(current_user);
        }
        catch (Exception e)
        {

        }
    }

    void donot_bunk_for_first_time()
    {
        bunk_count_string=data.child("bunk").getValue(String.class);
        non_bunk_count_string=data.child("donotbunk").getValue(String.class);
        Log.d("see",bunk_count_string);

        //bunk_vote_count=Integer.parseInt(bunk_count_string)-1;
        donot_bunk_vote_count=Integer.parseInt(non_bunk_count_string)+1;

        //databaseReference.child("bunk").setValue(String.valueOf(bunk_vote_count));
        databaseReference.child("donotbunk").setValue(String.valueOf(donot_bunk_vote_count));

        databaseReference.child(votepage_info_setter.nonbunkers).child(current_user).setValue("voted");

        try{
            votepage_info_setter.bunkers_list.remove(current_user);
        }
        catch (Exception e)
        {

        }



    }

    void donot_bunk_second_response()
    {

        bunk_count_string=data.child("bunk").getValue(String.class);
        non_bunk_count_string=data.child("donotbunk").getValue(String.class);
        Log.d("see",bunk_count_string);

        bunk_vote_count=Integer.parseInt(bunk_count_string)-1;
        donot_bunk_vote_count=Integer.parseInt(non_bunk_count_string)+1;

        databaseReference.child("bunk").setValue(String.valueOf(bunk_vote_count));
        databaseReference.child("donotbunk").setValue(String.valueOf(donot_bunk_vote_count));

        databaseReference.child(votepage_info_setter.nonbunkers).child(current_user).setValue("voted");
        databaseReference.child(votepage_info_setter.bunkers).child(current_user).setValue(null);

        try{
            votepage_info_setter.bunkers_list.remove(current_user);
        }
        catch (Exception e)
        {

        }


    }
}
