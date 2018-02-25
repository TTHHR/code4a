package cn.atd3.code4a.view.view


import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import cn.atd3.code4a.Constant
import cn.atd3.code4a.R
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_setting.*

var language = ""
class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)
        setContentView(R.layout.activity_setting)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingFragment())
                .commit()

        language = PreferenceManager.getDefaultSharedPreferences(this).getString("language", "miao")
    }

    override fun onStart() {
        try {
            topBar.setBackgroundColor(Color.parseColor(Constant.themeColor))
        }
        catch (e:Exception)
        {
            topBar.setBackgroundColor(Color.parseColor(Constant.defaultThemeColor))
        }
        super.onStart()
    }
    override fun onStop() {
        super.onStop()
        if (language != PreferenceManager.getDefaultSharedPreferences(this).getString("language", "miao"))//语言已经改变
        {
            android.os.Process.killProcess(android.os.Process.myPid())//退出程序
        }
        //用户可能修改了DEBUG模式
        Constant.debugmodeinfo = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("debug", false)
        //同上
        Constant.collectioninfo = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("collection", false)
        Constant.themeColor = PreferenceManager.getDefaultSharedPreferences(this).getString("themeColor", Constant.defaultThemeColor)
    }
}

