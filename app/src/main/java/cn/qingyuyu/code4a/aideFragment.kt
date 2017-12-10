package cn.qingyuyu.code4a

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import cn.qingyuyu.code4a.control.DataBaseController
import cn.qingyuyu.code4a.model.ArticleAdapter
import cn.qingyuyu.code4a.model.ArticleList
import cn.qingyuyu.code4a.remote.Remote
import cn.qingyuyu.code4a.remote.bean.Article
import com.hitomi.refresh.view.FunGameRefreshView
import es.dmoral.toasty.Toasty
import java.util.ArrayList

class aideFragment : Fragment() {
    private lateinit var refreshView: FunGameRefreshView
    private var articleTemp= ArticleList.getArticleList(context)
    private lateinit var listView: ListView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_aide, null, false)//实例化

        refreshView = view.findViewById(R.id.refresh)
        refreshView.setLoadingText(getString(R.string.info_loadingtext))
        refreshView.setGameOverText(getString(R.string.info_gaveover))
        refreshView.setLoadingFinishedText(getString(R.string.info_loadingfinish))
        refreshView.setTopMaskText(getString(R.string.info_pulltorefresh))
        refreshView.setBottomMaskText(getString(R.string.info_howtogame))
        listView = view.findViewById(R.id.list_view)
        val ad = ArticleAdapter(activity, R.layout.articlelist_item, articleTemp.aideList)
        listView.adapter = ad
        listView.onItemClickListener= AdapterView.OnItemClickListener{ adapterView, view, i, l->
            var intent=Intent()
            intent.setClass(activity,ViewArticleActivity::class.java)
            intent.putExtra("articleid",articleTemp.aideList[i].id)
            intent.putExtra("userid",articleTemp.aideList[i].user)
            intent.putExtra("title",articleTemp.aideList[i].title)
            startActivity(intent)
        }
        refreshView.setOnRefreshListener(object : FunGameRefreshView.FunGameRefreshListener {

            var articleList:Any?=null


            override fun onPullRefreshing() {

                try {
                    articleList = Remote.category.method("getArticleById", Article::class.java).call(2,1, 10)
                    if (articleList is ArrayList<*>) {
                        articleTemp.setArticles(articleList as ArrayList<Article>,1)
                    }
                }
                catch (e:Exception)
                {
                    Log.e("net error", "" + e)
                }
            }

            override fun onRefreshComplete() {
                ad.notifyDataSetChanged()
                Toasty.success(activity, getString(R.string.info_loadingfinish), Toast.LENGTH_SHORT).show()
            }
        })



        return view


    }



}