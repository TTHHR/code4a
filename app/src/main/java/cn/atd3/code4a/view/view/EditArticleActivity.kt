package cn.atd3.code4a.view.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSpinner
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.library.fileselect.FileSelectActivity
import cn.atd3.code4a.model.model.FileListModel
import cn.atd3.code4a.presenter.EditArticlePresenter
import cn.atd3.code4a.view.inter.EditArticleActivityInterface
import cn.carbs.android.library.MDDialog
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.scrat.app.richtext.RichEditText
import es.dmoral.toasty.Toasty

class EditArticleActivity : AppCompatActivity() ,EditArticleActivityInterface {
    private val REQUEST_CODE_GET_CONTENT = 666
    private val SELECTFILE = 555
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 444



    private  var message:TextView?=null
    private lateinit var richEditText: RichEditText
    private lateinit var eap:EditArticlePresenter
    private lateinit var md:AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarticle)

        eap=EditArticlePresenter(this)

        initView()

    }



    fun initView()
    {
        richEditText = findViewById(R.id.rich_text)
        val i=intent
        if (i.getStringExtra("content") != null) {
            Log.e("content","内蓉为:"+i.getStringExtra("content"))
            richEditText.fromHtml(i.getStringExtra("content"))

            //如果有create表示为编辑文章而不是新建文章
            if(i.getIntExtra("create",-1)!=-1) {
                eap.setArticleCreateTime(i.getIntExtra("create", (System.currentTimeMillis() / 1000).toInt()))
            }
            if(i.getIntExtra("id",-1)!=-1) {
                eap.setArticleId(i.getIntExtra("id",-1))
            }

        }
        val inflater = layoutInflater
        val dialoglayout = inflater.inflate(R.layout.dialog_articleproperty, null)
        val titleEdit=dialoglayout.findViewById<BootstrapEditText>(R.id.title)
        val okButton=dialoglayout.findViewById<BootstrapButton>(R.id.okbutton)
        val kind=dialoglayout.findViewById<AppCompatSpinner>(R.id.classspinner)
        val power=dialoglayout.findViewById<AppCompatSpinner>(R.id.powerspinner)
        val passwordEdit=dialoglayout.findViewById<BootstrapEditText>(R.id.password)

         md= AlertDialog.Builder(this@EditArticleActivity)
                .setTitle(R.string.title_articleproperty)
                .setView(dialoglayout)
                .setCancelable(false)//不可跳过
                .setOnKeyListener(object : DialogInterface.OnKeyListener{
                    override fun onKey(p0: DialogInterface?, keyCode: Int, p2: KeyEvent?): Boolean {
                        if (keyCode == KeyEvent.KEYCODE_BACK)
                        {
                            this@EditArticleActivity.finish()
                            return true
                        }
                        return false
                    }
                })
                .create()

        kind.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                eap.setArticleCategory(p2+1)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        power.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2)
                {
                    0->eap.setArticleVisibility("public")
                    1->eap.setArticleVisibility("sign")
                    2->eap.setArticleVisibility("password")
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        okButton.setOnClickListener{
            eap.setArticleTitle(titleEdit.text.toString())

            eap.setArticlePassword(passwordEdit.text.toString())

            eap.checkArticleInfo()//检查文章属性

            }
        md .show()

        if(richEditText.text.isEmpty())
            richEditText.fromHtml("<blockquote>Android 端的富文本编辑器</blockquote>" +
                    "<ul><li>支持实时编辑</li><li>支持图片插入,加粗,斜体,下划线,删除线,列表,引用块,撤销与恢复等</li><li>使用<u>Glide</u>加载图片</li></ul>\n" +
                    "<img src=\"http://img5.duitang.com/uploads/item/201409/07/20140907195835_GUXNn.thumb.700_0.jpeg\">")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {//创建菜单
        menuInflater.inflate(R.menu.activity_editarticle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.undo -> richEditText.undo()
            R.id.redo -> richEditText.redo()
            R.id.export-> {
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

                eap.uploadArticle()
            }
        }
        return true
    }




    override fun prgoressOfUpload(info: String) {
        runOnUiThread(
                Runnable {
                    if(message!=null)
                        message!!.text=info
                }
        )
    }


    override fun dismissArticleInfoDialog() {
        runOnUiThread(
                Runnable {
                    if(md.isShowing)
                        md.dismiss()
                }
        )

    }
      override fun showToast(infotype:Int, info:String) {
runOnUiThread {
    when (infotype) {
        SUCESS -> Toasty.success(applicationContext, info, Toast.LENGTH_LONG).show()
        INFO -> Toasty.info(applicationContext, info, Toast.LENGTH_LONG).show()
        NORMAL -> Toasty.normal(applicationContext, info, Toast.LENGTH_LONG).show()
        WARNING -> Toasty.warning(applicationContext, info, Toast.LENGTH_LONG).show()
        ERROR -> Toasty.error(applicationContext, info, Toast.LENGTH_LONG).show()
    }
}

      }

    override fun getXmlString(resourceId:Int):String {
return getString(resourceId)
}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE||data==null|| data.data==null)
            return
        val uri = data.data
        val width = richEditText.measuredWidth - richEditText.paddingLeft - richEditText.paddingRight
        richEditText.image(uri, width)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
        }

        val getImage = Intent(Intent.ACTION_GET_CONTENT)
        getImage.addCategory(Intent.CATEGORY_OPENABLE)
        getImage.type = "image/*"
        startActivityForResult(getImage, REQUEST_CODE_GET_CONTENT)
    }

    /**
     * 添加附件
     */
    fun insertFile(v: View) {
        val files =FileListModel.getIns().toArray()
        MDDialog.Builder(this@EditArticleActivity)
                .setMessages(files)
                .setTitle(R.string.title_showfile)
                .setPositiveButton(R.string.button_add,View.OnClickListener {
                    Toasty.info(this@EditArticleActivity, getString(R.string.select_file_info) , Toast.LENGTH_LONG, true).show()
                    val i=Intent(this@EditArticleActivity, FileSelectActivity::class.java)
                    startActivity(i)
                })
                .setOnItemClickListener(object:MDDialog.OnItemClickListener {
                    override fun onItemClicked(index: Int) {
                        Toasty.warning(this@EditArticleActivity, getString(R.string.click_file_remove)+FileListModel.getIns().get(index) , Toast.LENGTH_SHORT, true).show()
                        FileListModel.getIns().removeFile(index)
                    }
                } )
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
