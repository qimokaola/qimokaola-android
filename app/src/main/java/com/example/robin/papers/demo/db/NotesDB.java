package com.example.robin.papers.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
;
/**
 * Created by Robin on 2015/10/2.
 */
public class NotesDB extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "notes";
    public static final String ID = "_id";
    public static final String PAPERNAME = "papername";
    public static final String QNURL = "qiniuurl";
    public static final String WPURL = "wangpanurl";
    public static final String LOCALURL = "localurl";
    public static final String TYPE = "type";
    public static final String TIME = "time";

    public NotesDB(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" IF NOT EXISTS ("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +PAPERNAME+" TEXT NOT NULL,"
                +QNURL+" TEXT NOT NULL,"
                +WPURL+" TEXT NOT NULL,"
                +LOCALURL+" TEXT NOT NULL,"
                +TYPE+" TEXT NOT NULL,"
                +TIME+" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
