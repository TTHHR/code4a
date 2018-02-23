package cn.atd3.code4a.view.view

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.DownloadListener
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.qmuiteam.qmui.widget.QMUITopBarLayout

import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.WebPresenter
import cn.atd3.code4a.view.inter.WebViewInterface

import android.view.KeyEvent.KEYCODE_BACK
import android.widget.Toast
import cn.atd3.code4a.Constant


class WebActivity : AppCompatActivity(), WebViewInterface {
    private var wv: WebView? = null
    private var pb: ProgressBar? = null
    private var wp: WebPresenter? = null
    private var topBar: QMUITopBarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        topBar = findViewById(R.id.topbar)
        topBar!!.addLeftBackImageButton().setOnClickListener { finish() }
        topBar!!.setTitle(getString(R.string.app_name))

        try {
            topBar!!.setBackgroundColor(Color.parseColor(Constant.themeColor))
        }
        catch (e:Exception)
        {
            Toast.makeText(this,getString(R.string.waring_error_color),Toast.LENGTH_SHORT).show()
            topBar!!.setBackgroundColor(Color.parseColor(Constant.defaultThemeColor))
        }
        wv = findViewById(R.id.webview)

        pb = findViewById(R.id.progressBar)

        wp = WebPresenter(this)

        wp!!.setWebClient()
        wv!!.setDownloadListener(MyWebViewDownLoadListener())

        val i = intent

        if (i.getStringExtra("url") != null) {
            wp!!.loadPage(i.getStringExtra("url"))
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KEYCODE_BACK) {
            wp!!.goBack(wv)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPageLoading(pro: Int) {
        runOnUiThread { pb!!.progress = pro }
    }

    override fun loadPage(url: String) {
        runOnUiThread { wv!!.loadUrl(url) }

    }

    override fun goBack() {
        runOnUiThread { wv!!.goBack() }
    }

    override fun setWebClient(wvc: WebViewClient, wcc: WebChromeClient) {
        runOnUiThread {
            wv!!.webViewClient = wvc
            wv!!.webChromeClient = wcc
        }
    }

    override fun exit() {
        this.finish()
    }

    override fun showProgressBar() {
        runOnUiThread {
            if (pb!!.visibility == View.INVISIBLE)
                pb!!.visibility = View.VISIBLE
        }
    }

    override fun dismissProgressBar() {
        runOnUiThread {
            if (pb!!.visibility == View.VISIBLE)
                pb!!.visibility = View.INVISIBLE
        }
    }

    private inner class MyWebViewDownLoadListener : DownloadListener {

        override fun onDownloadStart(url: String, userAgent: String, contentDisposition: String, mimetype: String, contentLength: Long) {
            Log.e("downurl", url)
            val uri = Uri.parse(url)

            val intent = Intent(Intent.ACTION_VIEW, uri)

            startActivity(intent)

        }

    }
}
