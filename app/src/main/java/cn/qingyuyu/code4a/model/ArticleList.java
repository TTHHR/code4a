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
    private ArrayList<Article> listData = new ArrayList<>();

    public ArticleList(Context context) {
        ArrayList<Article> al= new DataBaseController().getTempArticles(context);
        if(al.isEmpty()) {
            Article refresh = new Article();
            refresh.setTitle("下拉刷新~(●'◡'●)");
            refresh.setAbstract("按住我下拉刷新");
            refresh.setUser(123);
            refresh.setModify(456);
            refresh.setCategory(0);
            listData.add(refresh);
        }
        else
            listData=al;
    }

    public ArrayList<Article> getListData() {
        return listData;
    }


    public void setArticles(@NotNull ArrayList<Article> articleList) {
        listData.clear();
        for (Article article : articleList) {
            listData.add(article);
        }
    }
}
