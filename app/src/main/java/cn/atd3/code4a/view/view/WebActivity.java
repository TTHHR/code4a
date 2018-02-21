package cn.atd3.code4a.view.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.atd3.code4a.R;
import cn.atd3.code4a.presenter.WebPresenter;
import cn.atd3.code4a.view.inter.WebViewInterface;

import static android.view.KeyEvent.KEYCODE_BACK;


public class WebActivity extends AppCompatActivity implements WebViewInterface {
    private WebView wv;
    private ProgressBar pb;
    private WebPresenter wp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        wv = findViewById(R.id.webview);

        pb = findViewById(R.id.progressBar);

        wp = new WebPresenter(this);

        wp.setWebClient();
        wv.setDownloadListener(new MyWebViewDownLoadListener());

        Intent i = getIntent();

        if (i.getStringExtra("url") != null) {
            wp.loadPage(i.getStringExtra("url"));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK)) {
            wp.goBack(wv);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageLoading(final int pro) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress(pro);
                    }
                }
        );
    }

    @Override
    public void loadPage(final String url) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        wv.loadUrl(url);
                    }
                }
        );

    }

    @Override
    public void goBack() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        wv.goBack();
                    }
                }
        );
    }

    @Override
    public void setWebClient(final WebViewClient wvc, final WebChromeClient wcc) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        wv.setWebViewClient(wvc);
                        wv.setWebChromeClient(wcc);
                    }
                }
        );
    }

    @Override
    public void exit() {
        this.finish();
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (pb.getVisibility() == View.INVISIBLE)
                            pb.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    @Override
    public void dismissProgressBar() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (pb.getVisibility() == View.VISIBLE)
                            pb.setVisibility(View.INVISIBLE);
                    }
                }
        );
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.e("downurl", url);
            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            startActivity(intent);

        }

    }
}
