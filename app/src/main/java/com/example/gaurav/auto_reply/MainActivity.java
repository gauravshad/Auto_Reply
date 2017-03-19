package com.example.gaurav.auto_reply;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.View;
import android.content.Intent;
import android.widget.RadioButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;
import android.content.pm.ActivityInfo;
import android.content.ComponentName;

public class MainActivity extends AppCompatActivity {

    public String profile="none";
    StateDataBaseAdapter state;
    ProfileDataBaseAdapter profiles;
    SettingsDataBaseAdapter settings;
    SharedPreferences sp;
    int f1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent s =new Intent(getApplicationContext(),splash.class);
        startActivity(s);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(myToolbar);


        final ListView listView = (ListView) findViewById(R.id.listview);

        state = new StateDataBaseAdapter(this);
        state = state.open();

        profiles = new ProfileDataBaseAdapter(this);
        profiles = profiles.open();

        settings = new SettingsDataBaseAdapter(this);
        settings = settings.open();

        sp = getSharedPreferences("flag", 0);
        f1 = sp.getInt("fl", 0);
        /*RadioButton meeting = (RadioButton) findViewById(R.id.meeting);
        RadioButton busy = (RadioButton) findViewById(R.id.busy);
        RadioButton sleep = (RadioButton) findViewById(R.id.sleep);*/
        ToggleButton autoreply = (ToggleButton) findViewById(R.id.MainToggleButton);


        final String prevprofile = state.getSinlgeEntry();

        if(!(prevprofile.equals("NOT EXIST")))
        {
            autoreply.setChecked(true);
        }

        if(f1==0) {
            f1 = 1;
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("fl", f1);
            edit.commit();
            Intent i = new Intent(getApplicationContext(), help.class);
            startActivity(i);

            profiles.insertEntry("Meeting", "I am in a meeting right now. I'll call you back.");
            profiles.insertEntry("Gym", "I am in gym right now.I'll call you back");
            profiles.insertEntry("Busy", "I am busy right now. I'll call you back");

            settings.insertEntry("sendto","all");
            settings.insertEntry("notification","on");

        }

        Feedlistview();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setLongClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int x = position;
                profile = (String) listView.getItemAtPosition(x);
            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                //Toast.makeText(getApplicationContext(), "Long Clicked : ", Toast.LENGTH_LONG).show();
                int x = position;
                String p = (String) listView.getItemAtPosition(x);

                Intent i = new Intent(getApplicationContext(), editprofile.class);
                i.putExtra("profile", p);
                startActivity(i);


                return true;
            }
        });




    }

    public void Feedlistview() {

    String count = profiles.getcount();
    String[] profilelist = new String[Integer.parseInt(count)];
        for (int i=0;i<Integer.parseInt(count);i++)
        {
            profilelist[i] = profiles.getprofile(i);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listview, R.id.label, profilelist);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current state

        // Always call the superclass so it can save the view hierarchy state

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Feedlistview();
        // Always call the superclass so it can restore the view hierarchy

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.help) {
            Intent i = new Intent(getApplicationContext(),help.class);
            startActivity(i);
        }
        else if (id == R.id.settings)
        {
            Intent i = new Intent(getApplicationContext(),settings.class);
            startActivity(i);
        }
        else
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    public void onMainToggleClicked(View view) {

        // Is AutoReply enabled?
        boolean on = ((ToggleButton) view).isChecked();

                if(on)
                {
                    if(profile.equals("none"))
                    {
                        Toast.makeText(getApplicationContext(), "Select a profile and then press Enable",
                                Toast.LENGTH_LONG).show();
                        ToggleButton autoreply = (ToggleButton) findViewById(R.id.MainToggleButton);
                        autoreply.setChecked(false);
                    }
                    else {
                        state.insertEntry(profile);

                        Intent Missedcallintent = new Intent(this, Missedcallservice.class);

                        Missedcallintent.putExtra("profile", profile);
                        startService(Missedcallintent);
                    }
                }

                else
                {

                    int n = state.deleteEntry();
                    profile="none";
                    /*RadioButton meeting = (RadioButton) findViewById(R.id.meeting);
                    RadioButton busy = (RadioButton) findViewById(R.id.busy);
                    RadioButton sleep = (RadioButton) findViewById(R.id.sleep);
                    meeting.setChecked(false);
                    busy.setChecked(false);
                    sleep.setChecked(false);
                    */
                    Intent Missedcallintent = new Intent(this, Missedcallservice.class);
                    stopService(Missedcallintent);
                }
    }

        public void onAddButtonClicked (View view)
        {
            Intent i = new Intent(this, addprofile.class);
            startActivity(i);

        }

        public void onWhatsappClicked (View v)
        {

            Intent sendIntent = new Intent();

            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! This app named Auto-Reply helps me when i am not available to take calls. Check it out.");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(sendIntent,"Share with"));



        }


}
