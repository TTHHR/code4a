package cn.atd3.code4a.view.view


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import cn.atd3.code4a.Constant
import cn.atd3.code4a.R
import cn.atd3.code4a.presenter.SettingPresenter
import cn.atd3.code4a.view.inter.SettingActivityInterface
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_setting.*
import cn.atd3.code4a.Constant.INFO
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView


class SettingActivity : AppCompatActivity(), SettingActivityInterface {
    private lateinit var sfp: SettingPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)
        setContentView(R.layout.activity_setting)
        topBar.addLeftBackImageButton().setOnClickListener( {
            finish()
        })
        topBar.setTitle(getString(R.string.setting))
        sfp = SettingPresenter(this)
        initListView()

    }

    override fun onStart() {
        try {
            topBar.setBackgroundColor(Color.parseColor(Constant.themeColor))
        } catch (e: Exception) {
            topBar.setBackgroundColor(Color.parseColor(Constant.defaultThemeColor))
        }
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        sfp.saveSetting()//保存设置
    }
    private fun initListView() {
        val languageView = groupListView.createItemView(getString(R.string.setting_language))
        languageView.detailText = sfp.language
        languageView.setOnClickListener {
            showToast(INFO,getString(R.string.info_changelanguage))
            val items=resources.getStringArray(R.array.language_list_key)
            val builder = QMUIDialog.CheckableDialogBuilder(this)
                    .setCheckedIndex(0)
                    .addItems(items, {
                        dialog, which ->
                        val values=resources.getStringArray(R.array.language_list_value)
                        sfp.language=values[which]
                        languageView.detailText=values[which]
                        dialog!!.dismiss()
                    })
            builder.show()

        }

        val themeView = groupListView.createItemView(getString(R.string.setting_theme_color))
        themeView.detailText =sfp.themeColor
        themeView.setOnClickListener {
            val builder = QMUIDialog.EditTextDialogBuilder(this)
            builder.setTitle(getString(R.string.setting_theme_summary))
            builder.setPlaceholder(Constant.defaultThemeColor)
            builder.setInputType(InputType.TYPE_CLASS_TEXT)
                    .addAction(getString(R.string.button_cancel), {
                        dialog, _->
                        dialog!!.dismiss()
                    })
                    .addAction(getString(R.string.button_ok),  {
                        dialog, _->
                        val color = builder.editText.text
                         if(!color.isEmpty())
                        {
                            sfp.themeColor=color.toString()
                            themeView.detailText=color
                        }
                        dialog!!.dismiss()

                    })
                    .show()

        }

        val debugView = groupListView.createItemView(getString(R.string.setting_debug))
        debugView.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        debugView.switch.isChecked=sfp.debug
       debugView.switch.setOnCheckedChangeListener{_,isChecked-> sfp.debug=isChecked }

        val collectionView = groupListView.createItemView(getString(R.string.setting_collection))
        collectionView.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        collectionView.switch.isChecked=sfp.collection
        collectionView.switch.setOnCheckedChangeListener { _, isChecked -> sfp.collection=isChecked }

        val aboutView = groupListView.createItemView(getString(R.string.setting_about))
        aboutView.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        aboutView.setOnClickListener { val i=Intent(this,AboutActivity::class.java)
            startActivity(i)
        }


        QMUIGroupListView.newSection(this)
//                .setTitle("Section 1: 默认提供的样式")
//                .setDescription("Section 1 的描述")
                .addItemView(languageView,null)
                .addItemView(themeView, null)
                .addItemView(debugView, null)
                .addItemView(collectionView, null)
                .addItemView(aboutView, null)
                .addTo(groupListView)
    }

    override fun getXmlString(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun showToast(infotype: Int, info: String) {

        this.runOnUiThread {
            val tipDialog: QMUITipDialog
            when (infotype) {
                Constant.SUCCESS -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(info)
                        .create()
                Constant.INFO -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(info)
                        .create()
                Constant.NORMAL -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                        .setTipWord(info)
                        .create()
                Constant.WARNING -> tipDialog = QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(info)
                        .create()
                Constant.ERROR -> tipDialog = QMUITipDialog.Builder(this)
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
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    tipDialog.dismiss()
                }
            }.start()
        }

    }
}

