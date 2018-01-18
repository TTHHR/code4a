package cn.atd3.code4a.view.view


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.ViewPager
import android.support.v4.view.GravityCompat

import android.view.View
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import cn.atd3.code4a.R
import cn.atd3.code4a.model.adapter.TabFragmentAdapter
import cn.atd3.code4a.model.inter.MessageModelInterface

import android.widget.LinearLayout
import cn.atd3.code4a.Constant
import cn.atd3.code4a.model.model.CategoryModel

import cn.atd3.code4a.presenter.DatabasePresenter
import cn.atd3.code4a.presenter.MainPresenter
import cn.dxkite.common.ui.notification.PopBanner
import cn.dxkite.common.ui.notification.popbanner.Adapter
import cn.dxkite.common.ui.notification.popbanner.Information
import cn.atd3.code4a.view.inter.MainViewInterface
import cn.dxkite.common.StorageData
import cn.dxkite.debug.DebugManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.File

class MainActivity : AppCompatActivity() ,MainViewInterface, NavigationView.OnNavigationItemSelectedListener{


    lateinit var myViewPager: ViewPager
    lateinit var head_iv: ImageView
    lateinit var uname: TextView
    private var tagList:List<Button> = ArrayList<Button>()

    private lateinit var newarticle:FloatingActionButton
    //把Fragment添加到List集合里面
    var fragmentList:List<ArticleFragment> = ArrayList<ArticleFragment>()

    private lateinit var mp:MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)//title bar
        val i = intent
        if (i.getStringExtra("url") != null) {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", i.getStringExtra("url"))
            startActivity(intent)
        }
        mp=MainPresenter(this)//Presenter
        initView()//初始化控件
        bindListener()//绑定事件

        // 异常报告
        DebugManager.askIfCrash(this,R.drawable.ic_launcher)
        // 固定横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    private fun initView()
    {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val catelist: List<CategoryModel>  = StorageData.loadObject(File(Constant.getPrivateFilePath() + Constant.categoryListFile)) as List<CategoryModel>
        val tagListLayout=findViewById<LinearLayout>(R.id.tagList)
        val defaultButton:Button = Button(applicationContext)
        defaultButton.text="首页"
        defaultButton.id=0
        defaultButton.setBackgroundColor(resources.getColor(R.color.btn_unable))
        tagList=tagList.plus(defaultButton)

        for ( cate:CategoryModel in catelist){
            val button=Button(applicationContext)
            button.text=cate.name
            button.id=cate.id
            button.setBackgroundColor(resources.getColor(R.color.btn_enable))
            tagList=tagList.plus(button)
        }

        for (btn:Button in tagList ){
            val af=ArticleFragment()
            af.init(btn.id)
            fragmentList=fragmentList.plus(af)
            btn.setOnClickListener { view ->
                for ((index,btn:Button) in tagList.withIndex() ){
                    if( btn.id ==view.id ){
                        btn.setBackgroundColor(resources.getColor(R.color.btn_unable))
                        myViewPager.currentItem =index
                    }else{
                        btn.setBackgroundColor(resources.getColor(R.color.btn_enable))
                    }
                }
            }
            tagListLayout.addView(btn)
        }


        myViewPager = findViewById(R.id.myViewPager)

         newarticle = findViewById(R.id.newArticle)
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val drawview = navigationView.inflateHeaderView(R.layout.nav_header_main)
        uname = drawview.findViewById(R.id.uname)
        head_iv = drawview.findViewById(R.id.headImage)

    }

    private fun bindListener(){
        //绑定adapter
        myViewPager.adapter=TabFragmentAdapter(supportFragmentManager,fragmentList)

        //写新文章按钮
        newarticle.setOnClickListener(View.OnClickListener { view ->
        val i=Intent(this,EditArticleActivity::class.java)
            startActivity(i)
        })


        //测试登陆
        head_iv.setOnClickListener(View.OnClickListener {
            var i=Intent(this,SigninActivity::class.java)
            startActivity(i)
        })


        //策划栏点击事件
        nav_view.setNavigationItemSelectedListener(this)


        class PageChange : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {

                for ((index,btn:Button) in tagList.withIndex() ){
                    if( index ==position ){
                        btn.setBackgroundColor(resources.getColor(R.color.btn_unable))
                    }else{
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

    override fun showMessageBanner(m: MessageModelInterface) {//显示通知消息
        Thread(Runnable {
            val bar = PopBanner(this@MainActivity, toolbar, R.mipmap.broadcast)
            bar.messageAdapter = Adapter {
                // 模拟从服务器获取信息
                val info=Information()
                info.message=m.messge
                info.url=m.url
                info.isTouchable = true
                info.time=5000
                info.color="#222222"
                info.backgroundColor="#cccccc"
                info
            }
            bar.update()
            runOnUiThread {
                bar.show()
            }
        }).start()
    }
    //返回键
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
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

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onDestroy() {

        DatabasePresenter().clearArticles(this)//清空之前数据库

        for(f in fragmentList)
        {
            f.onSaveEvent()//储存数据
        }

        super.onDestroy()
    }



}
