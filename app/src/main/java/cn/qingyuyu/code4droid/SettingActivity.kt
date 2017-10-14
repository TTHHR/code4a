package cn.qingyuyu.code4droid


import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

var language = ""

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
        language = PreferenceManager.getDefaultSharedPreferences(this).getString("language", "miao")
    }

    override fun onStop() {
        super.onStop()
        if (language != PreferenceManager.getDefaultSharedPreferences(this).getString("language", "miao"))//语言已经改变
        {
            Toast.makeText(this, "The language has changed,App will exit!", Toast.LENGTH_LONG).show()
            android.os.Process.killProcess(android.os.Process.myPid())//退出程序
        }
    }
}
