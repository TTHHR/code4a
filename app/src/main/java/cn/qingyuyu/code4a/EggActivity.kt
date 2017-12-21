package cn.qingyuyu.code4a

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import es.dmoral.toasty.Toasty

class EggActivity : AppCompatActivity() {
    var point=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egg)
        val iv=findViewById<ImageView>(R.id.imageView)
        iv.setOnClickListener {
            point+=1
            when(point) {
                1 -> iv.setImageResource(R.mipmap.honeycomb)
                2 -> iv.setImageResource(R.mipmap.icecream)
                3 -> iv.setImageResource(R.mipmap.jelly)
                4 -> iv.setImageResource(R.mipmap.kitkat)
                5 -> iv.setImageResource(R.mipmap.lollipop)
                6 -> iv.setImageResource(R.mipmap.marshmallow)
                7 -> iv.setImageResource(R.mipmap.n)
                else -> {Toasty.error(this, "what are you 弄啥咧！", Toast.LENGTH_SHORT).show()
                        point=0
                     }
            }

        }
    }
}
