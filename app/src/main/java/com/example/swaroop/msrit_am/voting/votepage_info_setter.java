package com.example.swaroop.msrit_am.voting;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by swaroop on 11/18/2016.
 */

public class votepage_info_setter {
    Context context;
    DataSnapshot data;

    static String bunk="bunk";
    static String donotbunk="donotbunk";
    static  String bunkers="bunkers";
    static String nonbunkers="nonbunkers";
    static String admin_name="admin_name";
    static String admin_password="admin_password";
    static String total_members="total_members";
    static String subject="subject";
    static  String grp_members_name="grp_members_name";




    static String bunk_value;
    static String donotbunk_value;
    static String admin_name_value;
    static String admin_password_value;
    static String total_members_value;
    static String subject_value;




    static  ArrayList<String> grp_members_name_list = new ArrayList<>();
    static  ArrayList<String> bunkers_list = new ArrayList<>();
    static  ArrayList<String> nonbunkers_list = new ArrayList<>();




    public votepage_info_setter(Context context, DataSnapshot data) {
        this.context = context;
        this.data = data;
    }

    void setinformation(Context context1,DataSnapshot data)
    {
       Set_wedges set_wedges=new Set_wedges(context);
        try {
            bunk_value = data.child(bunk).getValue(String.class);
            donotbunk_value = data.child(donotbunk).getValue(String.class);
            total_members_value = String.valueOf(data.child(grp_members_name).getChildrenCount());


            admin_name_value = data.child(admin_name).getValue(String.class);
           admin_password_value = data.child(admin_password).getValue(String.class);

            subject_value = data.child(subject).getValue(String.class);

            int i=0;
            grp_members_name_list.clear();
            for (DataSnapshot temp : data.child(grp_members_name).getChildren()) {
                grp_members_name_list.add(i,temp.getKey());
                i++;
            }
            i=0;
            bunkers_list.clear();
            for (DataSnapshot temp : data.child(bunkers).getChildren()) {
                bunkers_list.add(i,temp.getKey());
                i++;
            }
            i=0;
            nonbunkers_list.clear();
            for (DataSnapshot temp : data.child(nonbunkers).getChildren()) {
                nonbunkers_list.add(i,temp.getKey());
                i++;
            }

        }
        catch (Exception e)
        {
            Log.d("somthing ","wrong");
        }

        set_wedges.setSubject(subject_value);
        set_wedges.setPiechart(bunk_value,donotbunk_value,total_members_value);
        //set_wedges.set_list(grp_members_name_list);






    }


}
