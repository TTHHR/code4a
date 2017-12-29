package cn.dxkite.baidusign;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import cn.atd3.proxy.ProxyConfig;
import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;
import cn.qingyuyu.commom.service.FileDealService;

/**
 * 自定义WebView
 * Created by DXkite on 2017/11/3 0003.
 */

public class SignWebView extends WebView {

    private  ProgressBar progressbar;
    private  Context context;
    private  AppCompatActivity signActivity;

    /**
     * Constructs a new WebView with a Context object.
     *
     * @param context a Context object used to access application assets
     */
    public SignWebView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs   an AttributeSet passed to our parent
     */
    public SignWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        this.context=context;
        //添加进度条UI
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                10, 0, 0));

        Drawable drawable = context.getResources().getDrawable(R.drawable.web_progressbar);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());
        getSettings().setJavaScriptEnabled(true);
    }

    public  class WebViewClient extends  android.webkit.WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String cookie = CookieManager.getInstance().getCookie(url);
            String[] cookies =cookie.split(";");
            for(String cookieStr:cookies){
                ProxyConfig.getController().saveCookie(url,cookieStr);
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        Object baidu=new BaiduSignServer().method("getInfo",BaiduUser.class).call();


                        if (baidu instanceof BaiduUser){
                            Log.e("login baidu",baidu.toString());
                            new FileDealService().userFile(baidu.toString());
                            Looper.prepare();
                            Toast.makeText(context,"登陆成功！Success",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    } catch (ServerException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (PermissionException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }

    //进度条改变
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onPermissionRequest(PermissionRequest request) {

            if (Build.VERSION.SDK_INT >= 21) {
                request.grant(request.getResources());
            }
        }
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setSignActivity(AppCompatActivity signActivit) {
        this.signActivity = signActivit;
    }
}
