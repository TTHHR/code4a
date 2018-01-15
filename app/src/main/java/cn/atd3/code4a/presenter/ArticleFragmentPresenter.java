package cn.atd3.code4a.presenter;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.ArticleFragmentInterface;

/**
 * Created by harry on 2018/1/14.
 */

public class ArticleFragmentPresenter {
    private ArticleFragmentInterface afi;

    private ArrayList<ArticleModel> al= new ArrayList<>();

    public ArticleFragmentPresenter(ArticleFragmentInterface afi)
    {
        this.afi=afi;

    }

    public void setIntentData(Intent i,int p)
    {
        i.putExtra("articleid",al.get(p).getId());
        i.putExtra("userid",al.get(p).getUser());
        i.putExtra("title",al.get(p).getTitle());
    }
    public void setAdapterData()
    {
        if(al.size()==0)
        {
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
    public void update()
    {
        if(al!=null)
        afi.upDate(al);
    }
    public void requestData(final int kind)//Refresh 库自带异步
    {
        al.clear();//清空之前数据
              try {
                  Object articleList = Remote.category.method("getArticleById", ArticleModel.class).call(kind + 1, 1, 10);
                  if (articleList.getClass().equals(ArrayList.class)) {
                            for(ArticleModel am:(ArrayList<ArticleModel>)articleList)
                            {
                                Log.e("recdata",am.toString());
                                al.add(am);
                            }
                  }
              } catch (Exception e) {
                            Log.e("requestdata",""+e);
                }
        afi.upDate(al);//通知UI刷新

    }

}
