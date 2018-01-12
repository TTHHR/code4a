package cn.atd3.code4a.view.inter;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

/**
 * Created by harry on 2018/1/12.
 */

public interface WebViewInterface {

    void onPageLoading(int pro);

    void loadPage(String url);

    void goBack();

    void setWebClient(WebViewClient wvc, WebChromeClient wcc);

    void exit();

    void showProgressBar();

    void dismissProgressBar();

}
