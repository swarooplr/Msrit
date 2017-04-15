package com.example.swaroop.msrit_am.offlinebunk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import java.io.IOException;


public class offline_bunk extends AppCompatActivity {


    public static final String DEFAULT = "Subject-Required percentage: 0-0-Total no.of classes: 0";

    int []bunk_number={R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,R.id.b9,R.id.b10};
    int []subnames={R.id.sn1,R.id.sn2,R.id.sn3,R.id.sn4,R.id.sn5,R.id.sn6,R.id.sn7,R.id.sn8,R.id.sn9,R.id.sn10};
    int []class_total={R.id.ct1,R.id.ct2,R.id.ct3,R.id.ct4,R.id.ct5,R.id.ct6,R.id.ct7,R.id.ct8,R.id.ct9,R.id.ct10};
    int []percentage={R.id.p1,R.id.p2,R.id.p3,R.id.p4,R.id.p5,R.id.p6,R.id.p7,R.id.p8,R.id.p9,R.id.p10};
    int []plus={R.id.plus1,R.id.plus2,R.id.plus3,R.id.plus4,R.id.plus5,R.id.plus6,R.id.plus7,R.id.plus8,R.id.plus9,R.id.plus10};
    int []minus={R.id.minus1,R.id.minus2,R.id.minus3,R.id.minus4,R.id.minus5,R.id.minus6,R.id.minus7,R.id.minus8,R.id.minus9,R.id.minus10};
    int []edit={R.id.edit1,R.id.edit2,R.id.edit3,R.id.edit4,R.id.edit5,R.id.edit6,R.id.edit7,R.id.edit8,R.id.edit9,R.id.edit10};




    Button b,btn2;


    private final String filename = "bunkoff";










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_bunk);



        int []bunk_number={R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,R.id.b9,R.id.b10};
        int []subnames={R.id.sn1,R.id.sn2,R.id.sn3,R.id.sn4,R.id.sn5,R.id.sn6,R.id.sn7,R.id.sn8,R.id.sn9,R.id.sn10};
        int []class_total={R.id.ct1,R.id.ct2,R.id.ct3,R.id.ct4,R.id.ct5,R.id.ct6,R.id.ct7,R.id.ct8,R.id.ct9,R.id.ct10};
        int []percentage={R.id.p1,R.id.p2,R.id.p3,R.id.p4,R.id.p5,R.id.p6,R.id.p7,R.id.p8,R.id.p9,R.id.p10};
        int []plus={R.id.plus1,R.id.plus2,R.id.plus3,R.id.plus4,R.id.plus5,R.id.plus6,R.id.plus7,R.id.plus8,R.id.plus9,R.id.plus10};
        int []minus={R.id.minus1,R.id.minus2,R.id.minus3,R.id.minus4,R.id.minus5,R.id.minus6,R.id.minus7,R.id.minus8,R.id.minus9,R.id.minus10};
        int []edit={R.id.edit1,R.id.edit2,R.id.edit3,R.id.edit4,R.id.edit5,R.id.edit6,R.id.edit7,R.id.edit8,R.id.edit9,R.id.edit10};


        SharedPreferences sharedPreferences = getSharedPreferences(filename,Context.MODE_PRIVATE);

        String s = sharedPreferences.getString(String.valueOf(1),DEFAULT);
        System.out.println(s);

        if(s.equals(DEFAULT) )
        {
            System.out.println("New");
        }
        else {
            for (int i = 0; i < 10; i++) {
                TextView t;


                s = sharedPreferences.getString(String.valueOf(i), DEFAULT);
                System.out.println(s);
                String a[] = s.split("-");

                t = (TextView) findViewById(subnames[i]);
                t.setText(a[0]);

                t = (TextView) findViewById(percentage[i]);
                t.setText(a[1]);

                t = (TextView) findViewById(bunk_number[i]);
                t.setText(a[2]);

                t = (TextView) findViewById(class_total[i]);
                t.setText(a[3]);


            }
        }







        b=(Button)findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sort();
            }
        });





    }
    public void sort()
    {
        try {
            TextView temp1, temp2;
            int tempno1, tempno2;
            int i, j;
            for (i = 0; i < 10; i++) {
                for (j = 0; j < 10; j++) {
                    temp1 = (TextView) findViewById(bunk_number[i]);
                    temp2 = (TextView) findViewById(bunk_number[j]);
                    tempno1 = Integer.parseInt(temp1.getText().toString());
                    tempno2 = Integer.parseInt(temp2.getText().toString());

                    if (tempno1 < tempno2) {
                        swap(i, j);
                    }
                }
            }
        }
        catch(Exception e)
        {

        }
    }

    private void swap(int i,int j)
    {
        try {
            String temp;
            TextView temp1, temp2;

            temp1 = (TextView) findViewById(bunk_number[i]);
            temp2 = (TextView) findViewById(bunk_number[j]);

            temp = temp1.getText().toString();
            temp1.setText(temp2.getText().toString());
            temp2.setText(temp);

            temp1 = (TextView) findViewById(percentage[i]);
            temp2 = (TextView) findViewById(percentage[j]);

            temp = temp1.getText().toString();
            temp1.setText(temp2.getText().toString());
            temp2.setText(temp);

            temp1 = (TextView) findViewById(class_total[i]);
            temp2 = (TextView) findViewById(class_total[j]);

            temp = temp1.getText().toString();
            temp1.setText(temp2.getText().toString());
            temp2.setText(temp);

            temp1 = (TextView) findViewById(subnames[i]);
            temp2 = (TextView) findViewById(subnames[j]);

            temp = temp1.getText().toString();
            temp1.setText(temp2.getText().toString());
            temp2.setText(temp);

        }
        catch(Exception e)
        {

        }



    }

    public void add_one(View view){

        Toast.makeText(offline_bunk.this,"Skipped",Toast.LENGTH_SHORT).show();
        int position,bunkable;
        for(position=0;position<10;++position)
        {
            if(view==findViewById(plus[position]))
                break;
        }

        TextView temp=(TextView)findViewById(bunk_number[position]);
        bunkable=Integer.parseInt(temp.getText().toString());
        --bunkable;
        temp.setText(""+String.valueOf(bunkable));


    }

    public void sub_one(View view)
    {
        int position,bunkable;
        for(position=0;position<10;position++)
        {
            if(view == findViewById(minus[position]))
                break;
        }
        TextView temp=(TextView)findViewById(bunk_number[position]);
        bunkable=Integer.parseInt(temp.getText().toString());
        ++bunkable;
        temp.setText(""+bunkable);
    }

    public void edit_box(View view)
    {
        int position;
        for(position=0;position<10;position++)
        {
            if(view==findViewById(edit[position]))
                break;
        }
        final TextView sub_name_temp=(TextView)findViewById(subnames[position]);
        final TextView bunk_no=(TextView)findViewById(bunk_number[position]);
        final TextView percentage_temp=(TextView)findViewById(percentage[position]);
        final TextView total_class_temp=(TextView)findViewById(class_total[position]);


        View edit_box_diagloue= LayoutInflater.from(offline_bunk.this).inflate(R.layout.edit_box_for_offline_bunk,null);
        final EditText course_name=(EditText)edit_box_diagloue.findViewById(R.id.course_name);
        final EditText percent=(EditText)edit_box_diagloue.findViewById(R.id.percentage);
        final EditText ct=(EditText)edit_box_diagloue.findViewById(R.id.total_classes);


        AlertDialog.Builder edit_inputs=new AlertDialog.Builder(offline_bunk.this);

        edit_inputs.setTitle("Edit")
                .setView(edit_box_diagloue)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            int bunkclaculte=0;

                            if(course_name.getText().toString().length()==0)
                                throw new IOException("empty name");
                            else{
                                bunkclaculte=(Integer.parseInt(ct.getText().toString()))-(Integer.parseInt(ct.getText().toString())*Integer.parseInt(percent.getText().toString())/100);
                                sub_name_temp.setText(course_name.getText().toString());
                                total_class_temp.setText("Total no.of classes: "+ct.getText().toString());
                                percentage_temp.setText("Required percentage:  "+percent.getText().toString());
                                bunk_no.setText(String.valueOf(bunkclaculte));}
                        }catch (Exception e){
                            Toast.makeText(offline_bunk.this,"Wrong Inputs!",Toast.LENGTH_SHORT).show();

                        }

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false)
                .show();

    }

    @SuppressLint("CommitPrefEdits")
    public void writeToFile() throws IOException {
        TextView temp,name;
        String sname = "";




        SharedPreferences sharedPreferences = getSharedPreferences(filename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sa[];
        for(int i=0;i<10;i++) {
            StringBuilder s = new StringBuilder();


            name = (TextView) findViewById(subnames[i]);
            sname = name.getText().toString();
            s.append(sname);
            s.append("-");
            temp = (TextView) findViewById(percentage[i]);
            s.append(temp.getText().toString());
            s.append("-");
            temp = (TextView) findViewById(bunk_number[i]);

            s.append(temp.getText().toString());
            s.append("-");
            temp = (TextView) findViewById(class_total[i]);
            s.append(temp.getText().toString());
            System.out.println(s);
            editor.putString(String.valueOf(i),s.toString());
            sa = s.toString().split("-");

            System.out.println(sa[0] +" * "+ sa[1] +" * "+ sa[2] +" * " +sa[3]);

        }
        editor.commit();









    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
