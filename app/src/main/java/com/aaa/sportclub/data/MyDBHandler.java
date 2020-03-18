package com.aaa.sportclub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.aaa.sportclub.data.Contacts.DATABASE_NAME;
import static com.aaa.sportclub.data.Contacts.DATABASE_VERSION;
import static com.aaa.sportclub.data.Contacts.memberEntry.COLUMN_FIRST_NAME;
import static com.aaa.sportclub.data.Contacts.memberEntry.COLUMN_GENDER;
import static com.aaa.sportclub.data.Contacts.memberEntry.COLUMN_LAST_NAME;
import static com.aaa.sportclub.data.Contacts.memberEntry.COLUMN_SPORT;
import static com.aaa.sportclub.data.Contacts.memberEntry.TABLE_NAME;
import static com.aaa.sportclub.data.Contacts.memberEntry._ID;

public class MyDBHandler extends SQLiteOpenHelper {

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MEMBERS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_GENDER + " INTEGER NOT NULL,"
                + COLUMN_SPORT + " TEXT" + ")";

        db.execSQL(CREATE_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
    }
}
