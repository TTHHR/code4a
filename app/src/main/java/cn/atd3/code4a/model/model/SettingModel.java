package cn.atd3.code4a.model.model;

import java.io.Serializable;

import cn.atd3.code4a.Constant;

/**
 * Created by harry on 2018/3/3.
 *
 */

public class SettingModel  implements Serializable{
    private String language="zh";
    private String themeColor= Constant.defaultThemeColor;
    private boolean debug=false;
    private boolean collection=true;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }
}
