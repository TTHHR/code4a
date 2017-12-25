package cn.qingyuyu.code4a

import android.annotation.SuppressLint
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
import cn.dxkite.common.CrashHandler
import cn.qingyuyu.code4a.model.ArticleAdapter
import cn.qingyuyu.code4a.model.ArticleList
import cn.qingyuyu.code4a.remote.Remote
import cn.qingyuyu.code4a.remote.bean.Article
import com.hitomi.refresh.view.FunGameRefreshView
import es.dmoral.toasty.Toasty
import java.net.UnknownHostException
import java.util.ArrayList

@SuppressLint("ValidFragment")
class ArticleFragment(val kind:Int) : Fragment() {
    private lateinit var refreshView: FunGameRefreshView
    private var articleTemp= ArticleList.getArticleList(context)
    private lateinit var listView: ListView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article, null, false)//实例化
        Log.e("kind", "" + kind)
        refreshView = view.findViewById(R.id.refresh)
        refreshView.setLoadingText(getString(R.string.info_loadingtext))
        refreshView.setGameOverText(getString(R.string.info_gaveover))
        refreshView.setLoadingFinishedText(getString(R.string.info_loadingfinish))
        refreshView.setTopMaskText(getString(R.string.info_pulltorefresh))
        refreshView.setBottomMaskText(getString(R.string.info_howtogame))
        listView = view.findViewById(R.id.list_view)



        val ad = ArticleAdapter(activity, R.layout.articlelist_item, articleTemp.getArticleList(kind))
        listView.adapter = ad

        listView.onItemClickListener= AdapterView.OnItemClickListener{ adapterView, view, i, l->
           val intent= Intent()
            intent.setClass(activity, ViewArticleActivity::class.java)
            if(articleTemp.getArticleList(kind)[i].id!=null)
           intent.putExtra("articleid",articleTemp.getArticleList(kind)[i].id)
            intent.putExtra("userid",articleTemp.getArticleList(kind)[i].user)
            intent.putExtra("title",articleTemp.getArticleList(kind)[i].title)
            startActivity(intent)
        }
        refreshView.setOnRefreshListener(object : FunGameRefreshView.FunGameRefreshListener {
//            var userInfo:Any?=null;
//            var baiduInfo:Any?=null;
            var articleList:Any?=null
//            var cover:Any?=null;

            override fun onPullRefreshing() {
//                ld.upDate()

                // 测试获取用户信息
//                userInfo= Remote.user.method("getInfo", UserInfo::class.java).call()
                try {
                    articleList = Remote.category.method("getArticleById", Article::class.java).call(kind+1,1, 10)
                    if (articleList is ArrayList<*>) {
                        articleTemp.setArticles(articleList as ArrayList<Article>,kind)
                    }
                }
                catch (e: UnknownHostException)
                {
                    Log.e("Article","Host Error",e);
                }
//                cover=Remote.article.method("getCover",File::class.java).call(Param("article",1));

//                baiduInfo=Remote.baiduUser.method("getInfo", BaiduUserInfo::class.java).call()
            }

            override fun onRefreshComplete() {
                ad.notifyDataSetChanged()
                Toasty.success(activity, getString(R.string.info_loadingfinish), Toast.LENGTH_SHORT).show()

            }
        })

        return view

    }



}