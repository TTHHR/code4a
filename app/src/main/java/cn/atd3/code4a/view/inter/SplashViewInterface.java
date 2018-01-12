package cn.atd3.code4a.view.inter;

import android.net.Uri;

import cn.qingyuyu.commom.ui.SplashAd;

/**
 * Created by harry on 2018/1/12.
 */

public interface SplashViewInterface {


    void onImageUpdate(Uri imguri);

    void showToast(int infotype, String info);

    String getXmlString(int resourceId);

    void setSplashAdListener(SplashAd.SplashAdListener sal);

    void showAd(int showtime);

}
