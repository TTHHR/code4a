package cn.atd3.code4a.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.ArticleFragmentInterface;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.privateString;

/**
 * Created by harry on 2018/1/14.
 */

public class ArticleFragmentPresenter {
    private ArticleFragmentInterface afi;

    private ArrayList<ArticleModel> al = null;

    private int page=1;

    public ArticleFragmentPresenter(ArticleFragmentInterface afi) {
        this.afi = afi;
        al = new ArrayList<>();
    }

    public void setIntentData(Intent i, int p) {
        i.putExtra("articleid", al.get(p).getId());
        i.putExtra("userid", al.get(p).getUser());
        i.putExtra("title", al.get(p).getTitle());
    }

    public void setAdapterData(Context c,int category) {
        Log.e("al", "" + al.hashCode());
            al=new DatabasePresenter().getArticles(c,category);

        if (al.size() == 0) {
            //初始数据
            ArticleModel refresh0 = new ArticleModel();
            refresh0.setTitle("下拉刷新~(●'◡'●)");
            refresh0.setAbstract("按住我下拉刷新");
            refresh0.setUser(123);
            refresh0.setModify(456);
            refresh0.setCategory(0);
            al.add(refresh0);
        }
        afi.setAdapter(al);
    }

    public void saveToDatabase(Context c)
    {
            new DatabasePresenter().saveArticles(c,al);
    }


    public void loadMoreData(final int kind)
    {
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

    public void refreshData(final int kind)
    {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        page=1;
                        requestData(kind);
                        afi.upDate();//通知UI刷新
                        afi.onfinishRefresh();

                    }
                }
        ).start();

    }

    private boolean requestData(final int kind)
    {
        //先清空数据会导致还在请求网络数据时，用户滑动LIST，数组越界错误      al.clear();//清空之前数据
        try {
            Object articleList =null;
            if (kind ==0 ) {
               articleList = Remote.article.method("getList", ArticleModel.class).call( 1, 10);
            }else{
                articleList = Remote.category.method("getArticleById", ArticleModel.class).call(kind , page, 10);
            }
            if (articleList.getClass().equals(ArrayList.class)) {
                if(page==1)//就是刷新
                al.clear();//清空之前数据
                for (ArticleModel am : (ArrayList<ArticleModel>) articleList) {
                    Log.e("recdata", am.toString());
                    al.add(am);
                }
            }
        } catch (Exception e) {
            Log.e("requestdata", "" + e);
            afi.showToast(ERROR,e.toString());
            return false;
        }
        return true;

    }

}
