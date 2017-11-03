package cn.dxkite.baidusign;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by DXkite on 2017/11/3 0003.
 */

public class SignWebView extends WebView {
    static String TAG="SignWebView";
    /**
     * Constructs a new WebView with a Context object.
     *
     * @param context a Context object used to access application assets
     */
    public SignWebView(Context context) {
        super(context);
        this.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookie=CookieManager.getInstance().getCookie(url);
                Log.i(TAG,"get cookie:"+cookie);
            }
        });
    }
}
