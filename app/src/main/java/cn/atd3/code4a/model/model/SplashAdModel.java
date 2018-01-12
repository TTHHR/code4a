package cn.atd3.code4a.model.model;

import android.net.Uri;

import cn.atd3.code4a.model.inter.SplashAdModelInterface;


/**
 * Created by harry on 2018/1/12.
 */

public class SplashAdModel implements SplashAdModelInterface {

    private Uri u=null;

    private String url=null;

    @Override
    public Uri getImageUri() {
        return u;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setImageUri(Uri imguri) {
            u=imguri;
    }

    @Override
    public void setUrl(String url) {
        this.url=url;
    }
}
