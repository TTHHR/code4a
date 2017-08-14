package cn.qingyuyu.code4droid


import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceScreen
import android.util.Log
import android.widget.Toast
import cn.qingyuyu.code4droid.model.User
import com.baidu.api.Baidu
import android.os.Handler


class SettingActivity : PreferenceActivity(), Preference.OnPreferenceChangeListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 所的的值将会自动保存到SharePreferences
        addPreferencesFromResource(R.xml.setting)

    }

    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?): Boolean {

        if (preference!!.title == this.getString(R.string.setting_loginout) && User.instance.isLogind) {
            val baidu = Baidu(SomeValue.clientId, this@SettingActivity)
            Log.e("logout", "success")
            baidu.clearAccessToken()
            User.instance.logout()
            Toast.makeText(this@SettingActivity, "已退出登录", Toast.LENGTH_LONG).show()
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }

    override fun onPreferenceChange(preference: Preference?, p1: Any?): Boolean {
        Log.e("change", "" + preference!!.title)
        if (preference.title == this.getString(R.string.setting_language)) {

            Toast.makeText(this, "程序将在重启后生效", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable {
                System.exit(0)
            }, 1000)//1000毫秒后执行上面的跳转主界面语句
        }
        return true
    }
}
