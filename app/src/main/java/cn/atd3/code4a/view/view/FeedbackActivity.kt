package cn.atd3.code4a.view.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.atd3.code4a.Constant
import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.FeedbackPresenter
import cn.atd3.code4a.view.inter.FeedbackInterface
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import es.dmoral.toasty.Toasty

class FeedbackActivity : AppCompatActivity() ,FeedbackInterface{
    private  lateinit var fbp:FeedbackPresenter
    private lateinit var feedbackButton:BootstrapButton
    private lateinit var contentView:BootstrapEditText
    private lateinit var contactView:BootstrapEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        fbp= FeedbackPresenter(this)
        feedbackButton=findViewById(R.id.feedbacakbutton)
        contentView=findViewById(R.id.content)
        contactView=findViewById(R.id.contact)
        feedbackButton.setOnClickListener(View.OnClickListener {
            fbp.upToServer(contentView.text.toString(),contactView.text.toString())
        })
    }

    override fun getXmlString(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun showToast(infotype: Int, info: String) {
        runOnUiThread(
                Runnable {
                    when (infotype) {
                        SUCCESS -> Toasty.success(this, info, Toast.LENGTH_SHORT).show()
                        INFO -> Toasty.info(this, info, Toast.LENGTH_SHORT).show()
                        NORMAL -> Toasty.normal(this, info, Toast.LENGTH_SHORT).show()
                        WARNING -> Toasty.warning(this, info, Toast.LENGTH_SHORT).show()
                        ERROR -> Toasty.error(this, if (Constant.debugmodeinfo ) info else getString(R.string.remote_error), Toast.LENGTH_SHORT).show()
                    }
                }
        )
    }
}
