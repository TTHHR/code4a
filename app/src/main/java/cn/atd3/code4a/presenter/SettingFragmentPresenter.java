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
        }
        if(preference.getTitle()==sfi.getXmlString(R.string.setting_loginout))
        {
           //退出登录
            sfi.showToast(Constant.SUCCESS,"退出登陆成功，Success");
        }
        if(preference.getTitle()==sfi.getXmlString(R.string.setting_software_info))
        {
            sfi.openWebActivity("https://github.com/TTHHR/code4a/blob/dev/开源说明.txt");

        }
    }

}
