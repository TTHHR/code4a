package cn.qingyuyu.code4droid

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import cn.atd3.proxy.Param
import cn.qingyuyu.code4droid.model.ListData
import cn.qingyuyu.code4droid.remote.Remote
import cn.qingyuyu.code4droid.remote.bean.Article
import cn.qingyuyu.code4droid.remote.bean.BaiduUserInfo
import cn.qingyuyu.code4droid.remote.bean.UserInfo
import com.hitomi.refresh.view.FunGameRefreshView
import es.dmoral.toasty.Toasty
import java.util.ArrayList

class ContentFragment : Fragment() {


    private lateinit var refreshView: FunGameRefreshView

    private lateinit var listView: ListView
    var ld = ListData()
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
        val ad = ArrayAdapter<String>(activity, android.R.layout.simple_expandable_list_item_1, ld.listData)
        listView.adapter = ad
        refreshView.setOnRefreshListener(object : FunGameRefreshView.FunGameRefreshListener {
//            var userInfo:Any?=null;
//            var baiduInfo:Any?=null;
            var articleList:Any?=null;

            override fun onPullRefreshing() {
//                ld.upDate()

                // 测试获取用户信息
//                userInfo= Remote.user.method("getInfo", UserInfo::class.java).call()
                articleList= Remote.article.method("getList",  Article::class.java).call(1,10)
                if (articleList is ArrayList<*>){
                    ld.setArticles(articleList as ArrayList<Article>)
                }
//                baiduInfo=Remote.baiduUser.method("getInfo", BaiduUserInfo::class.java).call()
            }

            override fun onRefreshComplete() {
                ad.notifyDataSetChanged()
                Toasty.success(activity, "加载完成", Toast.LENGTH_SHORT).show()
            }
        })

        return view


    }
}
