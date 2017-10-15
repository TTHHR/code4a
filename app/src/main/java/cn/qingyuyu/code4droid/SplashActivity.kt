package cn.qingyuyu.code4droid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler



class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            //跳转到主界面
            this@SplashActivity.startActivity(intent)
            this@SplashActivity.finish()
        }, 1000)//1000毫秒后执行上面的跳转主界面语句
    }


}
