package cn.qingyuyu.code4droid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Environment
import android.os.Handler
import android.widget.Toast
import cn.qingyuyu.commom.SomeValue
import es.dmoral.toasty.Toasty
import java.io.File


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var userDir= File(Environment.getExternalStorageDirectory().toString()+ SomeValue.userDir)
        if(!userDir.exists())
            try {
                userDir.mkdir()
            }catch (e:Exception)
            {
                Toasty.warning(this@SplashActivity,getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
            }
        Handler().postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            //跳转到主界面
            this@SplashActivity.startActivity(intent)
            this@SplashActivity.finish()
        }, 1000)//1000毫秒后执行上面的跳转主界面语句
    }


}
