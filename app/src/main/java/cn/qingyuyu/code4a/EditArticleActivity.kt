package cn.qingyuyu.code4a

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSpinner
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import cn.qingyuyu.code4a.library.fileselect.FileList
import cn.qingyuyu.code4a.library.fileselect.FileSelectActivity
import com.scrat.app.richtext.RichEditText
import es.dmoral.toasty.Toasty
import android.widget.TextView
import cn.atd3.proxy.Param
import cn.carbs.android.library.MDDialog
import cn.qingyuyu.code4a.remote.Remote
import cn.qingyuyu.code4a.remote.bean.Article
import cn.qingyuyu.commom.SomeValue
import cn.qingyuyu.commom.service.FileDealService
import cn.qingyuyu.zip.DirTraversal
import cn.qingyuyu.zip.ZipHelper
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


/**
 * Created by harrytit on 2017/10/13.
 */
class EditArticleActivity : AppCompatActivity() {
    private val REQUEST_CODE_GET_CONTENT = 666
    private val SELECTFILE = 555
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 444
    private var richEditText: RichEditText? = null
    private val article=Article()
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
            val titleEdit=dialoglayout.findViewById<BootstrapEditText>(R.id.title)
            val okButton=dialoglayout.findViewById<BootstrapButton>(R.id.okbutton)
            val kind=dialoglayout.findViewById<AppCompatSpinner>(R.id.classspinner)
            val power=dialoglayout.findViewById<AppCompatSpinner>(R.id.powerspinner)
            val passwordEdit=dialoglayout.findViewById<BootstrapEditText>(R.id.password)

            val md= AlertDialog.Builder(this@EditArticleActivity)
                    .setTitle(R.string.title_articleproperty)
                    .setView(dialoglayout)
                    .setCancelable(false)//不可跳过
                    .setOnKeyListener(object :DialogInterface.OnKeyListener{
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

            kind.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    article.category=p2+1
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            power.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when(p2)
                    {
                        0->article.visibility="public"
                        1->article.visibility="sign"
                        2->article.visibility="password"
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            okButton.setOnClickListener{
                article.title=titleEdit.text.toString()
                article.slug=article.title
                if(article.title.isEmpty())
                    Toasty.error(this,getString(R.string.error_title),Toast.LENGTH_SHORT).show()
                else {
                    if(article.category==null)//默认分类
                    article.category=1

                    article.create=(System.currentTimeMillis()/1000).toInt()//创建时间

                    if(article.visibility==null)
                        article.visibility="public"

                    if(passwordEdit.text.toString().isEmpty())//设置密码
                        article.visibilitypassword="password"//默认密码
                    else
                        article.visibilitypassword=passwordEdit.text.toString()

                    article.status=2//默认文章为发布状态
                    md.dismiss()
                }
            }
            md .show()
            if(richEditText!=null)
            richEditText!!.fromHtml("<blockquote>Android 端的富文本编辑器</blockquote>" +
                    "<ul><li>支持实时编辑</li><li>支持图片插入,加粗,斜体,下划线,删除线,列表,引用块,撤销与恢复等</li><li>使用<u>Glide</u>加载图片</li></ul>\n" +
                    "<img src=\"http://img5.duitang.com/uploads/item/201409/07/20140907195835_GUXNn.thumb.700_0.jpeg\">")
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
                if(article.title==null||article.category==null){//检查一下
                   Toasty.error(this,"title or category  can not be null",Toast.LENGTH_SHORT).show()
                    return true
                }
                if(richEditText!!.text.toString().isEmpty())
                {
                    Toasty.error(this,"article can not be null",Toast.LENGTH_SHORT).show()
                    return true
                }



               val inflater = layoutInflater
                val dialoglayout = inflater.inflate(R.layout.dialog_uploadarticle, null)
                val message= dialoglayout.findViewById<TextView>(R.id.message)
                val md=AlertDialog.Builder(this@EditArticleActivity)
                        .setTitle(R.string.title_article)
                        .setView(dialoglayout)
                        .setCancelable(false)
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
                            else->{
                                Toasty.error(this@EditArticleActivity,"error",Toast.LENGTH_SHORT).show()
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
                    //打包文件
                    article.content=richEditText!!.toHtml()

                    Log.e("rich tohtml",richEditText!!.toHtml())

                if(richEditText!!.text.toString().length>40)
                    article.abstract=richEditText!!.text.toString().substring(0,40)
                else
                    article.abstract=richEditText!!.text.toString()

                article.modify=(System.currentTimeMillis()/1000).toInt()//添加修改时间




                val xmlFile=articleToXml()

                    if(xmlFile==false)
                    {
                        message = Message()
                        message.what=5
                        hd.sendMessage(message)
                    }
                    else {
                        val zipFile=packageFile()
                        if(zipFile==false)
                        {
                            message = Message()
                            message.what=5
                            hd.sendMessage(message)
                        }
                        else {
                            message = Message()
                            message.what = 2
                            hd.sendMessage(message)
                            //上传
                            try {
                                Remote.article.method("upload").call(
                                        Param("article", File(Environment.getExternalStorageDirectory().toString() + SomeValue.userDir + SomeValue.zipDir + SomeValue.zipFile)),
                                        Param("type", "xml"),
                                        Param("status", 2)
                                )
                               message = Message()
                                message.what = 3
                                hd.sendMessage(message)
                                Thread.sleep(1000)
                                message = Message()
                                message.what = 4
                                hd.sendMessage(message)

                            }
                            catch (e:Exception)
                            {
                                Log.e("upload",""+e)
                                message = Message()
                                message.what=5
                                hd.sendMessage(message)
                            }

                        }

                    }
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
    fun articleToXml():Boolean?
    {
        var userDir = File(Environment.getExternalStorageDirectory().toString() + SomeValue.userDir)
        val zipDir=File(userDir.absolutePath+SomeValue.zipDir)
        val xmlFile = File(userDir.absolutePath +SomeValue.zipDir+ "/index.xml")
        if (!userDir.exists())
            try {
                userDir.mkdir()
                File(userDir.absolutePath+SomeValue.zipDir).mkdir()
            } catch (e: Exception) {
                Toasty.warning(this, getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
                return false
            }
        if (!zipDir.exists())
            try {
                zipDir.mkdir()

            } catch (e: Exception) {
                Toasty.warning(this, getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
                return false
            }
        if (!xmlFile.exists())
            try {
                xmlFile.createNewFile()
            } catch (e: Exception) {
                Toasty.warning(this, getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
                return false
            }

        try {
            val dbf = DocumentBuilderFactory.newInstance()
            val db = dbf.newDocumentBuilder()
            val document = db.newDocument()
            val base64 = sun.misc.BASE64Encoder()
            document.setXmlStandalone(true)
            val articles = document.createElement("article")
            val attrs = document.createElement("attrs")
            val title = document.createElement("attr")
            val slug = document.createElement("attr")
            val category = document.createElement("attr")
            val tag = document.createElement("attr")
            val create = document.createElement("attr")
            val modify = document.createElement("attr")
            val visibility = document.createElement("attr")
            val abstracts = document.createElement("attr")
            val status = document.createElement("attr")
            val cover = document.createElement("attr")

            title.setAttribute("name", "title")
            title.setTextContent(base64.encode(article.title.toByteArray()))
            Log.e("eeeee",""+base64.encode(article.title.toByteArray()))
            slug.setAttribute("name", "slug")
            slug.setTextContent(base64.encode(article.slug.toByteArray()))
            category.setAttribute("name", "category")
            category.setAttribute("valve", ""+article.category)
            tag.setAttribute("name", "tag")
            create.setAttribute("name", "create")
            create.setAttribute("value", ""+article.create)
            modify.setAttribute("name", "modify")
            modify.setAttribute("value", ""+article.modify)
            visibility.setAttribute("name", "visibility")
            visibility.setAttribute("value", article.visibility)
            visibility.setAttribute("password", article.visibilitypassword)
            abstracts.setAttribute("name", "abstract")
            abstracts.setTextContent(base64.encode(article.abstract.toByteArray()))
            status.setAttribute("name", "status")
            status.setAttribute("value", "2")
            cover.setAttribute("name", "cover")
            cover.setAttribute("value", "assets/cover.png")
            attrs.appendChild(title)
            attrs.appendChild(slug)
            attrs.appendChild(category)
            attrs.appendChild(tag)
            attrs.appendChild(create)
            attrs.appendChild(modify)
            attrs.appendChild(visibility)
            attrs.appendChild(abstracts)
            attrs.appendChild(status)
            attrs.appendChild(cover)

            articles.appendChild(attrs)

            val content = document.createElement("content")
            content.setAttribute("type", "html")
            content.setTextContent(base64.encode(article.content.toByteArray()))

            articles.appendChild(content)

            val attachments = document.createElement("attachments")
            val attarchment = document.createElement("attachment")
            attarchment.setAttribute("name", "one")
            attarchment.setAttribute("src", "attarchment/1.zip")
            attarchment.setAttribute("visibility", "public")

            attachments.appendChild(attarchment)

            articles.appendChild(attachments)

            document.appendChild(articles)

            val tff = TransformerFactory.newInstance()
            val tf = tff.newTransformer()

            tf.setOutputProperty(OutputKeys.INDENT, "yes")

            tf.transform(DOMSource(document), StreamResult(xmlFile))

        }
        catch (e:Exception)
        {
            Log.e("article to xml",e.toString())
            return false
        }
        return true
    }
    fun packageFile():Boolean?
    {
        val root=Environment.getExternalStorageDirectory().toString()
        val zipFile= File(root+ SomeValue.userDir+SomeValue.zipDir+SomeValue.zipFile)

        if(zipFile.exists())
            try {
                zipFile.delete()
            } catch (e: Exception) {
                Toasty.warning(this, getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
                return false
            }

        val fds=FileDealService()
        val attachDir = File(root+ SomeValue.userDir+SomeValue.zipDir+"/attachments")
        if (!attachDir.exists())
            try {
                attachDir.mkdir()
            } catch (e: Exception) {
                Toasty.warning(this, getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
                return false
            }
        else//文件夹存在，可能有其它文件
        {
            fds.delFolder(attachDir.path)
            attachDir.mkdir()
        }
        val assetsDir = File(root+ SomeValue.userDir+SomeValue.zipDir+"/assets")
        if (!assetsDir.exists())
            try {
                assetsDir.mkdir()
            } catch (e: Exception) {
                Toasty.warning(this, getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
                return false
            }
        else//文件夹存在，可能有其它文件
        {
            fds.delFolder(assetsDir.path)
            assetsDir.mkdir()
        }
        val zipfiles=DirTraversal.arrayListFiles(root+ SomeValue.userDir+SomeValue.zipDir)
        try {
            ZipHelper.zipFiles(zipfiles, zipFile)
        }
        catch (e:Exception){
            Log.e("zip",e.toString())
            return false
        }
        return true
    }
}