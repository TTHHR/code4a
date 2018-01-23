package cn.atd3.code4a.util;

/**
 * 文章数据库存储器
 * Created by harrytit on 2017/10/10.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cn.atd3.code4a.database.ArticleDatabase;


public class DbHelper extends SQLiteOpenHelper {

    private final static  String DATABASE_NAME="code4a.db";

    //TOOD: 数据库外部存储

    private String CREATE_TBL = "create table " + ArticleDatabase.TBL_NAME
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

    public DbHelper(Context context) {
        super(context,DATABASE_NAME, null, 3);
        Log.d("Article","Context is "+context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}