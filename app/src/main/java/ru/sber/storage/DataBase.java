package ru.sber.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vitaly Kirillov on 25.09.2014.
 */
class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sber.db";
    private static final int DATABASE_VERSION = 1;

    DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(RatesTable.CREATE_SQL);
    }
}