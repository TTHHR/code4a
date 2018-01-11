package cn.qingyuyu.code4a.util;

/**
 * Created by harrytit on 2017/10/10.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cn.qingyuyu.commom.SomeValue;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "code.db";
    private String TBL_NAME = "Articles";
    private String CLEAR = "drop table if exists "+TBL_NAME;
    private String QUERY = "select * from "+TBL_NAME;
    private String CREATE_TBL = " create table "
            + " Articles(id integer,title text,slug text,user integer,created integer,modify integer,category integer, cover integer,views integer,status integer,mAbstract text);";
    private SQLiteDatabase db;

   public DbHelper(Context c) {
        super(c, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TBL);
    }

    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TBL_NAME, null, values);
        db.close();
    }

    public Cursor query() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME,null);
        Cursor c = db.rawQuery(QUERY, null);
        return c;
    }

    public void clear() {
        if (db == null)
            db = getWritableDatabase();
        db.execSQL(CLEAR);
        db.execSQL(CREATE_TBL);
    }
    public void close() {
        if (db != null)
            db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}