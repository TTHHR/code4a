package cn.atd3.code4a.presenter;

import android.content.Intent;

import java.util.ArrayList;

import cn.atd3.code4a.model.model.ArticleModel;
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

    public void setIntentData(Intent i)
    {

    }
    public void setAdapterData( int kind)
    {
        if(al.size()==0)
        {
            //初始数据
            ArticleModel refresh0 = new ArticleModel();
            refresh0.setTitle("下拉刷新~(●'◡'●)");
            refresh0.setAbstract("按住我下拉刷新"+kind);
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

              try {
                   Thread.sleep(2000);//模拟延时
              } catch (InterruptedException e) {
                            e.printStackTrace();
                }
             al.clear();//清空之前数据
            //模拟数据
            ArticleModel refresh1 = new ArticleModel();
        refresh1.setTitle("下拉刷新~(●'◡'●)");
              refresh1.setAbstract("按住我下拉刷新1"+kind);
              refresh1.setUser(123);
                refresh1.setModify(456);
          refresh1.setCategory(0);
        al.add(refresh1);
               afi.upDate(al);//通知UI刷新

    }

}
