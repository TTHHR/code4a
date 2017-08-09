package cn.qingyuyu.code4droid
import android.os.Bundle
import android.preference.PreferenceFragment

/**
 * Created by Administrator on 2017\8\5 0005.
 */
class SettingPreference : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 所的的值将会自动保存到SharePreferences
        addPreferencesFromResource(R.xml.setting)
    }
}