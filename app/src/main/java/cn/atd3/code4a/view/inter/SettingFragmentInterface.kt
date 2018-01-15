package cn.atd3.code4a.view.inter

import android.app.Activity

/**
 * Created by harry on 2018/1/15.
 */

interface SettingFragmentInterface {
    fun showToast(infotype: Int, info: String)
    fun getXmlString(resourceId: Int): String
    fun openWebActivity( url:String)
}
