package com.example.robin.papers.demo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by space on 15/10/25.
 */
public class OrderDB extends SQLiteOpenHelper {

    public static final String ID = "_id";
    public static final String TABLE_NAME = "orders";
    public static final String PAPERNAME = "papername";
    public static final String PAGE = "page";
    public static final String COUNT = "count";
    public static final String TYPE = "type";

    public OrderDB(Context context) {
        super(context, "orders", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PAPERNAME + " TEXT NOT NULL,"
                + PAGE + " INT(11) NOT NULL,"
                + TYPE + " TEXT NOT NULL,"
                + COUNT + " INT(11) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public JSONArray getJSONArray() {
        SQLiteDatabase dbWriter;
        dbWriter = getWritableDatabase();
        Cursor cursor;
        cursor = dbWriter.query(TABLE_NAME, null, null, null, null, null, null);
        JSONObject tObj;
        JSONArray tList;
        tList = new JSONArray();
        while(cursor.moveToNext())
        {
            JSONObject detail = new JSONObject();

            String paperName = cursor.getString(cursor.getColumnIndex(PAPERNAME));
            String count = cursor.getString(cursor.getColumnIndex(COUNT));
            String page = cursor.getString(cursor.getColumnIndex(PAGE));
            try {
                detail.put("name", paperName);
                detail.put("page", page);
                detail.put("count", count);
            } catch (Exception e)
            {
                //fuck
            }
            tList.put(detail);
        }
        return tList;
    }
}