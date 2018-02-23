package cn.atd3.code4a.view.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.atd3.code4a.Constant

import cn.atd3.code4a.Constant.*
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.FeedbackPresenter
import cn.atd3.code4a.view.inter.FeedbackInterface
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_feedback.*
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

class FeedbackActivity : AppCompatActivity(), FeedbackInterface {
    private lateinit var fbp: FeedbackPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)
        setContentView(R.layout.activity_feedback)
        fbp = FeedbackPresenter(this)
        feedBackButton.setOnClickListener({
            fbp.upToServer(content.text.toString(), contact.text.toString())
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
    Thread {
                try {
                    Thread.sleep(1500)
                } catch (e:InterruptedException) {
                    e.printStackTrace()
                } finally {
                    tipDialog.dismiss()
                }
            }.start()
}

     }
}
