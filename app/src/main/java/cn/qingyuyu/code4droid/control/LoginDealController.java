package cn.qingyuyu.code4droid.control;

import android.app.Activity;
import android.util.Log;
/**
 * Created by harrytit on 2017/11/2.
 */

public class LoginDealController {
    public boolean call(String call, Activity con)
    {
        Log.e("call",call);
        if(call.equals("login"))
            return login(con);
        else if(call.equals("bindmail"))
            return bindMail();
        else if(call.equals("checkmail"))
            return checkMail();
        return false;
    }
    private boolean login(Activity con)
    {
        boolean r=false;


        return r;
    }
    private boolean bindMail()
    {
        boolean r=false;
        //绑定邮箱
        return r;
    }
    private boolean checkMail()
    {
        boolean r=false;
        //检查是否已经绑定过邮箱

        return r;
    }

}