package cn.qingyuyu.code4droid.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.qingyuyu.code4droid.remote.bean.Article;

/**
 * Created by harrytit on 2017/10/20.
 */

public class ListData {
    private ArrayList<String> listData = new ArrayList<>();

    public ListData() {
        listData.add("下拉刷新~(●'◡'●)");
    }

    public ArrayList<String> getListData() {
        return listData;
    }


    public void setArticles(@NotNull ArrayList<Article> articleList) {
        listData.clear();
        for (Article article : articleList) {
            listData.add(article.getTitle());
        }
    }
}
