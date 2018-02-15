package cn.atd3.code4a.view.view;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.presenter.SettingFragmentPresenter;
import cn.atd3.code4a.view.inter.SettingFragmentInterface;
import es.dmoral.toasty.Toasty;

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
        sfp=new SettingFragmentPresenter(this);
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
    public void showToast(final int infotype, @NotNull final String info) {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        switch (infotype) {
                            case SUCCESS:
                                Toasty.success(getActivity(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case INFO:
                                Toasty.info(getActivity(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case NORMAL:
                                Toasty.normal(getActivity(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case WARNING:
                                Toasty.warning(getActivity(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR:
                                Toasty.error(getActivity(), Constant.debugmodeinfo?info:getString(R.string.remote_error), Toast.LENGTH_SHORT).show();
                                break;
                            default:

                        }
                    }
                }
        );
    }

    @Override
    public void openAboutActivity() {
        Intent i=new Intent(getActivity(),AboutActivity.class);
        startActivity(i);
    }
}
