package cn.qingyuyu.code4a.model;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.qingyuyu.code4a.control.DataBaseController;
import cn.qingyuyu.code4a.remote.bean.Article;

/**
 * Created by harrytit on 2017/10/20.
 */

public class ArticleList {

    private ArrayList<Article> c4droidList = new ArrayList<>();
    private ArrayList<Article> aideList = new ArrayList<>();
    private ArrayList<Article> androidList = new ArrayList<>();
    public ArticleList(Context context) {
        ArrayList<ArrayList> al= new DataBaseController().getTempArticles(context);
        if(al.get(0).isEmpty()) {
            Article refresh = new Article();
            refresh.setTitle("下拉刷新~(●'◡'●)");
            refresh.setAbstract("按住我下拉刷新0");
            refresh.setUser(123);
            refresh.setModify(456);
            refresh.setCategory(0);
            c4droidList.add(refresh);
        }
        else
            c4droidList=al.get(0);
        if(al.get(1).isEmpty()) {
            Article refresh = new Article();
            refresh.setTitle("下拉刷新~(●'◡'●)");
            refresh.setAbstract("按住我下拉刷新1");
            refresh.setUser(123);
            refresh.setModify(456);
            refresh.setCategory(1);
            aideList.add(refresh);
        }
        else
            aideList=al.get(1);
        if(al.get(2).isEmpty()) {
            Article refresh = new Article();
            refresh.setTitle("下拉刷新~(●'◡'●)");
            refresh.setAbstract("按住我下拉刷新2");
            refresh.setUser(123);
            refresh.setModify(456);
            refresh.setCategory(1);
            androidList.add(refresh);
        }
        else
            androidList=al.get(2);
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
