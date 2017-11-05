package cn.qingyuyu.code4a.model;

import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import cn.qingyuyu.code4a.remote.Remote;
import cn.qingyuyu.commom.SomeValue;
import cn.qingyuyu.commom.service.FileDealService;

/**
 * Created by harrytit on 2017/11/4.
 */

public class User {
    private static   String iconName = null;
    private static String userName = null;

    private static User u=new User();


    private User()
    {

    }


    public static User getInstance()
    {
      if (iconName == null && userName == null) {
          try{
                String data=new FileDealService().readFile(SomeValue.dirPath+SomeValue.userData);
              JSONObject user = JSONObject.parseObject(data);
                  iconName = user.getString("portrait");
                   userName = user.getString("uname");
               } catch ( Exception e) {
                   Log.e("User", "not login");
               }

          }
        return u;
    }


    public boolean isLogind(){

        return userName != null;


    }
    public boolean logout(){
        try {
            userName=null;

            new FileDealService().delFile(SomeValue.dirPath+SomeValue.userData);//删除本地信息
            Remote.user.method("signout").call();//服务器退出登陆
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public String getUserName(){
        return userName;
    }
    public Uri getimgUri() {
        File file = new File(SomeValue.dirPath + iconName);
        return Uri.fromFile(file);
    }


}
