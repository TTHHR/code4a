package cn.atd3.code4a;

import android.content.Context;
import android.content.SharedPreferences;

import cn.atd3.code4a.model.model.User;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/07   20:16
 **/
public class SigninUserManager {
    private static User user;

    public static User getUser(Context context) {
        if(user==null){
            user=new User();
            SharedPreferences sharedPreferences=context.getSharedPreferences(SigninUserManager.class.toString(),Context.MODE_PRIVATE);
            user.setAccount(sharedPreferences.getString("account",null));
            user.setSignin(sharedPreferences.getBoolean("signin",false));
            return user;
        }else {
            return user;
        }
    }

    public static void setUser(Context context,User user) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(SigninUserManager.class.toString(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("account",user.getAccount());
        editor.putBoolean("signin",user.getSignin());
        editor.commit();
        SigninUserManager.user = user;
    }
}
