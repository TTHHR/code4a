package cn.dxkite.baidusign;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import cn.atd3.proxy.ProxyConfig;
import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;

/**
 * 自定义WebView
 * Created by DXkite on 2017/11/3 0003.
 */

public class SignWebView extends WebView {

    private  ProgressBar progressbar;
    private  Context context;
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
                            Looper.prepare();
                            Toast.makeText(context,"你的百度信息为："+baidu,Toast.LENGTH_SHORT).show();
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
}
