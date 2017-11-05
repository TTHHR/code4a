package cn.qingyuyu.code4a

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by harrytit on 2017/10/11.
 */
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.setting)
    }

    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?): Boolean {
        if(preference!!.title==getString(R.string.setting_language))
        {
            Toasty.info(activity,getString(R.string.info_changelanguage), Toast.LENGTH_SHORT).show()
        }
        if(preference.title==getString(R.string.setting_loginout))
        {
            //退出登陆操作
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)

    }
}