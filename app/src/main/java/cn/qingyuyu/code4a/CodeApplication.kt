package cn.qingyuyu.code4a

import android.app.Application
import android.os.Looper
import android.preference.PreferenceManager

import android.widget.Toast
import cn.atd3.proxy.ProxyConfig

import cn.atd3.proxy.exception.ServerException

import cn.dxkite.common.CrashHandler
import cn.dxkite.common.ExceptionHandler
import cn.qingyuyu.code4a.control.SignController
import java.util.*
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.ImageLoader
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


/**
 * App应用控制器
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
        // 异常处理
        CrashHandler.getInstance().init(applicationContext)
        // 预定义处理器
        initGlobalHandler()
        // 设置RPC请求超时 5秒
        ProxyConfig.setTimeOut(5000)
        // 设置RPC控制器
        ProxyConfig.setCookiePath(applicationContext.filesDir.absolutePath)
        ProxyConfig.setController(SignController())
    }
    private  fun initGlobalHandler(){
        val serverTimeout= ExceptionHandler { context, thread, exception ->
            kotlin.run {
                Looper.prepare()
                Toast.makeText(context, getString(R.string.server_timeout), Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }
        CrashHandler.addHander(TimeoutException::class.java ,serverTimeout)
        CrashHandler.addHander(UnknownHostException::class.java,serverTimeout)
        CrashHandler.addHander(ServerException::class.java){ context, thread, exception ->
            kotlin.run {
                Looper.prepare()
                Toast.makeText(context,getString(R.string.server_exception), Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }

    }
}