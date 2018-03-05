package cn.atd3.code4a.view.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_egg.*
import cn.atd3.code4a.R
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog


class EggActivity : AppCompatActivity() {
    var point = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)
        setContentView(R.layout.activity_egg)
        // 全屏
        if (actionBar != null) {
            actionBar.hide()
        }
        imageView.setOnClickListener {
            point += 1
            when (point) {
                1 -> imageView.setImageResource(R.mipmap.honeycomb)
                2 -> imageView.setImageResource(R.mipmap.icecream)
                3 -> imageView.setImageResource(R.mipmap.jelly)
                4 -> imageView.setImageResource(R.mipmap.kitkat)
                5 -> imageView.setImageResource(R.mipmap.lollipop)
                6 -> imageView.setImageResource(R.mipmap.marshmallow)
                7 -> imageView.setImageResource(R.mipmap.n)
                else -> {
                  val d=  QMUITipDialog.Builder(this)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                            .setTipWord("what are you 弄啥咧！")
                            .create()
                           d .show()
                    Thread {
                        Thread.sleep(1500)
                        runOnUiThread{
                            d.dismiss()
                        }
                    }.start()
                    point = 0
                }
            }

        }
    }
}