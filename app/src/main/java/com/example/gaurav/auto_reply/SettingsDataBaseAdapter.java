package com.example.gaurav.auto_reply;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SettingsDataBaseAdapter
{
    static final String DATABASE_NAME = "settings.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"SETTINGS"+
            "( " +"ID"+" integer primary key autoincrement,"+ "ITEM text,VALUE text); ";
    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelpers dbHelper;
    public  SettingsDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelpers(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  SettingsDataBaseAdapter open() throws SQLException
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

    public void insertEntry(String item, String value)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("ITEM", item);
        newValues.put("VALUE", value);
        // Insert the row into your table
        db.insert("SETTINGS", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }
    public int deleteEntry(String item)
    {
        //String id=String.valueOf(ID);
        String where="ITEM=?";
        int numberOFEntriesDeleted= db.delete("SETTINGS", where, new String[]{item}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    public String getValue(String item)
    {
        Cursor cursor=db.query("SETTINGS", null, " ITEM=?", new String[]{item}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String value= cursor.getString(cursor.getColumnIndex("VALUE"));
        cursor.close();
        return value;
    }
/*
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
*/

}


