package cn.qingyuyu.code4droid.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.qingyuyu.code4droid.remote.bean.Article;

/**
 * Created by harrytit on 2017/10/20.
 */

public class ListData {
    private ArrayList<String> listData=new ArrayList<>();
   public  ListData(){
        listData.add("no Data");
       listData.add("no Data");
    }

   public ArrayList<String> getListData()
   {
       return listData;
   }
   public void upDate()
   {
       listData.clear();
       listData.add("测试数据");
       listData.add("测试数据");
       listData.add("测试数据");
       listData.add("测试数据");
   }

    public void setArticles(@NotNull ArrayList<Article> articleList) {
        listData.clear();
        listData.add("<--- 测试数据 --->");
        for (Article article: articleList){
            listData.add(article.getTitle());
        }
        listData.add("<--- 测试数据 --->");
    }
}
