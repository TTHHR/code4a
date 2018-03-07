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

    public static Boolean isSignin(Context context){
        User u=getUser(context);
        if(u.getId()==null||u.getName()==null){
            return false;
        }else {
            return true;
        }
    }

    public static User getUser(Context context) {
        if(user==null){
            user=new User();
            SharedPreferences sharedPreferences=context.getSharedPreferences(SigninUserManager.class.toString(),Context.MODE_PRIVATE);
            user.setEmail(sharedPreferences.getString("email",null));
            user.setId(sharedPreferences.getString("id",null));
            user.setName(sharedPreferences.getString("name",null));
            user.setSignupTime(sharedPreferences.getString("signupTime",null));
            return user;
        }else {
            return user;
        }
    }

    public static void setUser(Context context,User user) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(SigninUserManager.class.toString(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email",user.getEmail());
        editor.putString("id",user.getId());
        editor.putString("name",user.getName());
        editor.putString("signupTime",user.getSignupTime());
        editor.apply();
        SigninUserManager.user = user;
    }

    public static void deleteUser(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SigninUserManager.class.toString(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
