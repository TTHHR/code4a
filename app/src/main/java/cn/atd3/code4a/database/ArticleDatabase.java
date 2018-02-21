package cn.atd3.code4a.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.util.DbHelper;
import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;

/**
 * Created by harry on 2018/1/18.
 */

public class ArticleDatabase {
    DbHelper database =null;
    public static String TBL_NAME = "article_info";
    private String QUERY = "select * from " + TBL_NAME;

    public ArticleDatabase(Context context) {
        database = new DbHelper(context);
    }

    public void saveArticle(ArticleModel article)
    {
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
        if(article.getContent()!=null)
            cv.put("content", article.getContent()); //添加数据
        replace(cv);//执行插入操作
    }

    public ArrayList<ArticleModel> getArticles(int category) {
        ArrayList<ArticleModel> articleModels = new ArrayList<>();

        try {
            Cursor cursor = getListByCategory(category);
            while (cursor.moveToNext()) {
                ArticleModel a = new ArticleModel();
                a.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));//读取分类
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
            e.printStackTrace();
        } finally {
            database.close();
        }
        return articleModels;
    }
    public ArticleModel getArticle(int id) {
        ArticleModel a = null;

        try {
            SQLiteDatabase db = database.getReadableDatabase();
            String select = "id = ?";
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
                    "status",
                    "content"
            };
            String[] selectWhere = {
                    String.valueOf(id)
            };
            Cursor cursor = db.query(TBL_NAME, columns, select, selectWhere, null, null, null);
            while (cursor.moveToNext()) {
                a=new ArticleModel();
                a.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));//读取分类
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
                a.setContent(cursor.getString(cursor.getColumnIndex("content")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
        return a;
    }

    public void insert(ContentValues values) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.insert(TBL_NAME, null, values);
        db.close();
    }

    public void update(ContentValues values) {
        String whereClause = "id =?";
        String[] whereArgs = {values.getAsString("id")};
        SQLiteDatabase db = database.getWritableDatabase();
        db.update(TBL_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void replace(ContentValues values) {
        SQLiteDatabase db = database.getReadableDatabase();
        String[] whereArgs = {values.getAsString("id")};
        String[] columns = {"id"};
        Cursor cursor = db.query(TBL_NAME, columns, "id=?", whereArgs, null, null, null);
        if (cursor.getCount() == 0) {
            insert(values);
        } else {
            update(values);
        }
        cursor.close();
        db.close();
    }

    public boolean deleteArticle(int id)
    {
        SQLiteDatabase db = database.getReadableDatabase();
        String[] whereArgs = {""+id};
        int amount=db.delete(TBL_NAME,"id=?",whereArgs);
        db.close();
        return amount != 0;
    }

    public Cursor getListByCategory(int categoryId) {
        SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {
                "id",
                "category",
                "categoryId",
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
        if (categoryId == 0) {
            cursor = db.query(TBL_NAME, columns, null, null, null, null, "modify desc");
        } else {
            String select = "categoryId = ?";
            String[] selectWhere = {
                    String.valueOf(categoryId)
            };
            cursor = db.query(TBL_NAME, columns, select, selectWhere, null, null, "modify desc");
        }

        return cursor;
    }

    public Cursor query() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY, null);
        return c;
    }

    /**
     * 预加载
     */
    public void fetchFirst() {
        try {
            Object articleList = Remote.article.method("getList", ArticleModel.class).call(1, 10);
            if (articleList instanceof ArrayList){
                for (ArticleModel am : (ArrayList<ArticleModel>) articleList) {
                    this.saveArticle(am);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {"id"};
        Cursor cursor = db.query(TBL_NAME, columns, null, null, null, null, null);
        return cursor.getCount() == 0 ;
    }
}
