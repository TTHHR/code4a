package cn.qingyuyu.code4a

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import cn.qingyuyu.code4a.control.DataBaseController
import cn.qingyuyu.code4a.model.ArticleAdapter
import cn.qingyuyu.code4a.model.ArticleList
import cn.qingyuyu.code4a.remote.Remote
import cn.qingyuyu.code4a.remote.bean.Article
import com.hitomi.refresh.view.FunGameRefreshView
import es.dmoral.toasty.Toasty
import java.util.*
import android.content.Intent

class ContentFragment : Fragment() {
        private var kind=1
    private lateinit var refreshView: FunGameRefreshView
    private var articleTemp=ArticleList.getArticleList(context)
    private var articleData=ArrayList<Article>()
    private lateinit var listView: ListView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_content, null, false)//实例化

        val c4droid=view.findViewById<Button>(R.id.c4droid)
        val aide=view.findViewById<Button>(R.id.aide)
        val android=view.findViewById<Button>(R.id.android)

        refreshView = view.findViewById(R.id.refresh)
        refreshView.setLoadingText(getString(R.string.info_loadingtext))
        refreshView.setGameOverText(getString(R.string.info_gaveover))
        refreshView.setLoadingFinishedText(getString(R.string.info_loadingfinish))
        refreshView.setTopMaskText(getString(R.string.info_pulltorefresh))
        refreshView.setBottomMaskText(getString(R.string.info_howtogame))
        listView = view.findViewById(R.id.list_view)
        copyList(articleData,articleTemp.c4droidList)
        val ad = ArticleAdapter(activity,R.layout.articlelist_item,articleData)
        listView.adapter = ad
        listView.onItemClickListener= AdapterView.OnItemClickListener{ adapterView,view,i,l->
           var intent=Intent()
            intent.setClass(activity,ViewArticleActivity::class.java)
            intent.putExtra("id",articleData[i].id)
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
                    Log.e("get Article","kind="+kind)
                    //articleList = Remote.article.method("getList", Article::class.java).call(1, 10)
                    articleList = Remote.category.method("getArticleById", Article::class.java).call(kind,1, 10)
                    if (articleList is ArrayList<*>) {
                        articleTemp.setArticles(articleList as ArrayList<Article>,kind)
                        when(kind)
                        {
                            1->copyList(articleData,articleTemp.c4droidList)
                            2->copyList(articleData,articleTemp.aideList)
                            3->copyList(articleData,articleTemp.androidList)
                        }

                    }
                }
                catch (e:Exception)
                {
                    Log.e("net error",""+e)
                }
//                cover=Remote.article.method("getCover",File::class.java).call(Param("article",1));

//                baiduInfo=Remote.baiduUser.method("getInfo", BaiduUserInfo::class.java).call()
            }

            override fun onRefreshComplete() {
                ad.notifyDataSetChanged()
                Toasty.success(activity, getString(R.string.info_loadingfinish), Toast.LENGTH_SHORT).show()
            }
        })

        c4droid.setOnClickListener(View.OnClickListener {
            kind=1
            c4droid.setBackgroundColor(resources.getColor(R.color.btn_unable))
            aide.setBackgroundColor(resources.getColor(R.color.btn_enable))
            android.setBackgroundColor(resources.getColor(R.color.btn_enable))
            copyList(articleData,articleTemp.c4droidList)
            ad.notifyDataSetChanged()
        })
        aide.setOnClickListener(View.OnClickListener {
            kind=2
            aide.setBackgroundColor(resources.getColor(R.color.btn_unable))
            c4droid.setBackgroundColor(resources.getColor(R.color.btn_enable))
            android.setBackgroundColor(resources.getColor(R.color.btn_enable))
            copyList(articleData,articleTemp.aideList)
            ad.notifyDataSetChanged()
        })
        android.setOnClickListener(View.OnClickListener {
            kind=3
            android.setBackgroundColor(resources.getColor(R.color.btn_unable))
            c4droid.setBackgroundColor(resources.getColor(R.color.btn_enable))
            aide.setBackgroundColor(resources.getColor(R.color.btn_enable))
            copyList(articleData,articleTemp.androidList)
            ad.notifyDataSetChanged()
        })


        return view


    }

    override fun onDestroy() {
        var data= DataBaseController()
        data.clearArticles(context)
        data.saveArticles(context,articleTemp.c4droidList)
        data.saveArticles(context,articleTemp.aideList)
        data.saveArticles(context,articleTemp.androidList)
        super.onDestroy()
    }
    fun copyList( m :ArrayList<Article>  ,  s: ArrayList<Article>)
    {
        m.clear()
        m.addAll(s)
    }
}
