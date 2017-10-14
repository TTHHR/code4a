package cn.qingyuyu.code4droid

import android.app.Application
import android.preference.PreferenceManager
import java.util.*
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.ImageLoader



/**
 * Created by Administrator on 2017\8\6 0006.
 */
class CodeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val language: String = PreferenceManager.getDefaultSharedPreferences(this).getString("language", "miao")
// 本地语言设置
        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        if (language == "miao")//就是没有设置
            conf.locale = Locale.getDefault()
        else
            conf.locale = Locale(language)
        res.updateConfiguration(conf, dm)


        val imageLoader = ImageLoader.getInstance()  //初始化图片加载
        imageLoader.init(ImageLoaderConfiguration.createDefault(this))


    }
}