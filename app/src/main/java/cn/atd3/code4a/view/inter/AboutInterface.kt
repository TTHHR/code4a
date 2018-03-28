package cn.atd3.code4a.view.inter

import cn.atd3.code4a.model.model.UpdateInfo


/**
 * Created by harry on 2018/1/15.
 */

interface AboutInterface {
    fun showToast(infotype: Int, info: String)
    fun getXmlString(resourceId: Int): String
    fun openWebActivity( url:String)
    fun showWaitDialog()
    fun dismissWaitDialog()
    fun showUpdateInfo(ui: UpdateInfo)
}
