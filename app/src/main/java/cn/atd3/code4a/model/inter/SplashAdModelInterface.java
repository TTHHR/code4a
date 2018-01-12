package cn.atd3.code4a.model.inter;

import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * Created by harry on 2018/1/12.
 */

public interface SplashAdModelInterface {

    //提供数据
    Uri getImageUri();
    String getUrl();

    //存储数据
    void setImageUri(Uri imguri);
    void setUrl(String url);

}
