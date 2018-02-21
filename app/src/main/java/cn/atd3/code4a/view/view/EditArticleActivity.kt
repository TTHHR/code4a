package cn.atd3.code4a.view.view

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import cn.atd3.code4a.Constant
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.library.fileselect.FileSelectActivity
import cn.atd3.code4a.model.model.ArticleModel
import cn.atd3.code4a.model.model.CategoryModel
import cn.atd3.code4a.model.model.FileListModel
import cn.atd3.code4a.presenter.EditArticlePresenter
import cn.atd3.code4a.util.UriRealPath
import cn.atd3.code4a.view.inter.EditArticleActivityInterface
import cn.carbs.android.library.MDDialog
import cn.dxkite.common.StorageData
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.scrat.app.richtext.RichEditText
import top.zibin.luban.Luban
import java.io.File

class EditArticleActivity : AppCompatActivity(), EditArticleActivityInterface {


    private val REQUEST_CODE_GET_CONTENT = 666


    private var message: TextView? = null
    private lateinit var richEditText: RichEditText
    private lateinit var eap: EditArticlePresenter
    private lateinit var md: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarticle)

        eap = EditArticlePresenter(this)

        initView()

    }


    fun initView() {
        richEditText = findViewById(R.id.rich_text)
        val i = intent
        val a = i.getSerializableExtra("article")
        if (a != null && a is ArticleModel)//说明不是新建文章，是编辑文章
        {
            eap.setArticle(a)
            richEditText.fromHtml(a.content)
        }
        addArticleVisibility()
        addArticleCategory()
        addArticleTitle()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {//创建菜单
        menuInflater.inflate(R.menu.activity_editarticle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.undo -> richEditText.undo()
            R.id.redo -> richEditText.redo()
            R.id.export -> {
                val inflater = layoutInflater
                val dialoglayout = inflater.inflate(R.layout.dialog_uploadarticle, null)
                message = dialoglayout.findViewById<TextView>(R.id.message)
                md = AlertDialog.Builder(this@EditArticleActivity)
                        .setTitle(R.string.title_upload_article)
                        .setView(dialoglayout)
                        .setCancelable(false)
                        .create()
                md.show()
                eap.setArticleContent(richEditText.toHtml())//设置内容
                eap.setArticleModifyTime()//设置修改时间

                eap.uploadArticle(this@EditArticleActivity)
            }
            R.id.addTitle ->
                addArticleTitle()
            R.id.addCategory ->
                addArticleCategory()
            R.id.addPower ->
                addArticleVisibility()
        }
        return true
    }


    override fun prgoressOfUpload(info: String) {
        runOnUiThread(
                Runnable {
                    if (message != null)
                        message!!.text = info
                }
        )
    }


    override fun dismissArticleInfoDialog() {
        runOnUiThread(
                Runnable {
                    if (md != null && md.isShowing)
                        md.dismiss()
                }
        )

    }

    override fun showToast(infotype: Int, info: String) {

        runOnUiThread {
            val tipDialog: QMUITipDialog
            when (infotype) {
                SUCCESS -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(info)
                        .create()
                INFO -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(info)
                        .create()
                NORMAL -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
                WARNING -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(info)
                        .create()
                ERROR -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(if (Constant.debugmodeinfo) info else getString(R.string.remote_error))
                        .create()
                else -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
            }
            tipDialog.show()
            Thread(
                    Runnable {
                        try {
                            Thread.sleep(1500)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } finally {
                            tipDialog.dismiss()
                        }
                    }
            ).start()
        }

    }

    override fun getXmlString(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || data.data == null)
            return
        val uri = data.data
        Log.e("image uri", "" + uri)
        Thread(Runnable {
            val f = Luban.with(this).setTargetDir(Constant.getPublicFilePath()).get(UriRealPath.getRealPathFromUri(this, uri))
            val u = Uri.fromFile(f)
            Log.e("zip image uri", u.toString())
            runOnUiThread {
                val width = richEditText.measuredWidth - richEditText.paddingLeft - richEditText.paddingRight
                richEditText.image(u, width)
            }
        }).start()

    }


    private fun addArticleTitle() {
        val builder = QMUIDialog.EditTextDialogBuilder(this)
        builder.setTitle(getString(R.string.input_title))
        if (eap.isEditModel)
            builder.setPlaceholder(eap.title)
        builder.setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(getString(R.string.button_cancel), object : QMUIDialogAction.ActionListener {
                    override fun onClick(dialog: QMUIDialog?, index: Int) {
                        dialog!!.dismiss()
                        if (eap.title == null || eap.title.isEmpty())
                            finish()
                    }

                })
                .addAction(getString(R.string.button_ok), object : QMUIDialogAction.ActionListener {
                    override fun onClick(dialog: QMUIDialog?, index: Int) {
                        val title = builder.editText.text
                        if (title.isEmpty())
                            Toast.makeText(this@EditArticleActivity, getString(R.string.error_title), Toast.LENGTH_SHORT).show()
                        else {
                            eap.setArticleTitle(title.toString())
                            dialog!!.dismiss()
                        }
                    }

                })
                .show()
    }

    private fun addArticleCategory() {

        val cateListFile = File(getCategoryListFilePath())

        val catelist: List<CategoryModel>
        if (cateListFile.exists()) {
            catelist = StorageData.loadObject(cateListFile) as List<CategoryModel>
        } else {
            Log.i("EditActivity", "load from network faild, load from assets!")
            catelist = StorageData.loadObject(resources.assets.open(Constant.categoryListFile)) as List<CategoryModel>
        }

        val items = ArrayList<String>()

        for (cate: CategoryModel in catelist) {
            items.add(cate.name)
        }

        val builder = QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(0)
                .addItems(items.toTypedArray(), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        eap.setArticleCategoryId(which + 1)
                        dialog!!.dismiss()
                    }

                })
        builder.show()
    }

    private fun addArticleVisibility() {
        val items = resources.getStringArray(R.array.power_list_key)

        val builder = QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(0)
                .addItems(items, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        eap.setArticleVisibility(items[0])
                        Toast.makeText(this@EditArticleActivity, getString(R.string.wanning_visibility), Toast.LENGTH_SHORT).show()
                        dialog!!.dismiss()
                        if (which == 2) {
                            val passBuilder = QMUIDialog.EditTextDialogBuilder(this@EditArticleActivity)
                            passBuilder.setInputType(InputType.TYPE_CLASS_TEXT)
                                    .addAction(getString(R.string.button_cancel), object : QMUIDialogAction.ActionListener {
                                        override fun onClick(dialog: QMUIDialog?, index: Int) {
                                            Toast.makeText(this@EditArticleActivity, getString(R.string.password_empty), Toast.LENGTH_SHORT).show()
                                        }

                                    })
                                    .addAction(getString(R.string.button_ok), object : QMUIDialogAction.ActionListener {
                                        override fun onClick(dialog: QMUIDialog?, index: Int) {
                                            val passwd = passBuilder.editText.text
                                            if (passwd.isEmpty())
                                                Toast.makeText(this@EditArticleActivity, getString(R.string.password_empty), Toast.LENGTH_SHORT).show()
                                            else {
                                                eap.setArticlePassword(passwd.toString())
                                                dialog!!.dismiss()
                                            }
                                        }

                                    })
                                    .show()
                        }
                    }

                })
        builder.show()
    }

    /**
     * 加粗
     */
    fun setBold(v: View) {
        richEditText.bold(!richEditText.contains(RichEditText.FORMAT_BOLD))
    }

    /**
     * 斜体
     */
    fun setItalic(v: View) {
        richEditText.italic(!richEditText.contains(RichEditText.FORMAT_ITALIC))
    }

    /**
     * 下划线
     */
    fun setUnderline(v: View) {
        richEditText.underline(!richEditText.contains(RichEditText.FORMAT_UNDERLINED))
    }

    /**
     * 删除线
     */
    fun setStrikethrough(v: View) {
        richEditText.strikethrough(!richEditText.contains(RichEditText.FORMAT_STRIKETHROUGH))
    }

    /**
     * 序号
     */
    fun setBullet(v: View) {
        richEditText.bullet(!richEditText.contains(RichEditText.FORMAT_BULLET))
    }

    /**
     * 引用块
     */
    fun setQuote(v: View) {
        richEditText.quote(!richEditText.contains(RichEditText.FORMAT_QUOTE))
    }

    /**
    插入图片
     */
    fun insertImg(v: View) {
        val getImage = Intent(Intent.ACTION_GET_CONTENT)
        getImage.addCategory(Intent.CATEGORY_OPENABLE)
        getImage.type = "image/*"
        startActivityForResult(getImage, REQUEST_CODE_GET_CONTENT)
    }

    /**
     * 添加附件
     */
    fun insertFile(v: View) {
        val files = FileListModel.getIns().toArray()
        MDDialog.Builder(this@EditArticleActivity)
                .setMessages(files)
                .setTitle(R.string.title_showfile)
                .setPositiveButton(R.string.button_add, View.OnClickListener {
                    val i = Intent(this@EditArticleActivity, FileSelectActivity::class.java)
                    startActivity(i)
                })
                .setOnItemClickListener(object : MDDialog.OnItemClickListener {
                    override fun onItemClicked(index: Int) {

                        FileListModel.getIns().removeFile(index)
                    }
                })
                .setWidthMaxDp(600)
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show()

    }

    /**
     * 清除格式
     */
    fun clearFormat(v: View) {
        richEditText.clearFormats()
    }
}
