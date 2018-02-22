package cn.atd3.code4a.view.view

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

import cn.atd3.code4a.Constant
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.SettingFragmentPresenter
import cn.atd3.code4a.view.inter.SettingFragmentInterface

import cn.atd3.code4a.Constant.ERROR
import cn.atd3.code4a.Constant.INFO
import cn.atd3.code4a.Constant.NORMAL
import cn.atd3.code4a.Constant.SUCCESS
import cn.atd3.code4a.Constant.WARNING

/**
 * Created by harry on 2018/1/15.
 */

class SettingFragment : PreferenceFragment(), SettingFragmentInterface {

    lateinit var sfp: SettingFragmentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)
        sfp = SettingFragmentPresenter(this)
    }

    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen, preference: Preference): Boolean {

        sfp.onClick(preference)

        return super.onPreferenceTreeClick(preferenceScreen, preference)

    }

    override fun getXmlString(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun showToast(infotype: Int, info: String) {

        activity.runOnUiThread {
            val tipDialog: QMUITipDialog
            when (infotype) {
                SUCCESS -> tipDialog = QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(info)
                        .create()
                INFO -> tipDialog = QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(info)
                        .create()
                NORMAL -> tipDialog = QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
                WARNING -> tipDialog = QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(info)
                        .create()
                ERROR -> tipDialog = QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(if (Constant.debugmodeinfo) info else getString(R.string.remote_error))
                        .create()
                else -> tipDialog = QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
            }
            tipDialog.show()
            Thread {
                        try {
                            Thread.sleep(1500)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } finally {
                            tipDialog.dismiss()
                        }
                    }.start()
        }

    }

    override fun openAboutActivity() {
        val i = Intent(activity, AboutActivity::class.java)
        startActivity(i)
    }
}
