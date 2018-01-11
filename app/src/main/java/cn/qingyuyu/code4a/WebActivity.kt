package cn.qingyuyu.code4a

import android.graphics.PixelFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ProgressBar
import com.tencent.smtt.sdk.WebChromeClient

import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class WebActivity : AppCompatActivity() {
    private lateinit var mPageLoadingProgressBar: ProgressBar
    private var url = "http://github.com/tthhr"
    private lateinit var tbsContent:X5WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFormat(PixelFormat.TRANSLUCENT)

        val intent = intent
        if (intent != null) {
            url=intent.getStringExtra("url")
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                window
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
            }
        } catch (e: Exception) {
        }


        /*
		 * getWindow().addFlags(
		 * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
        setContentView(R.layout.activity_web)



        initView()
    }




    private fun initView() {

         mPageLoadingProgressBar = findViewById(R.id.progressBar)
        mPageLoadingProgressBar.max = 100
        mPageLoadingProgressBar.progressDrawable = this.resources
                .getDrawable(R.drawable.color_progressbar)





        tbsContent = findViewById(R.id.tbsContent)

        tbsContent.webChromeClient = object : WebChromeClient() {
            override   fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    mPageLoadingProgressBar.visibility = View.INVISIBLE
                } else {
                    if (View.INVISIBLE === mPageLoadingProgressBar.visibility) {
                        mPageLoadingProgressBar.visibility = View.VISIBLE
                    }
                    mPageLoadingProgressBar.progress = newProgress
                }
                super.onProgressChanged(view, newProgress)
            }

        }
        val webSetting = tbsContent.settings
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(false)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.domStorageEnabled = true
        webSetting.javaScriptEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        webSetting.setAppCachePath(this.getDir("appcache", 0).path)
        webSetting.databasePath = this.getDir("databases", 0).path
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .path)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND

        tbsContent.loadUrl(url)


    }

    override
    fun  onKeyDown( keyCode :Int,  event:KeyEvent):Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && tbsContent.canGoBack()) {
            tbsContent.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}

