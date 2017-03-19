package com.example.gaurav.auto_reply;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProfileDataBaseAdapter
{
    static final String DATABASE_NAME = "profiles.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"PROFILES"+
            "( " +"ID"+" integer primary key autoincrement,"+ "NAME text,MESSAGE text); ";
    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelperp dbHelper;
    public  ProfileDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelperp(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  ProfileDataBaseAdapter open() throws SQLException
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

    public void insertEntry(String name, String message)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("NAME", name);
        newValues.put("MESSAGE", message);
        // Insert the row into your table
        db.insert("PROFILES", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }
    public int deleteEntry(String name)
    {
        //String id=String.valueOf(ID);
        String where="NAME=?";
        int numberOFEntriesDeleted= db.delete("PROFILES", where, new String[]{name}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    public String getMessage(String name)
    {
        Cursor cursor=db.query("PROFILES", null, " NAME=?", new String[]{name}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String message= cursor.getString(cursor.getColumnIndex("MESSAGE"));
        cursor.close();
        return message;
    }

    public String getcount()
    {
        Cursor cursor=db.query("PROFILES", null, null, null, null, null, null);
        int count=cursor.getCount();

        cursor.close();
        return String.valueOf(count);
    }

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


}


