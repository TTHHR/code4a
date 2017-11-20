package cn.qingyuyu.code4a.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import cn.qingyuyu.code4a.remote.bean.Article;
import cn.qingyuyu.code4a.util.DbHelper;

/**
 * Created by harrytit on 2017/11/9.
 */

public class DataBaseController {
    public boolean saveArticles(Context con, ArrayList<Article> listData )
    {
        DbHelper database = new DbHelper(con);
        try {
            for(Article article : listData)
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
    public ArrayList<ArrayList> getTempArticles(Context context){
        ArrayList<ArrayList>al=new ArrayList<>();
        ArrayList<Article> a1=new ArrayList<>();
        ArrayList<Article> a2=new ArrayList<>();
        ArrayList<Article> a3=new ArrayList<>();
        DbHelper database = new DbHelper(context);
        try {
            Cursor c = database.query();
            while (c.moveToNext()) {
                Article a = new Article();
                a.setCategory(c.getInt(c.getColumnIndex("category")));//读取分类
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
               switch (a.getCategory())
               {
                   case 1:a1.add(a);break;
                   case 2:a2.add(a);break;
                   case 3:a3.add(a);break;
                       default:
               }

            }
        }
        catch (Exception e)
        {
            Log.e("sql",e.toString());
        }
        finally {
            database.close();
        }
           al.add(a1);
        al.add(a2);
        al.add(a3);
        return al;
    }
}
