package cn.atd3.code4a.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.util.DbHelper;

/**
 * Created by harry on 2018/1/18.
 */

public class ArticleDatabasePresenter {
    DbHelper database =null;
    public static String TBL_NAME = "article_info";
    private String CLEAR = "drop table if exists " + TBL_NAME;
    private String QUERY = "select * from " + TBL_NAME;

    public ArticleDatabasePresenter(Context context) {
        database = new DbHelper(context);
    }

    public boolean saveArticles(ArrayList<ArticleModel> listData) {

        Log.d("Article", "save list " + listData);
        try {
            for (ArticleModel article : listData) {
                ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据
                cv.put("id", article.getId()); //添加数据
                cv.put("title", article.getTitle()); //添加数据
                cv.put("slug", article.getSlug()); //添加数据
                cv.put("user", article.getUser());//添加数据
                cv.put("created", article.getCreate()); //添加数据
                cv.put("modify", article.getModify()); //添加数据
                cv.put("category", article.getCategory()); //添加数据
                cv.put("cover", article.getCover()); //添加数据
                cv.put("views", article.getViews()); //添加数据
                cv.put("status", article.getStatus()); //添加数据
                cv.put("abstract", article.getAbstract()); //添加数据
                Log.d("Article", "insert " + cv);
                replace(cv);//执行插入操作
            }
        } catch (Exception e) {
            Log.e("Article", "" + e);
            e.printStackTrace();
            return false;
        } finally {
            database.close();
        }
        return true;
    }

    public ArrayList<ArticleModel> getArticles(int category) {
        ArrayList<ArticleModel> articleModels = new ArrayList<>();

        try {
            Cursor cursor = getListByCategory(category);
            while (cursor.moveToNext()) {
                ArticleModel a = new ArticleModel();
                a.setCategory(cursor.getInt(cursor.getColumnIndex("category")));//读取分类
                a.setId(cursor.getInt(cursor.getColumnIndex("id")));
                a.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                a.setSlug(cursor.getString(cursor.getColumnIndex("slug")));
                a.setAbstract(cursor.getString(cursor.getColumnIndex("abstract")));
                a.setUser(cursor.getInt(cursor.getColumnIndex("user")));
                a.setCreate(cursor.getInt(cursor.getColumnIndex("created")));
                a.setModify(cursor.getInt(cursor.getColumnIndex("modify")));
                a.setCover(cursor.getInt(cursor.getColumnIndex("cover")));
                a.setViews(cursor.getInt(cursor.getColumnIndex("views")));
                a.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                articleModels.add(a);
            }

        } catch (Exception e) {
            Log.e("Article", e.toString());
            e.printStackTrace();
        } finally {
            database.close();
        }
        Log.d("Aritcle", "query from db " + articleModels);
        return articleModels;
    }


    public void insert(ContentValues values) {
        SQLiteDatabase db = database.getWritableDatabase();
        Log.d("Article", "insert into database " + values);
        db.insert(TBL_NAME, null, values);
        db.close();
    }

    public void update(ContentValues values) {
        String whereClause = "id =?";
        Log.d("Article", "id " + values.getAsString("id") + " update  " + values);
        String[] whereArgs = {values.getAsString("id")};
        SQLiteDatabase db = database.getWritableDatabase();
        db.update(TBL_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void replace(ContentValues values) {
        SQLiteDatabase db = database.getReadableDatabase();
        String[] whereArgs = {values.getAsString("id")};
        String[] columns = {"id"};
        Log.d("Article", "insert " + values.getAsString("id"));
        Cursor cursor = db.query(TBL_NAME, columns, "id=?", whereArgs, null, null, null);
        if (cursor.getCount() == 0) {
            insert(values);
        } else {
            update(values);
        }
        cursor.close();
        db.close();
        Log.d("Article", "end " + values.getAsString("id"));
    }

    public Cursor getListByCategory(int category) {
        SQLiteDatabase db = database.getReadableDatabase();
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
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY, null);
        return c;
    }


}
