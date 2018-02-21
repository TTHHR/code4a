package cn.atd3.code4a.view.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import cn.atd3.code4a.R
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog


class EggActivity : AppCompatActivity() {
    var point = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egg)
        // 全屏
        if (actionBar != null) {
            actionBar.hide()
        }
        val iv = findViewById<ImageView>(R.id.imageView)
        iv.setOnClickListener {
            point += 1
            when (point) {
                1 -> iv.setImageResource(R.mipmap.honeycomb)
                2 -> iv.setImageResource(R.mipmap.icecream)
                3 -> iv.setImageResource(R.mipmap.jelly)
                4 -> iv.setImageResource(R.mipmap.kitkat)
                5 -> iv.setImageResource(R.mipmap.lollipop)
                6 -> iv.setImageResource(R.mipmap.marshmallow)
                7 -> iv.setImageResource(R.mipmap.n)
                else -> {
                  val d=  QMUITipDialog.Builder(this)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                            .setTipWord("what are you 弄啥咧！")
                            .create()
                           d .show()
                    Thread(Runnable {
                        Thread.sleep(1500)
                        runOnUiThread{
                            d.dismiss()
                        }
                    }).start()
                    point = 0
                }
            }

        }
    }
}