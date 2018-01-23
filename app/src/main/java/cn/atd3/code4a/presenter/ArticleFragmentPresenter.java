package cn.atd3.code4a.presenter;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import cn.atd3.code4a.database.ArticleDatabase;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.ArticleFragmentInterface;

import static cn.atd3.code4a.Constant.ERROR;

/**
 * 文章列表处理
 * Created by harry on 2018/1/14.
 */

public class ArticleFragmentPresenter {
    private ArticleFragmentInterface afi;
    private static ArticleDatabase databasePresenter;
    private ArrayList<ArticleModel> al = null;

    private int page = 1;

    public ArticleFragmentPresenter(ArticleFragmentInterface afi, ArticleDatabase databasePresenter) {
        this.afi = afi;
        al = new ArrayList<>();
        ArticleFragmentPresenter.databasePresenter = databasePresenter;
    }

    public void setIntentData(Intent i, int p) {
        i.putExtra("articleid", al.get(p).getId());
        i.putExtra("userid", al.get(p).getUser());
        i.putExtra("article", al.get(p));
        i.putExtra("title", al.get(p).getTitle());
    }

    public void setAdapterData(int category) {
        Log.e("al", "" + al.hashCode());
        al = databasePresenter.getArticles(category);
        Log.d("Article", "category " + category + " list size = " + al.size());
        if (al.size() == 0) {
            afi.showTouch();
        } else {
            afi.showList();
        }
        afi.setAdapter(al);
    }

    public void loadMoreData(final int kind) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        requestData(kind);
                        afi.upDate();//通知UI刷新
                        afi.onfinishLoadmore();

                    }
                }
        ).start();

    }

    public void refreshData(final int kind) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        requestData(kind);
                        afi.upDate();//通知UI刷新
                        afi.onfinishRefresh();

                    }
                }
        ).start();
    }

    private boolean requestData(final int kind) {
        try {
            Object articleList = null;
            if (kind == 0) {
                articleList = Remote.article.method("getList", ArticleModel.class).call(1, 10);
            } else {
                articleList = Remote.category.method("getArticleById", ArticleModel.class).call(kind, page, 10);
            }
            if (articleList.getClass().equals(ArrayList.class)) {
                for (ArticleModel am : (ArrayList<ArticleModel>) articleList) {
                    Log.e("recdata", am.toString());
                    databasePresenter.saveArticle(am);
                }
                al.clear();
                al.addAll(databasePresenter.getArticles(kind));
            }
        } catch (Exception e) {
            Log.e("Article", "refersh list", e);
            afi.showToast(ERROR, e.toString());
            return false;
        }
        return true;

    }
}
