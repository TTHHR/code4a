package cn.qingyuyu.code4a.util;

/**
 * Created by harrytit on 2017/10/10.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.qingyuyu.code4a.model.Database;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "code.db";
    private String TBL_NAME = "";
    private String CREATE_TBL = " create table "
            + " CollTbl(_id integer primary key autoincrement,name text,url text, desc text) ";
    private SQLiteDatabase db;

    DbHelper(Context c, Database dbmodel) {
        super(c, DB_NAME, null, 2);
        this.TBL_NAME = dbmodel.getTabltName();
        this.CREATE_TBL = dbmodel.getCreate();
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
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
        return c;
    }

    public void del(int id) {
        if (db == null)
            db = getWritableDatabase();
        db.delete(TBL_NAME, "id=?", new String[]{String.valueOf(id)});
    }

    public void close() {
        if (db != null)
            db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}