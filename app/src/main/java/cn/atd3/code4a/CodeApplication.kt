package cn.atd3.code4a

import android.app.Application
import android.preference.PreferenceManager
import cn.code4a.ProxyController
import cn.atd3.proxy.ProxyConfig
import cn.dxkite.debug.Config
import cn.dxkite.debug.DebugManager
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import java.io.File
import java.util.*


/**
 * App应用控制器
 * Created by Administrator on 2017\8\6 0006.
 */
class CodeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化全局常量
        Constant.init(applicationContext)
        initCrashManager()
        initProxyManager()
        initSetting()
        // 初始化图片加载
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
    }

    private fun initProxyManager() {
        // 设置RPC请求超时 3秒
        ProxyConfig.setTimeOut(3000)
        // 设置RPC控制器
        ProxyConfig.setCookiePath(Constant.getPrivateFilePath()+File.separator + "cookies")
        ProxyConfig.setController(ProxyController())
    }

    private fun initCrashManager() {
        DebugManager.config(applicationContext,Config()
                .setSavePath(Constant.getPublicFilePath()+ File.separator + "crash-log")
                .setUploadSavePath(Constant.getPrivateFilePath()+ File.separator + "crash-log")
                .setUpstream("")
                .setCrashDumpPath(Constant.getPrivateFilePath()+File.separator + "crash-dump")
                .setDebug(Constant.isDebug()))
    }


    private fun initSetting()
    {

        //语言设置
        val language: String? = PreferenceManager.getDefaultSharedPreferences(this).getString("language", null)
        // 本地语言设置
        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        if (language == null)
            conf.locale = Locale.getDefault()
        else
            conf.locale = Locale(language)
        res.updateConfiguration(conf, dm)


        //debug模式
       Constant.debugmodeinfo = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("debug", false)

        //收集信息
        Constant.collectioninfo=PreferenceManager.getDefaultSharedPreferences(this).getBoolean("collection",false)
    }
}