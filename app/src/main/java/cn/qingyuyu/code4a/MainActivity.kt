package cn.qingyuyu.code4a

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.content.Intent
import android.util.Log
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import cn.qingyuyu.code4a.control.DataBaseController
import cn.qingyuyu.code4a.control.LoginDealController
import cn.qingyuyu.code4a.model.ArticleList
import cn.qingyuyu.code4a.model.TabFragmentAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import cn.qingyuyu.code4a.model.User
import cn.qingyuyu.commom.SomeValue
import cn.qingyuyu.commom.SomeValue.*
import com.xyzlf.share.library.interfaces.ShareConstant
import com.xyzlf.share.library.util.ShareUtil
import com.xyzlf.share.library.bean.ShareEntity



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
lateinit var myViewPager:ViewPager
    //把Fragment添加到List集合里面
    var fragmentList = mutableListOf(ArticleFragment(C4DROID) as Fragment,ArticleFragment(AIDE)as Fragment,ArticleFragment(ANDROID)as Fragment)
   lateinit var button_aide:Button
    lateinit var button_android:Button
    lateinit var button_c4droid:Button
    var head_iv: ImageView? = null
    var uname: TextView? = null
   private  var btnUnableColor=0
    private var btnEnableColor=0
    private var eggTrigger=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        btnUnableColor=ContextCompat.getColor(this@MainActivity,R.color.btn_unable)
        btnEnableColor=ContextCompat.getColor(this@MainActivity,R.color.btn_enable)
        initView()
    }

    fun initView() {

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        myViewPager =  findViewById(R.id.myViewPager)
        val adapter = TabFragmentAdapter(supportFragmentManager, fragmentList)
        myViewPager.adapter = adapter
        myViewPager.currentItem = 0  //初始化显示第一个页面
        class PageChange : ViewPager.OnPageChangeListener
        {

            override fun onPageSelected(position: Int) {
                when (position)
                {
                    C4DROID-> {
                        Log.e("viewpage",""+0)
                        button_aide.setBackgroundColor(btnEnableColor)
                        button_c4droid.setBackgroundColor(btnUnableColor)
                        button_android.setBackgroundColor(btnEnableColor)

                    }
                    AIDE->{
                        Log.e("viewpage",""+1)
                        button_aide.setBackgroundColor(btnUnableColor)
                        button_c4droid.setBackgroundColor(btnEnableColor)
                        button_android.setBackgroundColor(btnEnableColor)
                    }
                    ANDROID->{
                        Log.e("viewpage",""+2)
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
        myViewPager.addOnPageChangeListener(PageChange())



        button_c4droid = findViewById(R.id.c4droid)
        button_aide =  findViewById(R.id.aide)
        button_android = findViewById(R.id.android)


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




        //获取头像点击事件
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val drawview = navigationView.inflateHeaderView(R.layout.nav_header_main)
        uname = drawview.findViewById(R.id.uname)
        head_iv = drawview.findViewById(R.id.headImage)
        head_iv!!.setOnClickListener {
            val ldc=LoginDealController()
            if(true)//登录判断User.getInstance().isLogind
            {
                eggTrigger+=1
                if (eggTrigger>=5)
                {
                    eggTrigger=0
                    val i = Intent(this@MainActivity, EggActivity::class.java)
                    startActivity(i)
                }
            }
            else
            ldc.call("login",this@MainActivity,null)
        }
        val newarticle = findViewById<FloatingActionButton>(R.id.newarticle) as FloatingActionButton
        newarticle.setOnClickListener(View.OnClickListener { view ->
            val i = Intent(this@MainActivity, EditArticleActivity::class.java)
            startActivity(i)
        })
    }


    override fun onResume() {
        super.onResume()
        if (User.getInstance().isLogind) {
            uname!!.text = User.getInstance().userName
            head_iv!!.setImageURI(User.getInstance().getimgUri())
        } else {
            uname!!.text = this.getText(R.string.username)
            head_iv!!.setImageResource(R.mipmap.logo)
        }
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
        when (item.itemId) {
            R.id.nav_tool -> {


            }
            R.id.nav_share -> {
                val testBean = ShareEntity(getString(R.string.app_name), getString(R.string.share_content))
                testBean.url = SomeValue.shareUrl //分享链接
                testBean.imgUrl = SomeValue.shareImg
                ShareUtil.showShareDialog(this, testBean, ShareConstant.REQUEST_CODE)

            }
            R.id.nav_feedback -> {
                startActivity(Intent(this@MainActivity, FeedbackActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onDestroy() {
        val data= DataBaseController()
        data.clearArticles(this)
        val al=ArticleList.getArticleList(this)
        if(!al.getArticleList(C4DROID)[0].title.equals("下拉刷新~(●'◡'●)"))
        data.saveArticles(this,al.getArticleList(C4DROID))
        if(!al.getArticleList(AIDE)[0].title.equals("下拉刷新~(●'◡'●)"))
        data.saveArticles(this,al.getArticleList(AIDE))
        if(!al.getArticleList(ANDROID)[0].title.equals("下拉刷新~(●'◡'●)"))
        data.saveArticles(this,al.getArticleList(ANDROID))
        super.onDestroy()
    }
}
