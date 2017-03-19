package com.example.gaurav.auto_reply;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.CompoundButton;


public class settings extends AppCompatActivity {

    SettingsDataBaseAdapter settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        settings = new SettingsDataBaseAdapter(this);
        settings = settings.open();

        RadioButton all = (RadioButton) findViewById(R.id.all);
        RadioButton specific = (RadioButton) findViewById(R.id.specific);
        Switch switch1 = (Switch) findViewById (R.id.switch1);


        String value = settings.getValue("sendto");

        if (value.equals("all"))
        {
            all.setChecked(true);
            specific.setChecked(false);
        }
        else if (value.equals("specific"))
        {
            specific.setChecked(true);
            all.setChecked(false);
        }

        Button done;
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        String value1 = settings.getValue("notification");

        if(value1.equals("on"))
        {
            switch1.setChecked(true);
        }
        else {
            switch1.setChecked(false);
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {

                    settings.deleteEntry("notification");
                    settings.insertEntry("notification","on");
                }
                else
                {
                    settings.deleteEntry("notification");
                    settings.insertEntry("notification","off");
                }
            }

        });
    }


    public void onRadioButtonClicked (View view)
    {
        int id = view.getId();

        if (id == R.id.all)
        {
            int n = settings.deleteEntry("sendto");
            settings.insertEntry("sendto","all");
        }
        else if (id == R.id.specific)
        {
            int n = settings.deleteEntry("sendto");
            settings.insertEntry("sendto","specific");
        }
    }


}
