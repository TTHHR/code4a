package cn.dxkite.baidusign;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.util.logging.Handler;

import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;

/**
 * 自定义WebView
 * Created by DXkite on 2017/11/3 0003.
 */

public class SignWebView extends WebView {
    static String TAG="SignWebView";
//    Handler hander=null;
    /**
     * Constructs a new WebView with a Context object.
     *
     * @param context a Context object used to access application assets
     */
    public SignWebView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs   an AttributeSet passed to our parent
     */
    public SignWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        Log.d(TAG,"init webview");
        this.getSettings().setJavaScriptEnabled(true);
        this.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookie=CookieManager.getInstance().getCookie(url);
                Log.i(TAG,"get cookie:"+cookie);
            }
        });
    }

    public boolean change2SignPage(){
        new Thread(){
            @Override
            public void run() {
                try {
                    String url=(String)new BaiduSignServer().method("getAuthUrl",String.class).call();
                    // 调用webbview打开这个URL
                    Log.d(TAG,"getAuthUrl:"+url);
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (PermissionException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

}
