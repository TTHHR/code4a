package cn.qingyuyu.code4a

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.preference.PreferenceActivity
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.qingyuyu.code4a.remote.Remote

import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import cn.qingyuyu.code4a.remote.bean.Article
import es.dmoral.toasty.Toasty

class ViewArticleActivity : AppCompatActivity() {

    private var richEditText: TextView? = null

    private var id=0
    private lateinit var hd:Handler



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_article)
        val i = this.intent
         id=i.getIntExtra("id",-1)
        if (id!= -1) {
            Toasty.info(this, "Loding...", Toast.LENGTH_SHORT).show()
        }
        richEditText = findViewById<TextView>(R.id.rich_text)
        Log.e("id",""+id)
        if(richEditText != null) {
            val imageGetter = URLImageParser(richEditText as TextView)
             hd = object : Handler() {
                override fun handleMessage(msg: Message) {
                    val s = msg.obj as String

                    richEditText!!.text = Html.fromHtml(s, imageGetter, null)

                    super.handleMessage(msg)
                }
            }
        }
        }

    override fun onStart() {

        Thread( Runnable {
            run {
                var msg = Message()

                try {
                    var a = Remote.article.method("getArticleById",Article::class.java).call(id)
                        if(a is Article)
                        {
                            Log.i("obj","is article")
                            if(a.abstract!=null)
                            {
//                                msg.obj="\"<blockquote>Android 端的富文本编辑器</blockquote>\" +\n" +
//                                        "                    \"<ul><li>支持实时编辑</li><li>支持图片插入,加粗,斜体,下划线,删除线,列表,引用块,撤销与恢复等</li><li>使用<u>Glide</u>加载图片</li></ul>\\n\" +\n" +
//                                        "                    \"<img src=\"http://img5.duitang.com/uploads/item/201409/07/20140907195835_GUXNn.thumb.700_0.jpeg\">\" +\n" +
//                                        "                    \"<img src=\"http://blog.qingyuyu.cn/storage/a5124910.jpg\">\""
//                                Log.i("obj",a.toString());
//                                Log.i("obj",a.getAbstract())
                                // fix: kotlin keywords abstract error
                                msg.obj=a.getAbstract() + a.content   // abstract 属于关键字，不能用作属性名直接获取
//                                msg.obj=a.content;
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
                canvas.drawBitmap(drawBitmap!!, 0f, 0f, paint)
            }
        }
    }
}
