package cn.atd3.code4a.view.view

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

import cn.atd3.code4a.Constant
import cn.atd3.code4a.R
import cn.atd3.code4a.model.model.ArticleModel
import cn.atd3.code4a.presenter.ViewArticlePresenter
import cn.atd3.code4a.view.inter.ArticleViewInterface

import cn.atd3.code4a.Constant.ERROR
import cn.atd3.code4a.Constant.INFO
import cn.atd3.code4a.Constant.NORMAL
import cn.atd3.code4a.Constant.SUCCESS
import cn.atd3.code4a.Constant.WARNING
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.activity_view_article.*

class ViewArticleActivity : AppCompatActivity(), ArticleViewInterface {

    private var vap: ViewArticlePresenter? = null

    private var md: QMUITipDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)//沉浸式状态栏
        setContentView(R.layout.activity_view_article)
        val i = this.intent
        val article = i.getSerializableExtra("article") as ArticleModel


        // 固定横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        md = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.info_loading))
                .create()

        vap = ViewArticlePresenter(this, this, article)//控制器


        vap!!.shouWaitDialog()//等待


        vap!!.checkArticle()//检查数据是否正常


        topBar!!.addLeftBackImageButton().setOnClickListener { finish() }

        topBar!!.setTitle(if (article.title == null) "error" else article.title)

        // 菜单按钮
        topBar!!.addRightImageButton(R.mipmap.topbar_menu, 1).setOnClickListener { showBottomSheetList() }


        if (richText != null) {
            vap!!.initImageGetter(richText)//初始化图片加载器
        }


        copyButton!!.setOnClickListener {
            if (richText != null) {
                try {
                    val cm = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.primaryClip = ClipData.newPlainText("code", richText!!.text)
                    showToast(SUCCESS,getString(R.string.copy_success))
                }
                catch (e:Exception)
                {
                    showToast(WARNING,getString(R.string.copy_fail)+e)
                }

            }
        }

    }

    override fun onBackPressed() {

        vap!!.onDestory(this)
        //获得文章详情保存到本地

        //数据是使用Intent返回
        val intent = Intent()
        //设置返回数据
        this.setResult(if (vap!!.deleteArticle) 1 else 0, intent)
        //关闭Activity
        this.finish()

    }


    override fun onStart() {
        try {
            topBar!!.setBackgroundColor(Color.parseColor(Constant.themeColor))
        }
        catch (e:Exception)
        {
            showToast(WARNING,getString(R.string.waring_error_color))
            topBar!!.setBackgroundColor(Color.parseColor(Constant.defaultThemeColor))
        }
        vap!!.loadArticle()//加载文章

        super.onStart()
    }


    override fun showToast(infotype: Int, info: String) {

        runOnUiThread {
            val tipDialog: QMUITipDialog
            when (infotype) {
                SUCCESS -> tipDialog = QMUITipDialog.Builder(this@ViewArticleActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(info)
                        .create()
                INFO -> tipDialog = QMUITipDialog.Builder(this@ViewArticleActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(info)
                        .create()
                NORMAL -> tipDialog = QMUITipDialog.Builder(this@ViewArticleActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
                WARNING -> tipDialog = QMUITipDialog.Builder(this@ViewArticleActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(info)
                        .create()
                ERROR -> tipDialog = QMUITipDialog.Builder(this@ViewArticleActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(if (Constant.debugmodeinfo) info else getString(R.string.remote_error))
                        .create()
                else -> tipDialog = QMUITipDialog.Builder(this@ViewArticleActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
            }
            tipDialog.show()
            Thread {
                        try {
                            Thread.sleep(1500)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } finally {
                            tipDialog.dismiss()
                        }
                    }.start()
        }

    }

    override fun getXmlString(resourceId: Int): String {
        return getString(resourceId)
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

    override fun loadArticle(text: String?, imageGetter: Html.ImageGetter) {

        if(text!=null)
        runOnUiThread {
            richText!!.text = Html.fromHtml(text, imageGetter, null)
            copyButton!!.isClickable = true
        }
        else
            Log.e("view article","text is null")
    }

    override fun loadUser(un: String) {
        runOnUiThread { topBar!!.setSubTitle(un) }
    }

    private fun showBottomSheetList() {
        QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(getString(R.string.nav_share))
                .addItem(getString(R.string.button_edit))
                .addItem(getString(R.string.download_file))
                .addItem(getString(R.string.button_del))
                .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                    dialog.dismiss()
                    when (position) {
                        0 -> {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Share")
                            intent.putExtra(Intent.EXTRA_TEXT, richText!!.text.toString() + Constant.shareUrl)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(Intent.createChooser(intent, title))
                        }
                        1 -> {
                            //编辑文章
                            //需添加权限判断
                            val i = Intent(this@ViewArticleActivity, EditArticleActivity::class.java)
                            i.putExtra("article", vap!!.article)
                            startActivity(i)
                        }
                        2 -> {
                            val builder = QMUIDialog.MultiCheckableDialogBuilder(this@ViewArticleActivity)
                                    .addItems(vap!!.downFileList) { dialog, which -> }
                            builder.addAction(getString(R.string.button_cancel)) { dialog, index -> dialog.dismiss() }
                            builder.addAction(getString(R.string.button_ok)) { dialog, index ->
                                for (i in 0 until builder.checkedItemIndexes.size) {
                                    //创建下载任务,downloadUrl就是下载链接
                                    val request = DownloadManager.Request(Uri.parse(vap!!.getFileUrl(i)))
                                    // 设置Title
                                    request.setTitle(vap!!.downFileList[i])
                                    // 设置描述
                                    request.setDescription(getString(R.string.info_down) + vap!!.downFileList[i])
                                    //默认只显示下载中通知
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    //指定下载路径和下载文件名

                                    request.setDestinationInExternalPublicDir(Constant.downloadPath + "/" + vap!!.articleid + "/", vap!!.downFileList[i])
                                    Log.e("down path", Constant.downloadPath + "/" + vap!!.articleid + "/")
                                    Log.e("file", vap!!.downFileList[i])
                                    //获取下载管理器
                                    val downloadManager = this@ViewArticleActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                                    //将下载任务加入下载队列，否则不会进行下载
                                    downloadManager.enqueue(request)
                                }

                                dialog.dismiss()
                            }
                            builder.show()
                        }

                        3 ->
                            //删除文章
                            vap!!.deleteArticle()
                    }
                }
                .build()
                .show()
    }
}
