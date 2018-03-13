package com.fj.naufalprakoso.dicodingkamusmade.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NaufalPrakoso on 13/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "db_kamus";
    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_INDO_ENGLISH = "create table " + KamusColom.TABLE_INDO_ENGLISH +
            " (" + KamusColom.ID + " integer primary key autoincrement, " +
            KamusColom.NAMA + " text not null, " +
            KamusColom.KETERANGAN + " text not null);";

    public static String CREATE_TABLE_ENGLISH_INDO = "create table " + KamusColom.TABLE_ENGLISH_INDO +
            " (" + KamusColom.ID + " integer primary key autoincrement, " +
            KamusColom.NAMA + " text not null, " +
            KamusColom.KETERANGAN + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INDO_ENGLISH);
        db.execSQL(CREATE_TABLE_ENGLISH_INDO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KamusColom.TABLE_INDO_ENGLISH);
        db.execSQL("DROP TABLE IF EXISTS " + KamusColom.TABLE_ENGLISH_INDO);
        onCreate(db);
    }
}
