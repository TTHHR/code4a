package cn.atd3.code4a

import android.app.Application
import cn.atd3.code4a.model.model.SettingModel
import cn.code4a.ProxyController
import cn.atd3.proxy.ProxyConfig
import cn.dxkite.debug.Config
import cn.dxkite.debug.DebugManager
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.util.*


/**
 * App应用控制器
 * Created by Administrator on 2017\8\6 0006.
 */
//todo 用户登陆判断
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
        //初始化Logger
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    private fun initProxyManager() {
        // 设置RPC请求超时 3秒
        ProxyConfig.setTimeOut(3000)
        // 设置RPC控制器
        ProxyConfig.setCookiePath(Constant.getPrivateFilePath() + File.separator + "cookies")
        ProxyConfig.setController(ProxyController())
    }

    private fun initCrashManager() {
        DebugManager.config(applicationContext, Config()
                .setSavePath(Constant.getPublicFilePath() + File.separator + "crash-log")
                .setUploadSavePath(Constant.getPrivateFilePath() + File.separator + "crash-log")
                .setUpstream("")
                .setCrashDumpPath(Constant.getPrivateFilePath() + File.separator + "crash-dump")
                .setDebug(Constant.isDebug()))
    }


    private fun initSetting() {
        val sm = readSettingFile()
        if (sm != null) {
            //语言设置
            val language = sm.language
            // 本地语言设置
            val res = this.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = Locale(language)
            res.updateConfiguration(conf, dm)
            //debug模式
            Constant.debugmodeinfo = sm.isDebug

            //收集信息
            Constant.collectioninfo = sm.isCollection

            Constant.themeColor = sm.themeColor
        }

    }

    private fun readSettingFile(): SettingModel? {
        var sm: SettingModel? = null
        var fs: FileInputStream? = null
        var os: ObjectInputStream? = null
        try {
            fs = FileInputStream(Constant.settingFile)
            os = ObjectInputStream(fs)
            val o = os.readObject()
            if (o is SettingModel)
                sm = o
        } catch (e: Exception) {
            e.printStackTrace()
            sm = SettingModel()
        } finally {
            try {
                if (os != null)
                    os.close()
                if (fs != null)
                    fs.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return sm
    }
}