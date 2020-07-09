package com.ziko.isaac.easysalepost.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ziko.isaac.easysalepost.SQLite.EasySaleContract.*;

import androidx.annotation.Nullable;

public class EasySaleDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "easysalelist.db";
    public static final int DATABASE_VERSION = 1;

    public EasySaleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EASYSALE_TABLE = "CREATE TABLE " +
                EasySaleEntry.TABLE_NAME + " (" +
                EasySaleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EasySaleEntry.COLUMN_NAME + " TEXT, " +
                EasySaleEntry.COLUMN_IMAGE + " TEXT, " +
                EasySaleEntry.COLUMN_PRICE + " INTEGER, " +
                EasySaleEntry.COLUMN_QUANTITY + " INTEGER" +
                ");";
        db.execSQL(SQL_CREATE_EASYSALE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + EasySaleEntry.TABLE_NAME);
        onCreate(db);
    }
}
