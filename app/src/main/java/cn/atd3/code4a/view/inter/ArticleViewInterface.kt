package cn.atd3.code4a.view.inter

import android.text.Html

/**
 * Created by harry on 2018/1/15.
 */
interface ArticleViewInterface {
    fun showWaitDialog()
    fun dismissWaitDialog()
    fun loadArticle(text:String,ig:Html.ImageGetter)
    fun loadUser( un:String)
    fun showToast(infotype: Int, info: String)

    fun getXmlString(resourceId: Int): String
}