package cn.atd3.code4a.view.view

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSpinner
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import cn.atd3.code4a.Constant
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.library.fileselect.FileSelectActivity
import cn.atd3.code4a.model.model.CategoryModel
import cn.atd3.code4a.model.model.FileListModel
import cn.atd3.code4a.presenter.EditArticlePresenter
import cn.atd3.code4a.util.UriRealPath
import cn.atd3.code4a.view.inter.EditArticleActivityInterface
import cn.carbs.android.library.MDDialog
import cn.dxkite.common.StorageData
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
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
        if (i.getStringExtra("content") != null) {
            Log.e("content", "内蓉为:" + i.getStringExtra("content"))
            richEditText.fromHtml(i.getStringExtra("content"))

            //如果有create表示为编辑文章而不是新建文章
            if (i.getIntExtra("create", -1) != -1) {
                eap.setArticleCreateTime(i.getIntExtra("create", (System.currentTimeMillis() / 1000).toInt()))
            }
            if (i.getIntExtra("id", -1) != -1) {
                eap.setArticleId(i.getIntExtra("id", -1))
            }

        }
        val inflater = layoutInflater
        val dialoglayout = inflater.inflate(R.layout.dialog_articleproperty, null)
        val titleEdit = dialoglayout.findViewById<BootstrapEditText>(R.id.title)
        val okButton = dialoglayout.findViewById<BootstrapButton>(R.id.okbutton)
        val kind = dialoglayout.findViewById<AppCompatSpinner>(R.id.classspinner)
        val power = dialoglayout.findViewById<AppCompatSpinner>(R.id.powerspinner)
        val passwordEdit = dialoglayout.findViewById<BootstrapEditText>(R.id.password)

        md = AlertDialog.Builder(this@EditArticleActivity)
                .setTitle(R.string.title_articleproperty)
                .setView(dialoglayout)
                .setCancelable(false)//不可跳过
                .setOnKeyListener(object : DialogInterface.OnKeyListener {
                    override fun onKey(p0: DialogInterface?, keyCode: Int, p2: KeyEvent?): Boolean {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            this@EditArticleActivity.finish()
                            return true
                        }
                        return false
                    }
                })
                .create()


        val cateListFile = File(getCategoryListFilePath())

        val catelist: List<CategoryModel>
        if (cateListFile.exists()) {
            catelist = StorageData.loadObject(cateListFile) as List<CategoryModel>
        } else {
            Log.i("EditActivity", "load from network faild, load from assets!")
            catelist = StorageData.loadObject(resources.assets.open(Constant.categoryListFile)) as List<CategoryModel>
        }

        val spinnerstring = ArrayList<String>()

        for (cate: CategoryModel in catelist) {
            spinnerstring.add(cate.name)
        }

        val sad = ArrayAdapter(this@EditArticleActivity, android.R.layout.simple_list_item_1, spinnerstring)

        kind.adapter = sad

        kind.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.e("select kind", "" + p2 + 1)
                eap.setArticleCategory(p2 + 1)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        power.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> eap.setArticleVisibility("public")
                    1 -> eap.setArticleVisibility("sign")
                    2 -> eap.setArticleVisibility("password")
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        okButton.setOnClickListener {
            eap.setArticleTitle(titleEdit.text.toString())

            eap.setArticlePassword(passwordEdit.text.toString())

            eap.checkArticleInfo()//检查文章属性

        }
        md.show()

        if (richEditText.text.isEmpty())
            richEditText.fromHtml("<blockquote>Android 端的富文本编辑器</blockquote>" +
                    "<ul><li>支持实时编辑</li><li>支持图片插入,加粗,斜体,下划线,删除线,列表,引用块,撤销与恢复等</li><li>使用<u>Glide</u>加载图片</li></ul>\n")

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
                        .setTitle(R.string.title_article)
                        .setView(dialoglayout)
                        .setCancelable(false)
                        .create()
                md.show()
                eap.setArticleContent(richEditText.toHtml())//设置内容
                eap.setArticleModifyTime()//设置修改时间

                eap.uploadArticle(this@EditArticleActivity)
            }
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
                    if (md.isShowing)
                        md.dismiss()
                }
        )

    }

      override fun showToast(infotype:Int, info:String) {

runOnUiThread {
    val tipDialog:QMUITipDialog
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
                } catch (e:InterruptedException) {
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
