package cn.atd3.code4a.view.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.atd3.code4a.Constant
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.AboutPresenter
import cn.atd3.code4a.view.inter.AboutInterface
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import es.dmoral.toasty.Toasty

class AboutActivity : Activity(),AboutInterface {
private lateinit var abp:AboutPresenter
    private lateinit var aboutList:QMUIGroupListView
    private lateinit var topBar:QMUITopBarLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        topBar=findViewById(R.id.topbar)
        topBar.addLeftImageButton(R.mipmap.top_back,1).setOnClickListener(View.OnClickListener {
            finish()
        })
        topBar.setTitle("关于")

        abp= AboutPresenter(this)

        aboutList=findViewById(R.id.about_list)

        QMUIGroupListView.newSection(this).addItemView(aboutList.createItemView("检查更新"),object: View.OnClickListener{
            override fun onClick(p0: View?) {
               abp.onCheakUpdate()
            }
        })
                .addItemView(aboutList.createItemView("软件信息"),object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        openWebActivity(Constant.softwareInfoUrl)
                    }
                })
                .addItemView(aboutList.createItemView("访问贴吧"),object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        openWebActivity(Constant.tiebaUrl)
                    }
                })
                .addItemView(aboutList.createItemView("捐赠"),object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        openWebActivity(Constant.donationUrl)
                    }
                })
                .addTo(aboutList)

    }
     override fun showToast(infotype:Int, info:String) {
runOnUiThread {
    when (infotype) {
        SUCCESS -> Toasty.success(applicationContext, info, Toast.LENGTH_SHORT).show()
        INFO -> Toasty.info(applicationContext, info, Toast.LENGTH_SHORT).show()
        NORMAL -> Toasty.normal(applicationContext, info, Toast.LENGTH_SHORT).show()
        WARNING -> Toasty.warning(applicationContext, info, Toast.LENGTH_SHORT).show()
        ERROR -> Toasty.error(applicationContext, if (Constant.debugmodeinfo) info else getString(R.string.remote_error), Toast.LENGTH_SHORT).show()
    }
}

     }

    override fun getXmlString(resourceId:Int):String {
return getString(resourceId)
}
    override fun openWebActivity(url: String) {
        val i = Intent(this, WebActivity::class.java)
        i.putExtra("url", url)
        startActivity(i)
    }

    override fun showWaitDialog() {

    }

    override fun dismissWaitDialog() {

    }

}

