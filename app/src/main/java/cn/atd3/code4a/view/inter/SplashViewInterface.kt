package cn.atd3.code4a.view.inter

import android.net.Uri

import cn.qingyuyu.commom.ui.SplashAd

/**
 * Created by harry on 2018/1/12.
 */

interface SplashViewInterface {

    fun onImageUpdate(imguri: Uri)

    fun showToast(infotype: Int, info: String)

    fun getXmlString(resourceId: Int): String

    fun setSplashAdListener(sal: SplashAd.SplashAdListener)

    fun showAd(showtime: Int)

}
