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
import cn.qingyuyu.code4droid.model.ListData
import cn.qingyuyu.code4droid.remote.Remote
import cn.qingyuyu.code4droid.remote.bean.UserInfo
import com.hitomi.refresh.view.FunGameRefreshView
import es.dmoral.toasty.Toasty

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
            var userInfo:Any?=null;
            override fun onPullRefreshing() {
                ld.upDate()
                // 模拟后台耗时任务
                // 测试获取用户信息
                userInfo= Remote.user.method("getInfo", UserInfo::class.java).call()
                // SystemClock.sleep(2000)
            }

            override fun onRefreshComplete() {
                ad.notifyDataSetChanged()
                Toasty.success(activity, "加载完成:"+userInfo, Toast.LENGTH_SHORT).show()
            }
        })

        return view


    }
}
