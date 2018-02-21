package cn.atd3.code4a.view.view;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.presenter.SettingFragmentPresenter;
import cn.atd3.code4a.view.inter.SettingFragmentInterface;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.INFO;
import static cn.atd3.code4a.Constant.NORMAL;
import static cn.atd3.code4a.Constant.SUCCESS;
import static cn.atd3.code4a.Constant.WARNING;

/**
 * Created by harry on 2018/1/15.
 */

public class SettingFragment extends PreferenceFragment implements SettingFragmentInterface {

    SettingFragmentPresenter sfp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        sfp = new SettingFragmentPresenter(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        sfp.onClick(preference);

        return super.onPreferenceTreeClick(preferenceScreen, preference);

    }

    @Override
    public String getXmlString(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public void showToast(final int infotype, final String info) {

        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        final  QMUITipDialog tipDialog ;
                        switch (infotype) {
                            case SUCCESS:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case INFO:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case NORMAL:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case WARNING:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case ERROR:
                                tipDialog =  new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord(Constant.debugmodeinfo?info:getString(R.string.remote_error))
                                        .create();
                                break;
                            default:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                                        .setTipWord(info)
                                        .create();
                        }
                        tipDialog.show();
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        finally {
                                            tipDialog.dismiss();
                                        }

                                    }
                                }
                        ).start();
                    }
                }
        );

    }

    @Override
    public void openAboutActivity() {
        Intent i = new Intent(getActivity(), AboutActivity.class);
        startActivity(i);
    }
}
