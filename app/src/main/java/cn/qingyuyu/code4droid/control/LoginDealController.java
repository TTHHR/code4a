package cn.qingyuyu.code4droid.control;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;

import java.io.IOException;

import cn.qingyuyu.commom.SomeValue;

/**
 * Created by harrytit on 2017/11/2.
 */

public class LoginDealController {
    public boolean call(String call, Activity con)
    {
        if(call.equals("login"))
            return login(con);
        else if(call.equals("bindmail"))
            return bindMail();
        else if(call.equals("checkmail"))
            return checkMail();
        return false;
    }
    public boolean login(Activity con)
    {
        boolean r=false;
        //登陆操作实体
        final Baidu baidu = new Baidu(SomeValue.clientId, con);
        baidu.authorize(con, null, false, true, new BaiduDialog.BaiduDialogListener() {
            @Override
            public void onComplete(Bundle bundle) {
                AsyncBaiduRunner runner = new AsyncBaiduRunner(baidu);
                runner.request(Baidu.LoggedInUser_URL, null, "POST", new DefaultRequstListener());
            }

            @Override
            public void onBaiduException(BaiduException e) {

            }

            @Override
            public void onError(BaiduDialogError baiduDialogError) {

            }

            @Override
            public void onCancel() {

            }
        });

        return r;
    }
    public boolean bindMail()
    {
        boolean r=false;
        //绑定邮箱
        return r;
    }
    public boolean checkMail()
    {
        boolean r=false;
        //检查是否已经绑定过邮箱

        return r;
    }
    //百度登陆Listener
    class DefaultRequstListener implements AsyncBaiduRunner.RequestListener {
        @Override
        public void onComplete(String s) {

        }
        @Override
        public void onIOException(IOException e) {

        }
        @Override
        public void onBaiduException(BaiduException e) {

        }
    }
}