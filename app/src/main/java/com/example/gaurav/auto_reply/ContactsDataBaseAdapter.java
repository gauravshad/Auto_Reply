package com.example.gaurav.auto_reply;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactsDataBaseAdapter
{
    static final String DATABASE_NAME = "contacts.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"CONTACTS"+
            "( " +"ID"+" integer primary key autoincrement,"+ "PROFILE text,NAME text,NUMBER text); ";
    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelperc dbHelper;
    public  ContactsDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelperc(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  ContactsDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String profile, String name, String number)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("PROFILE", profile);
        newValues.put("NAME", name);
        newValues.put("NUMBER", number);
        // Insert the row into your table
        db.insert("CONTACTS", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }
    public int deleteEntry(String profile)
    {
        //String id=String.valueOf(ID);
        String where="PROFILE=?";
        int numberOFEntriesDeleted= db.delete("CONTACTS", where, new String[]{profile}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    public int deleteContact(String profile, String name)
    {
        //String id=String.valueOf(ID);
        String where="PROFILE=? and NAME=?";
        int numberOFEntriesDeleted= db.delete("CONTACTS", where, new String[]{profile,name}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }



    public String getContact(String profile, String number)
    {
        Cursor cursor=db.query("CONTACTS", null, " PROFILE = ? AND NUMBER = ?", new String[]{profile, number}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            String x = "not exist";
            return x;
        }
        cursor.moveToFirst();
        String name= cursor.getString(cursor.getColumnIndex("NAME"));
        cursor.close();
        return name;
    }

    public String getcount(String profile)
    {
        Cursor cursor=db.query("CONTACTS", null, "PROFILE =?", new String[]{profile}, null, null, null);
        int count=cursor.getCount();

        cursor.close();
        return String.valueOf(count);
    }

    public String getname(String profile,Integer lc)
    {
        Cursor cursor=db.query("CONTACTS", null, " PROFILE=?", new String[]{profile},null, null, null);
        if(cursor.getCount()<1) // DOES Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToPosition(lc);
        String name= cursor.getString(cursor.getColumnIndex("NAME"));
        cursor.close();
        return name;
    }

    public String getnumber(String profile,Integer lc)
    {
        Cursor cursor=db.query("CONTACTS", null, " PROFILE=?", new String[]{profile},null, null, null);
        if(cursor.getCount()<1) // DOES Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToPosition(lc);
        String number= cursor.getString(cursor.getColumnIndex("NUMBER"));
        cursor.close();
        return number;
    }


  /*
    public String getprofile(Integer c)
    {
        Cursor cursor=db.query("PROFILES", null, null, null, null, null, null);
        if(cursor.getCount()<1) // DOES Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToPosition(c);
        String name= cursor.getString(cursor.getColumnIndex("NAME"));
        cursor.close();
        return name;
    }

    public void  updateEntry(String profile,String name,String message)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("NAME", name);
        updatedValues.put("MESSAGE",message);

        String where="NAME = ?";
        db.update("PROFILES", updatedValues, where, new String[]{profile});
    }
*/

}


