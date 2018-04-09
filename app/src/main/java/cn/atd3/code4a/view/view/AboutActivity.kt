package cn.atd3.code4a.view.view

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import cn.atd3.code4a.Constant
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.model.model.UpdateInfo
import cn.atd3.code4a.presenter.AboutPresenter
import cn.atd3.code4a.view.inter.AboutInterface
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.activity_about.*




class AboutActivity : AppCompatActivity(), AboutInterface {


    private lateinit var abp: AboutPresenter
    private var md: QMUITipDialog? = null
    private var i=0
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
                    abp.onCheakUpdate(this)
                }
                .addItemView(aboutList.createItemView(getString(R.string.setting_software_info))) { openWebActivity(Constant.softwareInfoUrl) }
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
        md = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.info_loading))
                .create()
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
            if(tipDialog!=null) {
                tipDialog.show()
                Thread(
                        Runnable {
                            try {
                                Thread.sleep(1500)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            } finally {
                                tipDialog!!.dismiss()
                            }
                        }
                ).start()
            }
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
        runOnUiThread {
            if (!md!!.isShowing)
                md!!.show()
        }
    }

    override fun dismissWaitDialog() {
        runOnUiThread {
            if (md!!.isShowing)
                md!!.dismiss()
        }
    }
    override fun showUpdateInfo(ui:UpdateInfo) {
        runOnUiThread {
            val a=QMUIDialog.MessageDialogBuilder(this)
                    .setTitle(ui.name)
                    .setMessage(ui.versionInfo)
                    .addAction("取消") { dialog, _ -> dialog.dismiss() }
                    .addAction(getString(R.string.download_update),{
                                                                dialog, _ ->
                                        dialog!!.dismiss()

                                         //创建下载任务,downloadUrl就是下载链接
                                        val request = DownloadManager.Request(Uri.parse(ui.download))
                                      // 设置Title
                                          request.setTitle(ui.version)
                                      // 设置描述
                                          request.setDescription(getString(R.string.info_down) + ui.version)
                        //默认只显示下载中通知
                                      request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                      //指定下载路径和下载文件名

                                      request.setDestinationInExternalPublicDir(Constant.downloadPath ,ui.version+".apk")
                                      //获取下载管理器
                                      val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                                       //将下载任务加入下载队列，否则不会进行下载
                            downloadManager.enqueue(request)
                            })
                    .create()

            a .show()
        }


    }
}

