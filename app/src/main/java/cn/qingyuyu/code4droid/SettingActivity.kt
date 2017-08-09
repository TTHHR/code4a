package cn.qingyuyu.code4droid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.os.Handler
import android.widget.Toast


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingPreference()).commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Toast.makeText(this,"程序重启后生效",Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable {
               System.exit(0)
            }, 2000)//1000毫秒后执行
        }
        return super.onKeyDown(keyCode, event)
    }
}
