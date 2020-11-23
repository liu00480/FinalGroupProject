package com.example.finalgroupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener_liu extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "TicketDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "TICKET_INFO";
    public final static String COL_EVENT_NAME = "EVENT_NAME";
    public final static String COL_START_DATE = "START_DATE";
    public final static String COL_PRICE_RANGE = "PRICE_RANGE";
    public final static String COL_URL = "URL";

    public MyOpener_liu(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
                + COL_EVENT_NAME + " text PRIMARY KEY,"
                + COL_START_DATE + " text,"
                + COL_PRICE_RANGE + " text,"
                + COL_URL + " text);");  // add or remove columns
    }

    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
