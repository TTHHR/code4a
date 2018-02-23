package cn.atd3.code4a.view.view


import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import android.view.KeyEvent
import android.view.View

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

import cn.atd3.code4a.Constant
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.SplashPresenter
import cn.atd3.code4a.view.inter.SplashViewInterface
import cn.qingyuyu.commom.ui.SplashAd

import android.view.KeyEvent.KEYCODE_BACK
import cn.atd3.code4a.Constant.ERROR
import cn.atd3.code4a.Constant.INFO
import cn.atd3.code4a.Constant.NORMAL
import cn.atd3.code4a.Constant.SUCCESS
import cn.atd3.code4a.Constant.WARNING
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity(), SplashViewInterface {

    private var sp: SplashPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //将window的背景图设置为空
        window.setBackgroundDrawableResource(android.R.color.white)
        setContentView(R.layout.activity_splash)
        splashView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)


        sp = SplashPresenter(this, this)//实例化，并将自身做参数

        sp!!.requestPermissions(this)//请求权限

        sp!!.requestAdInfo()//请求广告信息

        sp!!.setSplashAdListener(this, splashAd)//设置广告监听

        sp!!.showAd(3000)//显示广告3秒

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        sp!!.onRequestPermissionsResult(requestCode, grantResults)//Presenter处理请求权限结果
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onImageUpdate(imguri: Uri) {

        runOnUiThread {
            if (splashAd!= null)
                splashAd!!.setAdImageURI(imguri)
            //设置图片
        }

    }


    override fun showToast(infotype: Int, info: String) {

        runOnUiThread {
            val tipDialog: QMUITipDialog
            when (infotype) {
                SUCCESS -> tipDialog = QMUITipDialog.Builder(this@SplashActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(info)
                        .create()
                INFO -> tipDialog = QMUITipDialog.Builder(this@SplashActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(info)
                        .create()
                NORMAL -> tipDialog = QMUITipDialog.Builder(this@SplashActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
                WARNING -> tipDialog = QMUITipDialog.Builder(this@SplashActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(info)
                        .create()
                ERROR -> tipDialog = QMUITipDialog.Builder(this@SplashActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(if (Constant.debugmodeinfo) info else getString(R.string.remote_error))
                        .create()
                else -> tipDialog = QMUITipDialog.Builder(this@SplashActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
            }
            tipDialog.show()
            Thread {
                        try {
                            Thread.sleep(1500)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } finally {
                            tipDialog.dismiss()
                        }
                    }.start()
        }

    }

    override fun getXmlString(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun setSplashAdListener(sal: SplashAd.SplashAdListener) {
        if (splashAd != null)
            splashAd!!.setSplashAdListener(sal)
    }

    override fun showAd(showtime: Int) {
        runOnUiThread {
            if (splashAd != null)
                splashAd!!.show(this@SplashActivity, showtime)
        }
    }

}
