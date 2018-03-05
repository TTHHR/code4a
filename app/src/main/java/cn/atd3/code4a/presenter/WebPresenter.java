package cn.atd3.code4a.presenter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import cn.atd3.code4a.view.inter.WebViewInterface;

/**
 * Created by harry on 2018/1/12.
 */

public class WebPresenter {

    private WebViewInterface wvi;

    public WebPresenter(WebViewInterface wvi)
    {
        this.wvi=wvi;
    }

    public void loadPage(String url)
    {
        wvi.loadPage(url);
    }

    public void goBack(WebView v)
    {
        if(v.canGoBack())
            wvi.goBack();
        else
        {
            wvi.exit();
        }
    }

    public void setWebClient() {

        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };

        WebChromeClient wcc = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    Log.e("ppppp",""+newProgress);
                    wvi.showProgressBar();
                        wvi.onPageLoading(newProgress);
                } else {
                    wvi.dismissProgressBar();
                }
            }
        };

        wvi.setWebClient(wvc,wcc);

    }


}
