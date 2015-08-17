package com.example.yugenshtil.torontoparkguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yugenshtil on 15/08/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Parks.db";
    public static final String TABLE_NAME = "Parks_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "LOCATION";
    public static final String COL_4 = "POSTALCODE";
    public static final String COL_5 = "FACILITIES";


    public DatabaseHelper(Context context) {
        //DATABASE WILL BE CREATED
        super(context, DATABASE_NAME, null, 1);
        Log.d("Ole", "Table Constructor");
        // instatnce of sqllite db

    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        boolean check = true;
        try {
            String DB_FULL_PATH = "/data/data/com.example.yugenshtil.torontoparkguide/databases/Parks.db";
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            check = false;
        }
        return check;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //execute query we have passed
        Log.d("Ole", "Table onCreate");
        db.execSQL("create table " + TABLE_NAME + " (ID integer primary key AUTOINCREMENT, NAME TEXT, LOCATION TEXT, POSTALCODE TEXT, FACILITIES TEXT) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Ole", "Table Drop");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String name, String loc, String code, String facilities) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, loc);
        contentValues.put(COL_4, code);
        contentValues.put(COL_5, facilities);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;


    }
}