package cn.atd3.code4a.view.view

import android.support.v4.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.ViewPager
import android.support.v4.view.GravityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.model.adapter.TabFragmentAdapter
import cn.atd3.code4a.model.inter.MessageModelInterface
import android.util.Log
import cn.atd3.code4a.net.Remote
import cn.atd3.code4a.presenter.MainPresenter
import cn.dxkite.common.ui.notification.PopBanner
import cn.dxkite.common.ui.notification.popbanner.Adapter
import cn.dxkite.common.ui.notification.popbanner.Information
import cn.atd3.code4a.view.inter.MainViewInterface
import cn.dxkite.debug.DebugManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() ,MainViewInterface, NavigationView.OnNavigationItemSelectedListener{


    lateinit var myViewPager: ViewPager
    lateinit var button_aide: Button
    lateinit var button_android: Button
    lateinit var button_c4droid: Button
    lateinit var head_iv: ImageView
    lateinit var uname: TextView
    private var btnUnableColor = 0
    private var btnEnableColor = 0

    private lateinit var newarticle:FloatingActionButton
    //把Fragment添加到List集合里面
    var fragmentList = mutableListOf(ArticleFragment(C4DROID) as Fragment, ArticleFragment(AIDE) as Fragment, ArticleFragment(ANDROID) as Fragment)
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
        DebugManager.askIfCrash(this,R.drawable.ic_launcher);
    }

    private fun initView()
    {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        myViewPager = findViewById(R.id.myViewPager)

        button_c4droid = findViewById(R.id.c4droid)
        button_aide = findViewById(R.id.aide)
        button_android = findViewById(R.id.android)

        btnUnableColor = ContextCompat.getColor(this@MainActivity, R.color.btn_unable)
        btnEnableColor = ContextCompat.getColor(this@MainActivity, R.color.btn_enable)

         newarticle = findViewById(R.id.newArticle)
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val drawview = navigationView.inflateHeaderView(R.layout.nav_header_main)
        uname = drawview.findViewById(R.id.uname)
        head_iv = drawview.findViewById(R.id.headImage)
        //测试登陆
        head_iv.setOnClickListener(View.OnClickListener {
            var i=Intent(this,SigninActivity::class.java)
            startActivity(i)
        })
    }

    private fun bindListener(){
        //绑定adapter
        myViewPager.adapter=TabFragmentAdapter(supportFragmentManager,fragmentList)

        //写新文章按钮
        newarticle.setOnClickListener(View.OnClickListener { view ->
//            Thread(){
//                kotlin.run {
//                    Log.e("DXKite","perpare signup")
//                    val ret:Any=Remote.user.method("signup").call("dxkite-test","dxkite_test1@qq.com","dxlidx");
//                    Log.e("DXKite",ret.toString())
//                }
//            }.start()
            throw RuntimeException("Something is error")
;            //
        })

        //策划栏点击事件
        nav_view.setNavigationItemSelectedListener(this)
        // 设置TAB的点击事件
        button_aide.setOnClickListener(View.OnClickListener {
            myViewPager.currentItem = AIDE
            button_aide.setBackgroundColor(btnUnableColor)
            button_c4droid.setBackgroundColor(btnEnableColor)
            button_android.setBackgroundColor(btnEnableColor)
        })
        button_android.setOnClickListener(View.OnClickListener {
            myViewPager.currentItem = ANDROID
            button_aide.setBackgroundColor(btnEnableColor)
            button_c4droid.setBackgroundColor(btnEnableColor)
            button_android.setBackgroundColor(btnUnableColor)
        })
        button_c4droid.setOnClickListener(View.OnClickListener {
            myViewPager.currentItem = C4DROID
            button_aide.setBackgroundColor(btnEnableColor)
            button_c4droid.setBackgroundColor(btnUnableColor)
            button_android.setBackgroundColor(btnEnableColor)
        })

        class PageChange : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {

                when (position) {
                    C4DROID -> {
                        Log.e("viewpage", "" + 0)
                        button_aide.setBackgroundColor(btnEnableColor)
                        button_c4droid.setBackgroundColor(btnUnableColor)
                        button_android.setBackgroundColor(btnEnableColor)

                    }
                    AIDE -> {
                        Log.e("viewpage", "" + 1)
                        button_aide.setBackgroundColor(btnUnableColor)
                        button_c4droid.setBackgroundColor(btnEnableColor)
                        button_android.setBackgroundColor(btnEnableColor)
                    }
                    ANDROID -> {
                        Log.e("viewpage", "" + 2)
                        button_aide.setBackgroundColor(btnEnableColor)
                        button_c4droid.setBackgroundColor(btnEnableColor)
                        button_android.setBackgroundColor(btnUnableColor)
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

}
