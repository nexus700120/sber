package ru.sber.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.sber.App;
import ru.sber.converter.domain.RatesResponse;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class RatesTable {

    private final static String TABLE_NAME = "rates";
    private final static String COLUMN_NAME = "name";
    private final static String COLUMN_CHAR_CODE = "char_code";
    private final static String COLUMN_VALUE = "value";


    public static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME + " TEXT, " +
            COLUMN_CHAR_CODE + " TEXT, " +
            COLUMN_VALUE + " REAL);";

    public static void replace(List<RatesResponse.CurrencyInfo> currencyInfoList) {
        if (currencyInfoList == null || currencyInfoList.isEmpty()) {
            return;
        }

        synchronized (RatesTable.class) {
            SQLiteDatabase db = DataBaseManager.getInstance(App.getAppContext()).openDatabase();
            db.beginTransaction();
            db.delete(TABLE_NAME, null, null);
            for (RatesResponse.CurrencyInfo currencyInfo : currencyInfoList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, currencyInfo.name);
                values.put(COLUMN_CHAR_CODE, currencyInfo.charCode);
                values.put(COLUMN_VALUE, currencyInfo.value);
                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            DataBaseManager.getInstance(App.getAppContext()).closeDatabase();
        }
    }

    @NonNull
    public static List<RatesResponse.CurrencyInfo> get() {
        synchronized (RatesTable.class) {
            List<RatesResponse.CurrencyInfo> currencyInfoList = new ArrayList<>();
            SQLiteDatabase db = DataBaseManager.getInstance(App.getAppContext()).openDatabase();
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    RatesResponse.CurrencyInfo currencyInfo = new RatesResponse.CurrencyInfo();

                    currencyInfo.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    currencyInfo.charCode = cursor.getString(cursor.getColumnIndex(COLUMN_CHAR_CODE));
                    currencyInfo.value = cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE));

                    currencyInfoList.add(currencyInfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            DataBaseManager.getInstance(App.getAppContext()).closeDatabase();
            return currencyInfoList;
        }
    }

    public static void clear() {
        synchronized (RatesTable.class) {
            SQLiteDatabase db = DataBaseManager.getInstance(App.getAppContext()).openDatabase();
            db.beginTransaction();
            db.delete(TABLE_NAME, null, null);
            db.setTransactionSuccessful();
            db.endTransaction();
            DataBaseManager.getInstance(App.getAppContext()).closeDatabase();
        }
    }
}
