package cn.qingyuyu.code4a;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;
import cn.qingyuyu.code4a.remote.Remote;
import cn.qingyuyu.code4a.remote.bean.Article;
import cn.qingyuyu.code4a.remote.bean.UserInfo;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/10   1:35
 **/
public class TryLoginActivity extends AppCompatActivity {
    private static final String TAG = "TryLoginActivity";
    private TextView message;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_login);

        message=(TextView)findViewById(R.id.message);
        trySignup();
    }

    private void showMessage(final String string){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                message.append(string);
            }
        });
    }

    private void trySignup(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String s;
                    s=Remote.user.method("signup").call("ygl2018","ygl2018@qq.com","aaa12345").toString();
                    //s=Remote.category.method("getArticleById").call(1,1, 10).toString();
                    showMessage(s);
                    Log.i(TAG,"trySignup() s:"+s);
                }catch (ServerException e){
                    e.printStackTrace();
                }catch (PermissionException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
