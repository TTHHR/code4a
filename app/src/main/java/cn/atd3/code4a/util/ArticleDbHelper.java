package cn.atd3.code4a.util;

/**
 * 文章数据库存储器
 * Created by harrytit on 2017/10/10.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ArticleDbHelper extends SQLiteOpenHelper {
    //TOOD: 数据库外部存储
    private String TBL_NAME = "article_info";
    private final static  String DATABASE_NAME="code4a.db";

    private String CLEAR = "drop table if exists " + TBL_NAME;
    private String QUERY = "select * from " + TBL_NAME;


    private String CREATE_TBL = "create table " + TBL_NAME
            + "( " +
            "id integer primary key," +
            "title text," +
            "slug text," +
            "user integer," +
            "created integer," +
            "modify integer," +
            "category integer, " +
            "cover integer," +
            "views integer," +
            "status integer," +
            "abstract text);";
    private SQLiteDatabase db;

    public ArticleDbHelper(Context c) {
        super(c,DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL);
    }

    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("Article","insert into database "+values);
        db.insert(TBL_NAME, null, values);
        db.close();
    }

    public void update(ContentValues values) {
        String whereClause = "id =?";
        Log.d("Article","id "+values.getAsString("id")+" update  "+values);
        String[] whereArgs = {values.getAsString("id")};
        SQLiteDatabase db = getWritableDatabase();
        db.update(TBL_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void replace(ContentValues values) {
        SQLiteDatabase db = getReadableDatabase();
        String[] whereArgs = {values.getAsString("id")};
        String[] columns = {"id"};
        Log.d("Article","insert "+values.getAsString("id"));
        Cursor cursor = db.query(TBL_NAME, columns, "id=?", whereArgs, null, null, null);
        if (cursor.getCount() == 0) {
            insert(values);
        } else {
            update(values);
        }
        cursor.close();
        db.close();
        Log.d("Article","end "+values.getAsString("id"));
    }

    public Cursor getListByCategory(int category) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                "id",
                "category",
                "title",
                "slug",
                "abstract",
                "user",
                "created",
                "modify",
                "cover",
                "views",
                "status"
        };

        Cursor cursor = null;
        if (category == 0) {
            cursor = db.query(TBL_NAME, columns, null, null, null, null, null);
        } else {
            String select = "category = ?";
            String[] selectWhere = {
                    String.valueOf(category)
            };
            cursor = db.query(TBL_NAME, columns, select, selectWhere, null, null, null);
        }

        return cursor;
    }

    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
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