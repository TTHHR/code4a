package cn.atd3.code4a.library.fileselect.model.domain;


import cn.atd3.code4a.library.fileselect.model.BaseData;

/**
 * Created by liwei on 2017/4/28.
 */

public class HeaderData extends BaseData {

    private String path;

    public HeaderData(String path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public void setAppName(String appName) {

    }

    @Override
    public String getAppPackageName() {
        return null;
    }

    @Override
    public void setAppPackageName(String appPackageName) {

    }

    @Override
    public Long getSize() {
        return null;
    }

    @Override
    public void setSize(Long size) {

    }

    @Override
    public Long getLastModified() {
        return null;
    }

    @Override
    public void setLastModified(Long lastModified) {

    }

    @Override
    public Long getOpenTime() {
        return null;
    }

    @Override
    public void setOpenTime(Long openTime) {

    }
}
