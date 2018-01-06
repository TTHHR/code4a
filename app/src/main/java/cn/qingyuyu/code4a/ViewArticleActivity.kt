package cn.qingyuyu.code4a

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message

import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import cn.carbs.android.library.MDDialog
import cn.qingyuyu.code4a.remote.Remote

import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import cn.qingyuyu.code4a.remote.bean.Article
import cn.qingyuyu.commom.SomeValue
import com.beardedhen.androidbootstrap.BootstrapButton
import es.dmoral.toasty.Toasty
import java.util.regex.Matcher
import java.util.regex.Pattern

class ViewArticleActivity : AppCompatActivity() {
    private lateinit var mycomment: BootstrapButton
    private var richText: TextView? = null
private lateinit var loadBar:ProgressBar
    private lateinit var copyButton:BootstrapButton
    private var articleid=0
    private var userid=999


    private lateinit var article:Article

    private lateinit var imageGetter:URLImageParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_article)
        loadBar=findViewById(R.id.progressBar)
        copyButton=findViewById(R.id.copy)
        copyButton.setOnClickListener {
            if(richText !=null)
            {
                val cm = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  cm.primaryClip=ClipData.newPlainText("code", richText!!.text)
                Toasty.info(this,getString(R.string.info_success),Toast.LENGTH_SHORT).show()
            }
        }
        val i = this.intent
         articleid=i.getIntExtra("articleid",-1)
        userid=i.getIntExtra("userid",-1)
        if (articleid== -1) {
            Toasty.info(this, "error...", Toast.LENGTH_SHORT).show()
            loadBar.visibility=View.INVISIBLE
        }
        supportActionBar!!.title=i.getStringExtra("title")
        richText = findViewById<TextView>(R.id.rich_text)
        Log.e("id",""+articleid)
        if(richText != null) {
             imageGetter = URLImageParser(richText as TextView)

        }
    mycomment=findViewById(R.id.mycomment)
        mycomment.setOnClickListener{
            MDDialog.Builder(this@ViewArticleActivity)
                    .setTitle("Edit my comment")
                    .setContentView(R.layout.dialog_mycomment)
                    .setNegativeButton(R.string.button_cancel,View.OnClickListener {

                    })
                    .setPositiveButton(R.string.button_ok,View.OnClickListener {

                    })

                    .setWidthMaxDp(600)
                    .setShowTitle(true)
                    .setShowButtons(true)
                    .create()
                    .show()
        }


    }
    //创建菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_viewarticle, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!=null)
        when(item.itemId)
        {
            R.id.action_downloadfile->{
                //下载附件
            }

            R.id.del->{
                //删除文章
            }

            R.id.edit->{
                //编辑文章

                val i= Intent(this@ViewArticleActivity,EditArticleActivity::class.java)

                i.putExtra("content", article.content)

                startActivity(i)

            }


        }

        return super.onOptionsItemSelected(item)
    }




    override fun onStart() {

        Thread( Runnable {
            run {
                var usern=""

                try {
                    val username=Remote.user.method("id2name",String::class.java).call(userid)
                    if(username is String)
                    {
                           usern=username
                    }
                    else
                    {
                       usern="error"
                    }

                    Log.e("username",""+username)
                }
                catch (e:Exception)
                {
                    Log.e("userid",e.toString())
                }

                runOnUiThread {
                     supportActionBar!!.subtitle=usern
                    }

                var text=""
                try {
                    val a = Remote.article.method("getArticleById",Article::class.java).call(articleid)
                        if(a is Article)
                        {
                            Log.i("obj","is article")
                            if(a.content!=null)
                            {
                                article=a
                                // fix: kotlin keywords abstract error
                                text= a.content   // abstract 属于关键字，不能用作属性名直接获取
                               val imgSet= getImgStr(text)
                                for(imgurl in imgSet)
                                {
                                    Log.e("img",imgurl)
                                    text=text.replace(imgurl,SomeValue.ServerAddress+imgurl)//地址转换成绝对地址
                                }
                                Log.e("final",text)
                            }
                            else
                            {
                                Log.e("obj","null")
                               text=""
                            }
                         }

                    runOnUiThread {
                        loadBar.visibility=View.INVISIBLE
                        if(imageGetter!=null) {
                            richText!!.text = Html.fromHtml(text, imageGetter, null)
                            copyButton.isClickable = true
                        }
                    }
                }
                catch (e:Exception)
                {
                    Log.e("net error",""+e)
                }
            }
        }).start()

        super.onStart()
    }

    /**
     * 得到网页中图片的地址
     * @param  html字符串
     */
    fun getImgStr(htmlStr: String): Set<String> {
        val pics = HashSet<String>()
        var img = ""
        val p_image: Pattern
        val m_image: Matcher
        val regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>"
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE)
        m_image = p_image.matcher(htmlStr)
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group()
            // 匹配<img>中的src数据
            val m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img)
            while (m.find()) {
                if(!m.group(1).startsWith("http:"))//图片为表情就跳过
                pics.add(m.group(1))
            }
        }
        return pics
    }

    inner class URLImageParser(internal var mTextView: TextView) : Html.ImageGetter {

        override fun getDrawable(source: String): Drawable {
            val urlDrawable = URLDrawable()
            ImageLoader.getInstance().loadImage(source,
                    object : SimpleImageLoadingListener() {
                        override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                            urlDrawable.drawBitmap = loadedImage
                            urlDrawable.setBounds(0, 0, loadedImage!!.width, loadedImage.height)
                            mTextView.invalidate()
                            mTextView.text = mTextView.text
                        }
                    })
            return urlDrawable
        }
    }

    inner class URLDrawable : BitmapDrawable() {
        var drawBitmap: Bitmap? = null
        override fun draw(canvas: Canvas) {
            if (drawBitmap != null) {
                canvas.drawBitmap(drawBitmap, 0f, 0f, paint)
            }
        }
    }
}
