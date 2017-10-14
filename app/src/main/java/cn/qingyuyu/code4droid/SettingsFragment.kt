package cn.qingyuyu.code4droid

import android.os.Bundle
import android.preference.PreferenceFragment

/**
 * Created by harrytit on 2017/10/11.
 */
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.setting)
    }

}