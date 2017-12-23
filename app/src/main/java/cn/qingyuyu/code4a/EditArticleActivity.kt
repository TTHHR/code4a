package cn.qingyuyu.code4a

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import cn.qingyuyu.code4a.library.fileselect.FileList
import cn.qingyuyu.code4a.library.fileselect.FileSelectActivity
import com.scrat.app.richtext.RichEditText
import es.dmoral.toasty.Toasty
import android.widget.TextView
import cn.atd3.proxy.Param
import cn.carbs.android.library.MDDialog
import cn.qingyuyu.code4a.remote.Remote
import java.io.File


/**
 * Created by harrytit on 2017/10/13.
 */
class EditArticleActivity : AppCompatActivity() {
    private val REQUEST_CODE_GET_CONTENT = 666
    private val SELECTFILE = 555
    private val SELECTFILE_CANCLE = 0
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 444
    private var richEditText: RichEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarticle)
        initView()
    }
    fun initView()
    {
        richEditText = findViewById<RichEditText>(R.id.rich_text)
        var i=intent
        if (i.getStringExtra("html") == null) {
            val inflater = layoutInflater
            val dialoglayout = inflater.inflate(R.layout.dialog_articleproperty, null)

            val classSpinner= dialoglayout.findViewById<Spinner>(R.id.classspinner)

            val md=MDDialog.Builder(this@EditArticleActivity)
                    .setTitle(R.string.title_articleproperty)
                    .setContentView(dialoglayout)
                    .setWidthMaxDp(600)
                    .setShowTitle(true)
                    .setShowButtons(true)
                    .setCancelable(true)
                    .create()
            md .show()
            if(richEditText!=null)
            richEditText!!.fromHtml("<blockquote>Android 端的富文本编辑器</blockquote>" +
                    "<ul><li>支持实时编辑</li><li>支持图片插入,加粗,斜体,下划线,删除线,列表,引用块,撤销与恢复等</li><li>使用<u>Glide</u>加载图片</li></ul>\n" +
                    "<img src=\"http://img5.duitang.com/uploads/item/201409/07/20140907195835_GUXNn.thumb.700_0.jpeg\">" +
                    "<img src=\"http://blog.qingyuyu.cn/storage/a5124910.jpg\">")
        }
        else
        {
            richEditText!!.fromHtml(i.getStringExtra("html"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_editarticle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.undo -> richEditText!!.undo()
            R.id.redo -> richEditText!!.redo()
            else -> {
               val inflater = layoutInflater
                val dialoglayout = inflater.inflate(R.layout.dialog_uploadarticle, null)
                val message= dialoglayout.findViewById<TextView>(R.id.message)
                val md=MDDialog.Builder(this@EditArticleActivity)
                        .setTitle(R.string.title_article)
                        .setContentView(dialoglayout)
                        .setWidthMaxDp(600)
                        .setShowTitle(true)
                        .setShowButtons(false)
                        .setCancelable(true)
                        .create()
                md .show()
                val hd=object:Handler(){
                    override fun handleMessage(msg: Message) {
                        val s = msg.what
                        Log.e("what",""+s)
                        when(s)
                        {
                            1-> message.text=getString(R.string.action_packfile)
                            2-> message.text=getString(R.string.title_upfile)
                            3->message.text=getString(R.string.action_upfinish)
                            4->{
                                Log.e("html",richEditText!!.toHtml())
                                md.cancel()
                            }
                        }

                            super.handleMessage(msg)
                    }
                }

                Thread(Runnable {
                    var message = Message()
                    message.what=1
                    hd.sendMessage(message)
                    Thread.sleep(1000)
                    message = Message()
                    message.what=2
                    hd.sendMessage(message)
                    Thread.sleep(1000)

                    // 0 - delete 1 - crash 2 public
                    Remote.article.method("upload").call(
                            Param("article",File("path/to/file")),
                            Param("type","xml"),
                            Param("status",2)
                    );

                    message = Message()
                    message.what=3
                    hd.sendMessage(message)
                    Thread.sleep(1000)
                    message = Message()
                    message.what=4
                    hd.sendMessage(message)
                }).start()



            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE||data==null|| data.data==null)
            return
        val uri = data.data
        val width = richEditText!!.measuredWidth - richEditText!!.paddingLeft - richEditText!!.paddingRight
        richEditText!!.image(uri, width)
    }

    /**
     * 加粗
     */
    fun setBold(v: View) {
        richEditText!!.bold(!richEditText!!.contains(RichEditText.FORMAT_BOLD))
    }

    /**
     * 斜体
     */
    fun setItalic(v: View) {
        richEditText!!.italic(!richEditText!!.contains(RichEditText.FORMAT_ITALIC))
    }

    /**
     * 下划线
     */
    fun setUnderline(v: View) {
        richEditText!!.underline(!richEditText!!.contains(RichEditText.FORMAT_UNDERLINED))
    }

    /**
     * 删除线
     */
    fun setStrikethrough(v: View) {
        richEditText!!.strikethrough(!richEditText!!.contains(RichEditText.FORMAT_STRIKETHROUGH))
    }

    /**
     * 序号
     */
    fun setBullet(v: View) {
        richEditText!!.bullet(!richEditText!!.contains(RichEditText.FORMAT_BULLET))
    }

    /**
     * 引用块
     */
    fun setQuote(v: View) {
        richEditText!!.quote(!richEditText!!.contains(RichEditText.FORMAT_QUOTE))
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
        val files = FileList.fileList.toArray(arrayOfNulls<String>(FileList.fileList.size))
        MDDialog.Builder(this@EditArticleActivity)
                .setMessages(files)
                .setTitle(R.string.title_showfile)
                .setNegativeButton(R.string.button_add,View.OnClickListener {
                    Toasty.info(this@EditArticleActivity, getString(R.string.select_file_info) , Toast.LENGTH_LONG, true).show()
                    var i=Intent(this@EditArticleActivity,FileSelectActivity::class.java)
                    startActivityForResult(i,SELECTFILE)
                })
                .setPositiveButton(R.string.button_ok,View.OnClickListener { Toasty.success(this@EditArticleActivity, getString(R.string.button_ok ), Toast.LENGTH_SHORT, true).show() })
                .setOnItemClickListener(object:MDDialog.OnItemClickListener {
                    override fun onItemClicked(index: Int) {
                        Toasty.warning(this@EditArticleActivity, getString(R.string.click_file_remove)+FileList.fileList.get(index) , Toast.LENGTH_SHORT, true).show()
                        FileList.fileList.removeAt(index)
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
        richEditText!!.clearFormats()
    }
}