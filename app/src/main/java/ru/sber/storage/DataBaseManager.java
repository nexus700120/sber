package ru.sber.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vitaly Kirillov on 26.11.2014.
 */
class DataBaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();
    private DataBase mHelper;
    private static DataBaseManager sInstance;
    private SQLiteDatabase mDatabase;

    private DataBaseManager(Context context) {
        mHelper = new DataBase(context);
    }

    public static synchronized DataBaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseManager(context);
        }
        return sInstance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = mHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }
}