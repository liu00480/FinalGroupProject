package com.example.recipesearchpage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "RecipeFavoriteDB";
    protected final static int VERSION_NUM = 4;
    public final static String TABLE_NAME = "RECIPEINFO";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_URL= "URL";
    public final static String COL_INGREDIENTS= "INGREDIENTS";
    //public final static String COL_ID = "_id";

    /**
     * constructor
     * @param ctx Context: to use for locating paths to the the database This value may be null.
     *            String: of the database file, or null for an in-memory database This value may be null.
     *            SQLiteDatabase.CursorFactory: to use for creating cursor objects, or null for the default This value may be null.
     *            int: number of the database (starting at 1); if the database is older, onUpgrade(SQLiteDatabase, int, int)
     *            will be used to upgrade the database; if the database is newer, onDowngrade(SQLiteDatabase, int, int)
     *            will be used to downgrade the database
     */
    public MyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.

    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, using findViewById(int)
     * to programmatically interact with widgets in the UI, calling managedQuery(android.net.Uri,
     * java.lang.String[], java.lang.String, java.lang.String[], java.lang.String) to retrieve
     * cursors for data being displayed, etc.
     * @param db SQLiteDatabase: The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
                + COL_TITLE + " text PRIMARY KEY,"
                + COL_URL + " text,"
                + COL_INGREDIENTS  + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM

    /**
     * Called when the database needs to be upgraded. The implementation should use this method
     * to drop tables, add tables, or do anything else it needs to upgrade to the new schema version.
     * @param db SQLiteDatabase: The database.
     * @param oldVersion int: The old database version.
     * @param newVersion int: The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM

    /**
     * Called when the database needs to be downgraded. This is strictly similar to onUpgrade
     * (SQLiteDatabase, int, int) method, but is called whenever current version is newer than
     * requested one. However, this method is not abstract, so it is not mandatory for a customer
     * to implement it. If not overridden, default implementation will reject downgrade and throws
     * SQLiteException
     * @param db SQLiteDatabase: The database.
     * @param oldVersion int: The old database version.
     * @param newVersion int: The new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}