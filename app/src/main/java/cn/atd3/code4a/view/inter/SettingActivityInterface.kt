package cn.atd3.code4a.view.inter


/**
 * Created by harry on 2018/1/15.
 */

interface SettingActivityInterface {
    fun showToast(infotype: Int, info: String)
    fun getXmlString(resourceId: Int): String
}
