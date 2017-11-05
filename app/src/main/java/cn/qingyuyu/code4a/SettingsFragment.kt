package cn.qingyuyu.code4a

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.widget.Toast
import cn.qingyuyu.code4a.control.LoginDealController
import cn.qingyuyu.code4a.model.User
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
            var l=LoginDealController()
            l.call("logout",null, null)
            Toasty.success(activity,"退出登陆成功，Sucess",Toast.LENGTH_SHORT).show()
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)

    }
}