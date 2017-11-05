package cn.dxkite.baidusign

import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class SignActivity : AppCompatActivity() {
    var signView:SignWebView?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        signView= findViewById(R.id.sign_view) as SignWebView
        change2Auth()
    }

    private fun change2Auth(){
        signView!!.setSignActivity(this)
        Thread {
            kotlin.run {
                var url=BaiduSignServer().method("getAuthUrl",String::class.java).call() as String;
                runOnUiThread {
                    signView!!.loadUrl(url);
                }
            }
        }.start()
    }
}
