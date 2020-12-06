package com.example.ticketmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "TicketDB";
    protected final static int VERSION_NUM = 4;
    public final static String TABLE_NAME = "TICKET_INFO";
    public final static String COL_EVENT_NAME = "EVENT_NAME";
    public final static String COL_START_DATE = "START_DATE";
    public final static String COL_PRICE_MAX = "PRICE_MAX";
    public final static String COL_PRICE_MIN = "PRICE_MIN";
    public final static String COL_URL = "URL";
    public final static String COL_IMG_URL = "IMG_URL";
    public final static String COL_ID = "id";

    public MyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
                        + COL_ID+ " INTEGER PRIMARY KEY,"
                        + COL_EVENT_NAME + " text,"
                        + COL_START_DATE + " text,"
                        + COL_PRICE_MAX + " text,"
                        + COL_PRICE_MIN + " text,"
                        + COL_URL + " text,"
                        + COL_IMG_URL + " text);"
                        );
    }

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

