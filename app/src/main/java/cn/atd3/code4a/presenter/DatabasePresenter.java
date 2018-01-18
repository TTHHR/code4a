package cn.atd3.code4a.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.util.DbHelper;

/**
 * Created by harry on 2018/1/18.
 */

public class DatabasePresenter {

    public boolean saveArticles(Context con, ArrayList<ArticleModel> listData )
    {
        DbHelper database = new DbHelper(con);
        try {
            for(ArticleModel article : listData)
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
                cv.put("mAbstract", article.getAbstract()); //添加数据
                database.insert(cv);//执行插入操作
            }
        }
        catch (Exception e)
        {
            Log.e("sql",""+e);
            return  false;
        }
        finally {
            database.close();
        }
        return  true;
    }
    public boolean clearArticles( Context c)
    {
        DbHelper database = new DbHelper(c);
        try {
            database.clear();
            return true;
        }
        catch (Exception e)
        {
            Log.e("sql",e.toString());
        }
        finally {
            database.close();
        }
        return  true;
    }



    public ArrayList<ArticleModel> getArticles(Context context,int category){
        ArrayList<ArticleModel>al=new ArrayList<>();
        DbHelper database = new DbHelper(context);
        try {
            Cursor c = database.query();
            while (c.moveToNext()) {
                ArticleModel a = new ArticleModel();
                a.setCategory(c.getInt(c.getColumnIndex("category")));//读取分类

                if(a.getCategory()!=category)//不是所需分类
                    continue;//跳过

                a.setId(c.getInt(c.getColumnIndex("id")));
                a.setTitle(c.getString(c.getColumnIndex("title")));
                a.setSlug(c.getString(c.getColumnIndex("slug")));
                a.setAbstract(c.getString(c.getColumnIndex("mAbstract")));
                a.setUser(c.getInt(c.getColumnIndex("user")));
                a.setCreate(c.getInt(c.getColumnIndex("created")));
                a.setModify(c.getInt(c.getColumnIndex("modify")));
                a.setCover(c.getInt(c.getColumnIndex("cover")));
                a.setViews(c.getInt(c.getColumnIndex("views")));
                a.setStatus(c.getInt(c.getColumnIndex("status")));

                al.add(a);
            }
        }
        catch (Exception e)
        {
            Log.e("sql",e.toString());
        }
        finally {
            database.close();
        }
        return al;
    }








}
