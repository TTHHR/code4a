package cn.qingyuyu.code4a

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
    private var richEditText: TextView? = null
private lateinit var loadBar:ProgressBar
    private var articleid=0
    private var userid=999
    private lateinit var hd:Handler
    private val CHANGE_USERNAME=0
    private val CHANGE_CONTENT=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_article)
        loadBar=findViewById(R.id.progressBar)
        val i = this.intent
         articleid=i.getIntExtra("articleid",-1)
        userid=i.getIntExtra("userid",-1)
        if (articleid== -1) {
            Toasty.info(this, "error...", Toast.LENGTH_SHORT).show()
            loadBar.visibility=View.INVISIBLE
        }
        supportActionBar!!.title=i.getStringExtra("title")
        richEditText = findViewById<TextView>(R.id.rich_text)
        Log.e("id",""+articleid)
        if(richEditText != null) {
            val imageGetter = URLImageParser(richEditText as TextView)
             hd = object : Handler() {
                override fun handleMessage(msg: Message) {
                    when(msg.what)
                    {
                        CHANGE_CONTENT->{
                            loadBar.visibility=View.INVISIBLE
                            richEditText!!.text = Html.fromHtml(msg.obj as String, imageGetter, null)
                        }
                        CHANGE_USERNAME->{
                            supportActionBar!!.subtitle=msg.obj as String
                        }

                    }
                    super.handleMessage(msg)
                }
            }
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
    override fun onStart() {

        Thread( Runnable {
            run {
                var msg = Message()

                try {
                    var username=Remote.user.method("id2name",String.javaClass).call(userid)
                    if(username is String)
                    {
                            msg.obj=username
                    }
                    else
                    {
                        msg.obj="error"
                    }
                    msg.what=CHANGE_USERNAME
                    hd.sendMessage(msg)
                    Log.e("username",""+msg.obj)
                }
                catch (e:Exception)
                {
                    Log.e("userid",e.toString())
                }





                 msg = Message()
                try {
                    var a = Remote.article.method("getArticleById",Article::class.java).call(articleid)
                        if(a is Article)
                        {
                            Log.i("obj","is article")
                            if(a.content!=null)
                            {
                                // fix: kotlin keywords abstract error
                                var text= a.content   // abstract 属于关键字，不能用作属性名直接获取
                               var imgSet= getImgStr(text)
                                for(imgurl in imgSet)
                                {
                                    Log.e("img",imgurl)
                                    text=text.replace(imgurl,SomeValue.ServerAddress+imgurl)//地址转换成绝对地址
                                }
                                Log.e("final",text)
                                msg.obj=text
                            }
                            else
                            {
                                Log.e("obj","null")
                                msg.obj="null"
                            }
                         }
                        else
                        {
                            msg.obj="error"
                        }
                    msg.what=CHANGE_CONTENT
                    hd.sendMessage(msg)
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
     * @param sets html字符串
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
