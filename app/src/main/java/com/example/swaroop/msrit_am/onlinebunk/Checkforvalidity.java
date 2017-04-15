package com.example.swaroop.msrit_am.onlinebunk;

import android.content.Context;
import android.widget.Toast;

import com.example.swaroop.msrit_am.Login_form;

/**
 * Created by swaroop on 2/12/2017.
 */

public class Checkforvalidity extends  Thread{

    Context context;
    public Checkforvalidity(Context cont)
    {   super();
        this.context=cont;

        start();


    }


    public void run() {
        try {



                Toast.makeText(this.context,"Check inputs",Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {

        }
    }
}