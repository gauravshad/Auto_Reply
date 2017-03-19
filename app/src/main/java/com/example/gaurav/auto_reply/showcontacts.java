package com.example.gaurav.auto_reply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.provider.ContactsContract;
import android.database.Cursor;

public class showcontacts extends Activity
{
    int count=0;
    MyCustomAdapter dataAdapter = null;
    ContactsDataBaseAdapter contacts;
    String profile;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcontacts);

        contacts = new ContactsDataBaseAdapter(this);
        contacts.open();

        Intent i =getIntent();
        profile = i.getStringExtra("profile");
        //Generate list View from ArrayList
        displayListView();

        checkButtonClick();

    }

    public void onSearchClicked(View view)
    {

    }
    private void displayListView()
    {

        //Array list of countries
        ArrayList<Contacts> stateList = new ArrayList<Contacts>();

        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);

        while (phones.moveToNext()) {


            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

           String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Contacts _states = new Contacts(phoneNumber,name,false);
            stateList.add(_states);

            count++;

        }

        phones.close();

        Collections.sort(stateList, new Comparator<Contacts>() {
            @Override
            public int compare(Contacts lhs, Contacts rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });


        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,R.layout.listview2, stateList);
        ListView listView = (ListView) findViewById(R.id.listview2);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // When clicked, show a toast with the TextView text
                Contacts state = (Contacts) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"Clicked on Row: " + state.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<Contacts>
    {

        private ArrayList<Contacts> stateList;

        public MyCustomAdapter(Context context, int textViewResourceId,

                               ArrayList<Contacts> stateList)
        {
            super(context, textViewResourceId, stateList);
            this.stateList = new ArrayList<Contacts>();
            this.stateList.addAll(stateList);
        }

        private class ViewHolder
        {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            ViewHolder holder = null;

            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null)
            {

                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate(R.layout.listview2, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);

                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        CheckBox cb = (CheckBox) v;
                        Contacts _state = (Contacts) cb.getTag();

                        Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();

                        _state.setSelected(cb.isChecked());
                    }
                });

            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            Contacts state = stateList.get(position);

            holder.code.setText(" (" + state.getCode() + ")");
            holder.name.setText(state.getName());
            holder.name.setChecked(state.isSelected());

            holder.name.setTag(state);

            return convertView;
        }

    }

    private void checkButtonClick()
    {

        ImageButton myButton = (ImageButton) findViewById(R.id.tick);

        myButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                contacts.deleteEntry(profile);

                //StringBuffer responseText = new StringBuffer();
                //responseText.append("The following were selected...\n");

                ArrayList<Contacts> stateList = dataAdapter.stateList;

                for(int i=0;i<stateList.size();i++)
                {
                    Contacts state = stateList.get(i);

                    if(state.isSelected())
                    {
                       // responseText.append("\n" + state.getName());
                        String n = state.getCode();
                        String number;

                        n = n.replaceAll("[^0-9]","");

                        if(n.length()>10)
                        {

                            number = n.substring(n.length()-10,n.length());
                        }
                        else
                        {
                            number = n;
                        }
                    contacts.insertEntry(profile,state.getName(),number);
                    }
                }

                //Toast.makeText(getApplicationContext(),
                  //      responseText, Toast.LENGTH_LONG).show();
            finish();
            }
        });
    }

}