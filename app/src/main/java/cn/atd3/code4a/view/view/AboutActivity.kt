package cn.atd3.code4a.view.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.atd3.code4a.Constant
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.AboutPresenter
import cn.atd3.code4a.view.inter.AboutInterface
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView

class AboutActivity : Activity(), AboutInterface {
    private lateinit var abp: AboutPresenter
    private lateinit var aboutList: QMUIGroupListView
    private lateinit var topBar: QMUITopBarLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        topBar = findViewById(R.id.topbar)
        topBar.addLeftBackImageButton().setOnClickListener(View.OnClickListener {
            finish()
        })
        topBar.setTitle("关于")

        topBar.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))

        abp = AboutPresenter(this)

        aboutList = findViewById(R.id.about_list)

        QMUIGroupListView.newSection(this).addItemView(aboutList.createItemView("检查更新"), object : View.OnClickListener {
            override fun onClick(p0: View?) {
                abp.onCheakUpdate()
            }
        })
                .addItemView(aboutList.createItemView("软件信息"), object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        openWebActivity(Constant.softwareInfoUrl)
                    }
                })
                .addItemView(aboutList.createItemView("访问贴吧"), object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        openWebActivity(Constant.tiebaUrl)
                    }
                })
                .addItemView(aboutList.createItemView("捐赠"), object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        openWebActivity(Constant.donationUrl)
                    }
                })
                .addTo(aboutList)

    }

    override fun showToast(infotype: Int, info: String) {

        runOnUiThread {
            var tipDialog: QMUITipDialog? = null
            when (infotype) {
                SUCCESS -> tipDialog = QMUITipDialog.Builder(applicationContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(info)
                        .create()
                INFO -> tipDialog = QMUITipDialog.Builder(applicationContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(info)
                        .create()
                NORMAL -> tipDialog = QMUITipDialog.Builder(applicationContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
                WARNING -> tipDialog = QMUITipDialog.Builder(applicationContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(info)
                        .create()
                ERROR -> tipDialog = QMUITipDialog.Builder(applicationContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(info)
                        .create()
            }
            if (tipDialog != null)
                tipDialog!!.show()
        }

    }

    override fun getXmlString(resourceId: Int): String {
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

