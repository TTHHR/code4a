package cn.qingyuyu.code4droid.model;

import java.util.ArrayList;

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
}
