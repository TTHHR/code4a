package cn.atd3.code4a.view.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import cn.atd3.code4a.Constant
import cn.atd3.code4a.R
import cn.atd3.code4a.R.color.bootstrap_gray_lighter
import cn.atd3.code4a.model.adapter.TabFragmentAdapter
import cn.atd3.code4a.model.model.CategoryModel
import cn.atd3.code4a.net.Remote
import cn.atd3.code4a.presenter.MainPresenter
import cn.atd3.code4a.view.inter.MainViewInterface
import cn.dxkite.common.StorageData
import cn.dxkite.common.ui.notification.PopBanner
import cn.dxkite.common.ui.notification.popbanner.Adapter
import cn.dxkite.debug.DebugManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import java.io.File
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), MainViewInterface, NavigationView.OnNavigationItemSelectedListener {

    private var tagList: List<Button> = ArrayList()
    private var exitTime = 0L

    //把Fragment添加到List集合里面
    var fragmentList: List<ArticleFragment> = ArrayList()
    private lateinit var mp: MainPresenter
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //将window的背景图设置为空
        window.setBackgroundDrawableResource(bootstrap_gray_lighter)
        QMUIStatusBarHelper.translucent(this)
        setContentView(R.layout.activity_main)
        try {
            topBar!!.setBackgroundColor(Color.parseColor(Constant.themeColor))
        }
        catch (e:Exception)
        {
            Toast.makeText(this,getString(R.string.waring_error_color),Toast.LENGTH_SHORT).show()
            topBar!!.setBackgroundColor(Color.parseColor(Constant.defaultThemeColor))
        }
        topBar!!.addLeftImageButton(R.mipmap.top_more,1).setOnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            else
            {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

        topBar!!.setTitle(getString(R.string.app_name))

        // 菜单按钮
        topBar!!.addRightImageButton(R.mipmap.topbar_menu, 1).setOnClickListener { showBottomSheetList() }
        mp = MainPresenter(this)//Presenter
        initView()//初始化控件
        bindListener()//绑定事件

        // 异常报告
        if (Constant.debugmodeinfo)
            DebugManager.askIfCrash(this, R.drawable.ic_launcher)
        // 固定横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mp.collection(this)  //收集装机信息
        val i = intent
        if (i.getStringExtra("url") != null) {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", i.getStringExtra("url"))
            startActivity(intent)
        }
        else
        {
            mp.showMessageBanner()//拉取通知
        }
    }

    private fun initView() {

        val cateListFile = File(Constant.getCategoryListFilePath())
        val catelist: List<CategoryModel>

        if (cateListFile.exists()) {
            catelist = StorageData.loadObject(cateListFile) as List<CategoryModel>
        } else {
            Log.i(TAG, "load from network faild, load from assets!")
            catelist = StorageData.loadObject(resources.assets.open(Constant.categoryListFile)) as List<CategoryModel>
        }


        val defaultButton = Button(applicationContext)
        defaultButton.text = "首页"
        defaultButton.id = 0
        defaultButton.setBackgroundColor(resources.getColor(R.color.btn_unable))
        tagList = tagList.plus(defaultButton)

        for (cate: CategoryModel in catelist) {
            val button = Button(applicationContext)
            button.text = cate.name
            button.id = cate.id
            button.setBackgroundColor(resources.getColor(R.color.btn_enable))
            tagList = tagList.plus(button)
        }

        for (btn: Button in tagList) {
            val af = ArticleFragment()
            af.init(btn.id)
            fragmentList = fragmentList.plus(af)
            btn.setOnClickListener { view ->
                for ((index, btn: Button) in tagList.withIndex()) {
                    if (btn.id == view.id) {
                        btn.setBackgroundColor(resources.getColor(R.color.btn_unable))
                        myViewPager.currentItem = index
                    } else {
                        btn.setBackgroundColor(resources.getColor(R.color.btn_enable))
                    }
                }
            }
            tagListLayout.addView(btn)
        }


        nav_view.setNavigationItemSelectedListener ( this )

    }




    private fun bindListener() {
        //绑定adapter
        myViewPager.adapter = TabFragmentAdapter(supportFragmentManager, fragmentList)

        //写新文章按钮
        newArticleButton.setOnClickListener({ _ ->
            //添加用户判断
            val i = Intent(this, EditArticleActivity::class.java)
            startActivity(i)
        })

        val navHeadMain=nav_view.inflateHeaderView(R.layout.nav_header_main)
        try {
            navHeadMain!!.setBackgroundColor(Color.parseColor(Constant.themeColor))
        }
        catch (e:Exception)
        {
            navHeadMain!!.setBackgroundColor(Color.parseColor(Constant.defaultThemeColor))
        }
        val headImage=navHeadMain.findViewById<ImageView>(R.id.headImage)
        //测试登陆
        headImage.setOnClickListener({
            //超级用户登陆
//            Thread {
//                        try {
//                             Remote.superUser.method("su").call("", 3)
//                        } catch (e: Exception) {
//                            Log.e("login", e.toString())
//                        }
//                    }.start()
            val i = Intent(this, SigninActivity::class.java)
            startActivity(i)
        })


        class PageChange : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                for ((index, btn: Button) in tagList.withIndex()) {
                    if (index == position) {
                        btn.setBackgroundColor(resources.getColor(R.color.btn_unable))
                    } else {
                        btn.setBackgroundColor(resources.getColor(R.color.btn_enable))
                    }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        }
        myViewPager.addOnPageChangeListener(PageChange())//滑动事件
    }

    override fun showMessageBanner(a: Adapter) {
        val bar = PopBanner(this@MainActivity, topBar, R.mipmap.broadcast)
        bar.messageAdapter = a

        bar.update()
        runOnUiThread {
            bar.show()
        }
    }


    //返回键
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return
        }
            if ((System.currentTimeMillis() - exitTime) > 2000) {
               Toast.makeText(this,getString(R.string.double_click_exit),Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                mp.updateAd()
                super.onBackPressed()
            }
    }

    //创建菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //菜单点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> startActivity(Intent(this@MainActivity, SettingActivity::class.java))

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    //右侧列表点击事件
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        Log.e("item select",""+item.itemId)
        when (item.itemId) {
            R.id.nav_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share")
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content) + Constant.shareUrl)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(Intent.createChooser(intent, title))
            }
            R.id.nav_feedback -> {
                val i = Intent(this@MainActivity, FeedbackActivity::class.java)
                startActivity(i)
            }


        }
        return true
    }
    private fun showBottomSheetList() {
        QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(getString(R.string.setting))
                .setOnSheetItemClickListener { dialog, _, _, _ ->
                    dialog.dismiss()
                    val i=Intent(this,SettingActivity::class.java)
                    startActivity(i)
                }
                .build()
                .show()
    }
}
