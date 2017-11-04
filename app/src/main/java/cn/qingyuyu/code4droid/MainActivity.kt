package cn.qingyuyu.code4droid

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hitomi.refresh.view.FunGameRefreshView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import cn.dxkite.baidusign.SignActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.baidu.api.BaiduDialogError
import com.baidu.api.BaiduException
import com.baidu.api.BaiduDialog.BaiduDialogListener
import com.baidu.api.Baidu
import com.baidu.api.Util
import com.baidu.api.AsyncBaiduRunner.RequestListener
import java.io.IOException
import com.baidu.api.AsyncBaiduRunner
import cn.qingyuyu.code4droid.model.User
import com.xyzlf.share.library.interfaces.ShareConstant
import com.xyzlf.share.library.util.ShareUtil
import com.xyzlf.share.library.bean.ShareEntity
import es.dmoral.toasty.Toasty


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var baidu: Baidu? = null
    final  var TAG="MainActivity";

    val url = Baidu.LoggedInUser_URL
    //是否每次授权都强制登陆
    private val isForceLogin = false
    private val isConfirmLogin = true
    var head_iv: ImageView? = null
    var uname: TextView? = null
    var listView: ListView? = null
    var refreshView: FunGameRefreshView? = null
    private var isPermissionRequested = false//权限
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initView()
        requestPermission()//请求权限
    }


    fun initView() {

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        //获取头像点击事件
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val drawview = navigationView.inflateHeaderView(R.layout.nav_header_main)
        uname = drawview.findViewById(R.id.uname)
        head_iv = drawview.findViewById(R.id.headImage)
        head_iv!!.setOnClickListener {
            /*
            baidu = Baidu(SomeValue.clientId, this@MainActivity)
            if (User.instance.isLogind) {
                return@setOnClickListener
            }

            baidu!!.authorize(this@MainActivity, null, isForceLogin, isConfirmLogin, object : BaiduDialogListener {

                override fun onComplete(values: Bundle) {
                    val runner = AsyncBaiduRunner(baidu)
                    runner.request(url, null, "POST", DefaultRequstListener())
                }

                override fun onBaiduException(e: BaiduException) {
                    Util.logd("error", "$e")
                }

                override fun onError(e: BaiduDialogError) {
                    Util.logd("error", "$e")
                }

                override fun onCancel() {
                    Util.logd("cancle", "I am back")
                }
            })*/
            startActivity(Intent(this@MainActivity,SignActivity::class.java))
        }
        val newarticle = findViewById<FloatingActionButton>(R.id.newarticle) as FloatingActionButton
        newarticle.setOnClickListener(View.OnClickListener { view ->
            val i = Intent(this@MainActivity, EditArticleActivity::class.java)
            startActivity(i)
        })
    }

    @SuppressLint("NewApi")
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true

            val permissions = ArrayList<String>()
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (permissions.size === 0) {
                return
            } else {
                requestPermissions(permissions.toArray(arrayOfNulls<String>(permissions.size)), 0)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (User.instance.isLogind) {
            uname!!.text = User.instance.getUserName()
            head_iv!!.setImageURI(User.instance.imgUri)
        } else {
            uname!!.text = this.getText(R.string.username)
            head_iv!!.setImageResource(R.mipmap.ic_launcher)
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
               //
                Log.i(TAG,"nav tool is clicked!");
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

    //百度登陆Listener
    inner class DefaultRequstListener : RequestListener {

        override fun onBaiduException(arg0: BaiduException) {
        }

        override fun onComplete(value: String) {
            User.getInstance(value)
            Toasty.success(this@MainActivity, "登陆成功", Toast.LENGTH_SHORT, true).show()
        }

        override fun onIOException(arg0: IOException) {
        }

    }
}
