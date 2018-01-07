package cn.qingyuyu.code4a.model;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.alibaba.fastjson.JSONObject;

import java.io.File;

import cn.qingyuyu.code4a.remote.Remote;
import cn.qingyuyu.commom.SomeValue;
import cn.qingyuyu.commom.service.FileDealService;

/**
 * Created by harrytit on 2017/11/4.
 */

public class User {
    private static String iconName = null;
    private static String userName = null;

    private static User u = new User();
    private static long lastModify=0;

    private User() {

    }


    public static User getInstance() {
        String userData=SomeValue.dirPath + SomeValue.userData;
        boolean firstRun=iconName == null && userName == null;
        boolean modified=false;
        File userFile=new File(userData);
        if (userFile.exists()){
            modified=userFile.lastModified() > lastModify;
            lastModify=new File(userData).lastModified();
        }
        if (firstRun || modified) {
            try {
                String data =  FileDealService.getInstance().readFile(userData);
                JSONObject user = JSONObject.parseObject(data);
                iconName = user.getString("portrait");
                userName = user.getString("uname");
            } catch (Exception e) {
                Log.e("User", "not login");
            }
        }
        return u;
    }


    public boolean isLogind() {

        return userName != null;


    }

    public boolean logout(Activity con) {
        try {
            userName = null;
             FileDealService.getInstance().delFile(SomeValue.dirPath + SomeValue.userData);//删除本地信息
            Remote.user.method("signout").call();//服务器退出登陆
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserName() {
        return userName;
    }

    public Uri getimgUri() {
        File file = new File(SomeValue.dirPath + iconName);
        return Uri.fromFile(file);
    }


}
