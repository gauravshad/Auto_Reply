package com.example.gaurav.auto_reply;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class addprofile extends Activity {

    ProfileDataBaseAdapter profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprofile);
     profile = new ProfileDataBaseAdapter(this);
        profile = profile.open();

    }


    public void onCancelButtonClicked (View view)
    {

        this.finish();
    }

    public void onSaveButtonClicked (View view)
    {

        EditText name = (EditText) findViewById(R.id.name);
        EditText message = (EditText) findViewById(R.id.message);

        String pname = name.getText().toString();
        String pmessage = message.getText().toString();

        if(pname.equals("")||pmessage.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
           profile.insertEntry(pname,pmessage);
            Toast.makeText(getApplicationContext(), "Profile Successfully Saved", Toast.LENGTH_LONG).show();

            this.finish();

        }


    }


}
