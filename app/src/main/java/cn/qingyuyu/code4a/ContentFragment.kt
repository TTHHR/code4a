package cn.qingyuyu.code4a

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

class ContentFragment : Fragment() {


    private lateinit var refreshView: FunGameRefreshView

    private lateinit var listView: ListView
    var ld = ArticleList(context)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_content, container, false)//实例化

        refreshView = view.findViewById(R.id.refresh)
        refreshView.setLoadingText(getString(R.string.info_loadingtext))
        refreshView.setGameOverText(getString(R.string.info_gaveover))
        refreshView.setLoadingFinishedText(getString(R.string.info_loadingfinish))
        refreshView.setTopMaskText(getString(R.string.info_pulltorefresh))
        refreshView.setBottomMaskText(getString(R.string.info_howtogame))
        listView = view.findViewById(R.id.list_view)
        val ad = ArticleAdapter(activity,R.layout.articlelist_item,ld.listData)
        listView.adapter = ad
        listView.onItemClickListener= AdapterView.OnItemClickListener{ adapterView,view,i,l->
            Toasty.info(activity,ld.listData[i].toString(),Toast.LENGTH_SHORT).show()
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
                    articleList = Remote.article.method("getList", Article::class.java).call(1, 10)
                    if (articleList is ArrayList<*>) {
                        ld.setArticles(articleList as ArrayList<Article>)
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
                if(ld.listData.size==1)
                    Toasty.error(activity, getString(R.string.error_network), Toast.LENGTH_SHORT).show()
                else
                Toasty.success(activity, getString(R.string.info_loadingfinish), Toast.LENGTH_SHORT).show()
            }
        })

        return view


    }

    override fun onDestroy() {
        var data= DataBaseController()
        data.clearArticles(context)
        data.saveArticles(context,ld.listData)
        super.onDestroy()
    }
}
