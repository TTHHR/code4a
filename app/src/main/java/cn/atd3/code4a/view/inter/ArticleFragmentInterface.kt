package cn.atd3.code4a.view.inter

import cn.atd3.code4a.model.model.ArticleModel
import java.util.ArrayList


/**
 * Created by harry on 2018/1/14.
 */

internal interface ArticleFragmentInterface {
    fun upDate(al:ArrayList<ArticleModel> )
    fun setAdapter(al:ArrayList<ArticleModel>)
    fun showToast(infotype:Int , info:String)
}
