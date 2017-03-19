package com.example.gaurav.auto_reply;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;


public class editprofile extends AppCompatActivity {

    ProfileDataBaseAdapter profiles;
    String profile;
    ContactsDataBaseAdapter contacts;
    String sname = "none";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        final ListView listView = (ListView) findViewById(R.id.listview3);

        profiles = new ProfileDataBaseAdapter(this);
        profiles = profiles.open();

        contacts = new ContactsDataBaseAdapter(this);
        contacts = contacts.open();

        Intent i =getIntent();
        profile = i.getStringExtra("profile");
        String msg = profiles.getMessage(profile);

        final EditText name = (EditText) findViewById(R.id.name);
        EditText message = (EditText) findViewById(R.id.message);

        name.setText(profile);
        message.setText(msg);
        Feedlistview();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int x = position;
                sname = (String) listView.getItemAtPosition(x);
            }
        });
    }


    public void onCancelButtonClicked (View view)
    {

        this.finish();
    }

    public void onDelete (View view)
    {
        int n = profiles.deleteEntry(profile);
        Toast.makeText(getApplicationContext(), "Profile Successfully Deleted", Toast.LENGTH_LONG).show();
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
            profiles.updateEntry(profile, pname, pmessage);
            Toast.makeText(getApplicationContext(), "Profile Successfully Updated", Toast.LENGTH_LONG).show();

            this.finish();

        }


    }

    public void onAddClicked (View view)
    {
        Intent i = new Intent(getApplicationContext(), showcontacts.class);
        i.putExtra("profile",profile);
        startActivity(i);
    }

    public void onRefreshClicked (View view)
    {
       Feedlistview();
    }

    public void Feedlistview() {

        String count = contacts.getcount(profile);
        String[] contactlist = new String[Integer.parseInt(count)];
        for (int i=0;i<Integer.parseInt(count);i++)
        {
            contactlist[i] = contacts.getname(profile, i);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listview3, R.id.label3, contactlist);
        ListView listView = (ListView) findViewById(R.id.listview3);
        listView.setAdapter(adapter);

    }

    public void onRemoveClicked (View view)
    {
        if(sname.equals("none"))
        {
            Toast.makeText(getApplicationContext(), "Select a contact and then press remove",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            int n = contacts.deleteContact(profile,sname);
            Feedlistview();
        }
    }

}
