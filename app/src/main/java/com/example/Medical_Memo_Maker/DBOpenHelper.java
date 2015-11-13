package com.example.Medical_Memo_Maker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{

    //Constants for db name and version
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 3;

    //Constants for identifying table and columns
    public static final String TABLE_NOTES = "notes";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "noteText";
    public static final String NOTE_CREATED = "noteCreated";
    public static final String NOTE_NAME = "noteName";
    public static final String NOTE_AGE = "noteAge";
    public static final String NOTE_LAST_UPDATED = "lastUpdated";
    public static final String NOTE_FEE = "noteFee";
    public static final String NOTE_MYIMAGELINK = "noteMyImageLink";


    public static final String[] ALL_COLUMNS =
            {NOTE_ID, NOTE_TEXT, NOTE_CREATED, NOTE_NAME, NOTE_AGE, NOTE_LAST_UPDATED, NOTE_FEE, NOTE_MYIMAGELINK};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TEXT + " TEXT, " +
                    NOTE_CREATED + " TEXT, " +
                    NOTE_NAME + " TEXT, " +
                    NOTE_AGE + " INTEGER, " +
                    NOTE_LAST_UPDATED + " TEXT, " +
                    NOTE_FEE + " REAL, " +
                    NOTE_MYIMAGELINK + " TEXT" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
}
