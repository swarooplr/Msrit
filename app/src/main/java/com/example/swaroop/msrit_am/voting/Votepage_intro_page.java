package com.example.swaroop.msrit_am.voting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swaroop.msrit_am.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Votepage_intro_page extends AppCompatActivity {

    public Button submit,entergroup;
    public Button register;
    EditText name,password;
    TextView person_name;
    static boolean succesfull_login=false;
    public String grp_name;
    Context context=this;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.main_vote_database_link);
    static DataSnapshot allgroupsdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.votepage_intro);

        //submit=(Button)findViewById(R.id.submit);
        register=(Button)findViewById(R.id.register);
        entergroup=(Button)findViewById(R.id.enter);

        name=(EditText) findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        person_name=(TextView) findViewById(R.id.person_name);

        Bundle bundle=getIntent().getExtras();
        person_name.setText(bundle.getString("person_name"));
        if(person_name.getText().toString().equals("Error please try to login"))
        {
           // submit.setVisibility(View.GONE);
            register.setVisibility(View.GONE);
        }
        else
        {
           Get_list_of_groups listgetter=new Get_list_of_groups();
            listgetter.getlist();
        }






       /* submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grp_name=name.getText().toString();
                if(grp_name.length()!=0) {

                    verify_group verifyGroup = new verify_group(name.getText().toString(), password.getText().toString());
                    verifyGroup.checker();
                    Toast.makeText(Votepage_intro_page.this,"Sumition sent",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Votepage_intro_page.this,"Enter name pls",Toast.LENGTH_SHORT).show();




            }
        });*/



        entergroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //////////////
                try{

                if(allgroupsdata.hasChild(name.getText().toString())&&allgroupsdata.child(name.getText().toString()).getValue().toString().equals(password.getText().toString()))
                {
                    Intent intent=new Intent(Votepage_intro_page.this,VotePage.class);
                    intent.putExtra("grp_name",name.getText().toString());
                    intent.putExtra("person_name",person_name.getText().toString());

                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Votepage_intro_page.this,"Invalid inputs",Toast.LENGTH_SHORT).show();
                }
                }
                catch (Exception e)
                {
                    Toast.makeText(Votepage_intro_page.this,"Error in loading ... Please try again later",Toast.LENGTH_LONG).show();
                }


                //////////////


               /* if(succesfull_login)
                {
                    Intent intent=new Intent(Votepage_intro_page.this,VotePage.class);
                    intent.putExtra("grp_name",grp_name);
                    intent.putExtra("person_name",person_name.getText().toString());

                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(Votepage_intro_page.this,"NO SUBMTION OR INVALID INFO",Toast.LENGTH_LONG);
                }
               */

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder= new AlertDialog.Builder(Votepage_intro_page.this);

                final View view = LayoutInflater.from(Votepage_intro_page.this).inflate(R.layout.create_new_group,null);

                builder.setView(view)
                        .setTitle("Create new Group")
                        .setCancelable(false)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText name= (EditText)view.findViewById(R.id.group_name_set);
                                EditText grp_password=(EditText)view.findViewById(R.id.password_set);
                                EditText admin_name=(EditText)view.findViewById(R.id.admin_name);
                                admin_name.setText(person_name.getText().toString());
                                EditText admin_pass=(EditText)view.findViewById(R.id.admin_password);
                                databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(Firebaselinks_for_vote.main_vote_database_link);

                                Create_new_group create_new_group=new Create_new_group(databaseReference,name.getText().toString(),grp_password.getText().toString(),admin_name.getText().toString(),admin_pass.getText().toString(),Votepage_intro_page.this);
                                int x=create_new_group.new_group();


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




    static void setSuccesfull_login(Boolean b)
    {
        if(b)
            succesfull_login=true;
        else
            succesfull_login=false;
    }



}
