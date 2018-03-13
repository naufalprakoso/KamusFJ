package com.fj.naufalprakoso.dicodingkamusmade.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.fj.naufalprakoso.dicodingkamusmade.model.Kamus;

import java.util.ArrayList;

/**
 * Created by NaufalPrakoso on 13/03/18.
 */

public class KamusDataHelper {

    private Context context;
    private DatabaseHelper dataBaseHelper;
    private SQLiteDatabase database;

    public KamusDataHelper(Context context) {
        this.context = context;
    }

    public KamusDataHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public long insertKamusBahasa(Kamus param) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KamusColom.NAMA, param.getNama());
        initialValues.put(KamusColom.KETERANGAN, param.getKeterangan());
        return database.insert(KamusColom.TABLE_INDO_ENGLISH, null, initialValues);
    }

    public long insertKamusEnglish(Kamus param) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KamusColom.NAMA, param.getNama());
        initialValues.put(KamusColom.KETERANGAN, param.getKeterangan());
        return database.insert(KamusColom.TABLE_ENGLISH_INDO, null, initialValues);
    }

    public ArrayList<Kamus> getEnglishByNama() {
        Cursor cursor = database.query(KamusColom.TABLE_ENGLISH_INDO, null, null, null, null, null, KamusColom.ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<Kamus> arrayList = new ArrayList<>();
        Kamus kamus;
        if (cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
                kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KamusColom.ID)));
                kamus.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KamusColom.NAMA)));
                kamus.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(KamusColom.KETERANGAN)));

                arrayList.add(kamus);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;
    }

    public ArrayList<Kamus> getBahasaByNama() {
        Cursor cursor = database.query(KamusColom.TABLE_INDO_ENGLISH, null, null, null, null, null, KamusColom.ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<Kamus> arrayList = new ArrayList<>();
        Kamus kamus;
        if (cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
                kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KamusColom.ID)));
                kamus.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KamusColom.NAMA)));
                kamus.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(KamusColom.KETERANGAN)));

                arrayList.add(kamus);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public void insertTransactionBahasa(Kamus param) {
        String sql = "INSERT INTO " + KamusColom.TABLE_INDO_ENGLISH + " (" + KamusColom.NAMA + ", " + KamusColom.KETERANGAN
                + ") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, param.getNama());
        stmt.bindString(2, param.getKeterangan());
        stmt.execute();
        stmt.clearBindings();
    }

    public void insertTransactionEnglish(Kamus param) {
        String sql = "INSERT INTO " + KamusColom.TABLE_ENGLISH_INDO + " (" + KamusColom.NAMA + ", " + KamusColom.KETERANGAN
                + ") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, param.getNama());
        stmt.bindString(2, param.getKeterangan());
        stmt.execute();
        stmt.clearBindings();
    }
}
