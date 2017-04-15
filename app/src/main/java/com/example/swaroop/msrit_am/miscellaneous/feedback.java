package com.example.swaroop.msrit_am.miscellaneous;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.swaroop.msrit_am.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * Created by swaroop on 2/13/2017.
 */

public class feedback extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);


        ImageButton send = (ImageButton) findViewById(R.id.submitfeed);
        final EditText feedback = (EditText) findViewById(R.id.feedback_text);


        Intent yes = getIntent();
        final String usn = yes.getExtras().getString("usn");



        try {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedback.getText().toString().length() > 0) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://msritam-d58b1.firebaseio.com/feedback");

                        databaseReference.child(usn).setValue(feedback.getText().toString());
                        Toast.makeText(feedback.this, "Feed back sent....", Toast.LENGTH_SHORT).show();


                    }
                }
            });


        } catch (Exception e) {
            Toast.makeText(feedback.this, "Error ", Toast.LENGTH_SHORT).show();
        }


    }
}
