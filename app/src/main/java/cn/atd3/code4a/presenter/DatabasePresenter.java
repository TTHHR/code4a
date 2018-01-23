package cn.atd3.code4a.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.util.ArticleDbHelper;

/**
 * Created by harry on 2018/1/18.
 */

public class DatabasePresenter {

    /**
     * 新增或者更新
     * @param con
     * @param listData
     * @return
     */
    public boolean saveArticles(Context con, ArrayList<ArticleModel> listData) {
        ArticleDbHelper database = new ArticleDbHelper(con);
        Log.d("Article","save list "+listData);
        try {
            for (ArticleModel article :listData) {
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
                Log.d("Article","insert " +cv);
                database.replace(cv);//执行插入操作
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

    public boolean clearArticles(Context c) {
        ArticleDbHelper database = new ArticleDbHelper(c);
        Log.d("Article","clear all items");
        try {
            database.clear();
            return true;
        } catch (Exception e) {
            Log.e("sql", e.toString());
        } finally {
            database.close();
        }
        return true;
    }


    public ArrayList<ArticleModel> getArticles(Context context, int category) {
        ArrayList<ArticleModel> articleModels = new ArrayList<>();
        ArticleDbHelper database = new ArticleDbHelper(context);
        try {
            Cursor cursor = database.getListByCategory(category);
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
        Log.d("Aritcle","query from db "+articleModels);
        return articleModels;
    }
}
