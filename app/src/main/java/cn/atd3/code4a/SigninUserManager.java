package cn.atd3.code4a;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

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
        if(TextUtils.isEmpty(u.getId())||TextUtils.isEmpty(u.getName())||TextUtils.isEmpty(u.getSignupTime())){
            return false;
        }else {
            return true;
        }
    }

    public static User getUser(Context context) {
        if(user==null){
            return getUserFromSP(context);
        }else {
            return user;
        }
    }

    public static void setUser(Context context,User user) {
        SigninUserManager.user = user;
        setUserToSP(context,user);
    }

    public static void deleteUser(Context context){
        SigninUserManager.user = null;
        setUserToSP(context,new User());
    }

    private static User getUserFromSP(Context context){
        User u=new User();
        SharedPreferences sharedPreferences=context.getSharedPreferences(SigninUserManager.class.toString(),Context.MODE_PRIVATE);
        u.setEmail(sharedPreferences.getString("email",""));
        u.setId(sharedPreferences.getString("id",""));
        u.setName(sharedPreferences.getString("name",""));
        u.setSignupTime(sharedPreferences.getString("signupTime",""));
        return u;
    }

    private static void setUserToSP(Context context,User u){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SigninUserManager.class.toString(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email",u.getEmail());
        editor.putString("id",u.getId());
        editor.putString("name",u.getName());
        editor.putString("signupTime",u.getSignupTime());
        editor.apply();
    }
}
