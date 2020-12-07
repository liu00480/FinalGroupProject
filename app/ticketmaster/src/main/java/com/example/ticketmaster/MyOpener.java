package com.example.ticketmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class handle database
 */
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

    /**
     * Constructor
     * @param ctx Context: used for locating paths to the the database.
     */
    public MyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Called when the activity is starting.
     * @param db SQLiteDatabase: The database
     */
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

    /**
     * Called when the database needs to be upgraded.
     * @param db SQLiteDatabase: The database
     * @param oldVersion int: The old database version
     * @param newVersion int: The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Called when the database needs to be downgraded.
     * @param db SQLiteDatabase: The database
     * @param oldVersion oldVersion int: The old database version
     * @param newVersion newVersion int: The new database version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

