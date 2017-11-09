package cn.dxkite.baidusign

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.CookieSyncManager


class SignActivity : AppCompatActivity() {
    var signView:SignWebView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        signView= findViewById(R.id.sign_view) as SignWebView
        CookieSyncManager.createInstance(this)
        change2Auth()
    }

    override fun onDestroy() {
        //清除cookies
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null)
        } else {

            CookieSyncManager.createInstance(this@SignActivity)
            val cookieManager = CookieManager.getInstance()
            cookieManager?.removeAllCookie()
            CookieSyncManager.getInstance().sync()
        }
        super.onDestroy()
    }
    private fun change2Auth(){
        signView!!.setSignActivity(this)
        Thread {
            kotlin.run {
                try {
                    var url = BaiduSignServer().method("getAuthUrl", String::class.java).call() as String
                    runOnUiThread {
                        signView!!.loadUrl(url)
                    }
                }
                catch (e:Exception)
                {
                    Log.e("net error",""+e)
                }
            }
        }.start()
    }
}
