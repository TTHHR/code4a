package cn.qingyuyu.c4droidcode

import android.app.Application
import android.preference.PreferenceManager
import java.util.*

/**
 * Created by Administrator on 2017\8\6 0006.
 */
class CodeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val language:String= PreferenceManager.getDefaultSharedPreferences(this).getString("language","zh")
// 本地语言设置
        var myLocale = Locale(language)
        var res =this.resources
        var dm = res.displayMetrics
        var conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

    }
}