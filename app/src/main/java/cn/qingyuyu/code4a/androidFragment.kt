package cn.qingyuyu.code4a

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import cn.qingyuyu.code4a.control.DataBaseController
import cn.qingyuyu.code4a.model.ArticleAdapter
import cn.qingyuyu.code4a.model.ArticleList
import cn.qingyuyu.code4a.remote.Remote
import cn.qingyuyu.code4a.remote.bean.Article
import com.hitomi.refresh.view.FunGameRefreshView
import es.dmoral.toasty.Toasty
import java.util.ArrayList

class androidFragment : Fragment() {
    private lateinit var refreshView: FunGameRefreshView
    private var articleTemp= ArticleList.getArticleList(context)
    private lateinit var listView: ListView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_android, null, false)//实例化


        refreshView = view.findViewById(R.id.refresh)
        refreshView.setLoadingText(getString(R.string.info_loadingtext))
        refreshView.setGameOverText(getString(R.string.info_gaveover))
        refreshView.setLoadingFinishedText(getString(R.string.info_loadingfinish))
        refreshView.setTopMaskText(getString(R.string.info_pulltorefresh))
        refreshView.setBottomMaskText(getString(R.string.info_howtogame))
        listView = view.findViewById(R.id.list_view)
        val ad = ArticleAdapter(activity, R.layout.articlelist_item, articleTemp.androidList)
        listView.adapter = ad
        Log.e("androidfragment","list"+articleTemp.androidList+"ad"+ad)
        listView.onItemClickListener= android.widget.AdapterView.OnItemClickListener{ adapterView, view, i, l->
            var intent= Intent()
            intent.setClass(activity,ViewArticleActivity::class.java)
            intent.putExtra("articleid",articleTemp.androidList[i].id)
            intent.putExtra("userid",articleTemp.androidList[i].user)
            intent.putExtra("title",articleTemp.androidList[i].title)
            startActivity(intent)
        }
        refreshView.setOnRefreshListener(object : FunGameRefreshView.FunGameRefreshListener {

            var articleList:Any?=null

            override fun onPullRefreshing() {

                try {
                    articleList = Remote.category.method("getArticleById", Article::class.java).call(3,1, 10)
                    if (articleList is ArrayList<*>) {
                        articleTemp.setArticles(articleList as ArrayList<Article>,2)
                    }
                }
                catch (e:Exception)
                {
                    android.util.Log.e("net error", "" + e)
                }

            }

            override fun onRefreshComplete() {
                ad.notifyDataSetChanged()
                Toasty.success(activity, getString(R.string.info_loadingfinish), android.widget.Toast.LENGTH_SHORT).show()
            }
        })

        return view


    }



}