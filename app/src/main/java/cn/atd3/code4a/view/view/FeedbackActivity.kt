package cn.atd3.code4a.view.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.atd3.code4a.Constant

import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.FeedbackPresenter
import cn.atd3.code4a.view.inter.FeedbackInterface
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

class FeedbackActivity : AppCompatActivity(), FeedbackInterface {
    private lateinit var fbp: FeedbackPresenter
    private lateinit var feedbackButton: BootstrapButton
    private lateinit var contentView: BootstrapEditText
    private lateinit var contactView: BootstrapEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        fbp = FeedbackPresenter(this)
        feedbackButton = findViewById(R.id.feedbacakbutton)
        contentView = findViewById(R.id.content)
        contactView = findViewById(R.id.contact)
        feedbackButton.setOnClickListener(View.OnClickListener {
            fbp.upToServer(contentView.text.toString(), contactView.text.toString())
        })
    }

    override fun getXmlString(resourceId: Int): String {
        return getString(resourceId)
    }

     override fun showToast(infotype:Int, info:String) {

runOnUiThread {
    val tipDialog:QMUITipDialog
    when (infotype) {
        SUCCESS -> tipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(info)
                .create()
        INFO -> tipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(info)
                .create()
        NORMAL -> tipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                .setTipWord(info)
                .create()
        WARNING -> tipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(info)
                .create()
        ERROR -> tipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(if (Constant.debugmodeinfo) info else getString(R.string.remote_error))
                .create()
        else -> tipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                .setTipWord(info)
                .create()
    }
    tipDialog.show()
    Thread(
            Runnable {
                try {
                    Thread.sleep(1500)
                } catch (e:InterruptedException) {
                    e.printStackTrace()
                } finally {
                    tipDialog.dismiss()
                }
            }
    ).start()
}

     }
}
