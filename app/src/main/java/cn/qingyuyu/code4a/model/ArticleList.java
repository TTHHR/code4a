package cn.qingyuyu.code4a.model;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.qingyuyu.code4a.control.DataBaseController;
import cn.qingyuyu.code4a.remote.bean.Article;

/**
 * Created by harrytit on 2017/10/20.
 */

public class ArticleList {
    private static ArticleList articleList=new ArticleList();
    private static ArrayList<Article> c4droidList = new ArrayList<>();
    private static ArrayList<Article> aideList = new ArrayList<>();
    private static ArrayList<Article> androidList = new ArrayList<>();
    private ArticleList(){}
    public static ArticleList getArticleList(Context context) {
        if(!c4droidList.isEmpty())
        {
            return articleList;
        }
        ArrayList<ArrayList> al= new DataBaseController().getTempArticles(context);
        if(al.get(0).isEmpty()) {
            Article refresh0 = new Article();
            refresh0.setTitle("下拉刷新~(●'◡'●)");
            refresh0.setAbstract("按住我下拉刷新0");
            refresh0.setUser(123);
            refresh0.setModify(456);
            refresh0.setCategory(0);
            c4droidList.add(refresh0);
        }
        else
            c4droidList=al.get(0);
        if(al.get(1).isEmpty()) {
            Article refresh1 = new Article();
            refresh1.setTitle("下拉刷新~(●'◡'●)");
            refresh1.setAbstract("按住我下拉刷新1");
            refresh1.setUser(456);
            refresh1.setModify(789);
            refresh1.setCategory(1);
            aideList.add(refresh1);
        }
        else
            aideList=al.get(1);
        if(al.get(2).isEmpty()) {
            Article refresh2 = new Article();
            refresh2.setTitle("下拉刷新~(●'◡'●)");
            refresh2.setAbstract("按住我下拉刷新2");
            refresh2.setUser(789);
            refresh2.setModify(123);
            refresh2.setCategory(1);
            androidList.add(refresh2);
        }
        else
            androidList=al.get(2);
        return articleList;
    }
    public ArrayList<Article> getC4droidList() {
        return c4droidList;
    }
    public ArrayList<Article>getAideList(){
        return aideList;
    }
    public ArrayList<Article>getAndroidList(){
        return androidList;
    }

    public void setArticles(@NotNull ArrayList<Article> articleList,int kind) {
        switch (kind)
        {
            case 0:c4droidList.clear();
                for (Article article : articleList) {
                    c4droidList.add(article);
                }
            case 1:aideList.clear();
                for (Article article : articleList) {
                    aideList.add(article);
                }
            case 2:androidList.clear();
                for (Article article : articleList) {
                    androidList.add(article);
                }
        }

    }
}
