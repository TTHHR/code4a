package cn.atd3.code4a.view.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import cn.atd3.code4a.Constant
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.AboutPresenter
import cn.atd3.code4a.view.inter.AboutInterface
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : Activity(), AboutInterface {
    private lateinit var abp: AboutPresenter
    private var i=0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)
        setContentView(R.layout.activity_about)
        topBar.addLeftBackImageButton().setOnClickListener( {
            finish()
        })
        topBar.setTitle(getString(R.string.setting_about))

        abp = AboutPresenter(this)


        QMUIGroupListView.newSection(this)
                .addItemView(aboutList.createItemView(getString(R.string.setting_update))) {
//        abp.onCheakUpdate()
                    val i = Intent(this, UpdateCheckActivity::class.java)
                    startActivity(i)
                }
                .addItemView(aboutList.createItemView(getString(R.string.setting_software_info))) { openWebActivity(Constant.softwareInfoUrl) }
                // TODO 添加如下选项
//                .addItemView(aboutList.createItemView(getString(R.string.setting_authors))){}
//                .addItemView(aboutList.createItemView(getString(R.string.setting_open_source))){}
                .addItemView(aboutList.createItemView(getString(R.string.setting_tieba_summary))) { openWebActivity(Constant.tiebaUrl) }
                .addItemView(aboutList.createItemView(getString(R.string.setting_auth_donation))) { openWebActivity(Constant.donationUrl) }
                .addTo(aboutList)
        logoImg.setOnClickListener {
            if (i >= 5) {
                startActivity(Intent(this, EggActivity::class.java))
                i = 0
            } else
                i++
        }
    }

    override fun onStart() {
        try {
            topBar.setBackgroundColor(Color.parseColor(Constant.themeColor))
        }
        catch (e:Exception)
        {
            topBar.setBackgroundColor(Color.parseColor(Constant.defaultThemeColor))
        }
        super.onStart()
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
                tipDialog.show()
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

