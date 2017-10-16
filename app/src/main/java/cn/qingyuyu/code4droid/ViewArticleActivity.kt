package cn.qingyuyu.code4droid

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView

import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener

class ViewArticleActivity : AppCompatActivity() {

    private var richEditText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_article)
        val i = this.intent
        richEditText = findViewById<TextView>(R.id.rich_text)
        if(richEditText != null) {
            val imageGetter = URLImageParser(richEditText as TextView)
            if (i.getStringExtra("html") != null)
                richEditText!!.text = Html.fromHtml(i.getStringExtra("html"), imageGetter, null)
        }
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
        var drawBitmap: Bitmap? = null;
        override fun draw(canvas: Canvas) {
            if (drawBitmap != null) {
                canvas.drawBitmap(drawBitmap!!, 0f, 0f, paint)
            }
        }
    }
}
