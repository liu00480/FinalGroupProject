package com.example.audiodatabaseapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * ALBUM_DATABASE - this my database class
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class ALBUM_DATABASE  extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "Audio Database api";
    public final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "track";
//this is the database created

    public final static String ID_HEADER39 = "strAlbum";
    public final static String ID_HEADER40 = "strArtist";
    public final static String ID_HEADER50 = "strSong";

    /**
     * calling the super class that extends this class through
     * a no arg constructor
     * @param context
     */
    public ALBUM_DATABASE(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);

    }


    /**
     * adding my columns
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_HEADER39 + " text,"
                + ID_HEADER40 + " text,"
                + ID_HEADER50 + " text);");
    }

    /**
     * Drop the old table
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db); }

    /**
     * Drop the old table
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db); }


    /**
     * inserting data method checker
     * @param album
     * @return
     */
    public boolean insertData(String album) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_HEADER39, album);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            return false; }
        return true; }


    /**
     * read  data method checker from table

     *  {@linktourl  https://stackoverflow.com/questions/9280692/android-sqlite-select-query}
     */
    public Cursor ReadDATA() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from " + TABLE_NAME,null);
        return cursor; }
}
