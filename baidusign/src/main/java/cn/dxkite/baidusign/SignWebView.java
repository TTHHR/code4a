package cn.dxkite.baidusign;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.atd3.proxy.ProxyConfig;

/**
 * 自定义WebView
 * Created by DXkite on 2017/11/3 0003.
 */

public class SignWebView extends WebView {

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

    public void init() {
        this.getSettings().setJavaScriptEnabled(true);
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookie = CookieManager.getInstance().getCookie(url);
                String[] cookies =cookie.split(";");
                for(String cookieStr:cookies){
                    Log.d("cookie-save",cookieStr);
                    ProxyConfig.getController().saveCookie(cookieStr);
                }
                Log.d("cookie-show", cookie);
            }
        });
    }
}
