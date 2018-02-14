package cn.atd3.code4a.presenter;

import android.preference.Preference;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.view.inter.SettingFragmentInterface;


/**
 * Created by harry on 2018/1/15.
 */

public class SettingFragmentPresenter {

    SettingFragmentInterface sfi;

    public SettingFragmentPresenter(SettingFragmentInterface sfi){
        this.sfi=sfi;
    }

   public void onClick(Preference preference)
    {
        if(preference.getTitle()==sfi.getXmlString(R.string.setting_language))
        {
            sfi.showToast(Constant.INFO,sfi.getXmlString(R.string.info_changelanguage));
            return;
        }
        if(preference.getTitle()==sfi.getXmlString(R.string.setting_collection))
        {
            sfi.showToast(Constant.INFO,sfi.getXmlString(R.string.about_collection));
            return;
        }
        if(preference.getTitle()==sfi.getXmlString(R.string.setting_auth_donation))
        {
            sfi.openWebActivity(Constant.donationUrl);
            return;
        }
        if(preference.getTitle()==sfi.getXmlString(R.string.setting_software_info))
        {
            sfi.openWebActivity(Constant.softwareInfoUrl);
            return;
        }
        if(preference.getTitle()==sfi.getXmlString(R.string.setting_tieba))
        {
            sfi.openWebActivity(Constant.tiebaUrl);
            return;
        }
    }

}
