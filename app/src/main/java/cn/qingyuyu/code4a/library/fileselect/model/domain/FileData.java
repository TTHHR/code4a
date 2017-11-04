package cn.qingyuyu.code4a.library.fileselect.model.domain;


import cn.qingyuyu.code4a.library.fileselect.model.BaseData;

/**
 * Created by liwei on 2017/4/27.
 */

public class FileData extends BaseData {

    private boolean isFolder;
    private String name;
    private String path;
    private Long lastModified;

    public boolean isFolder() {
        return isFolder;
    }

    public void setIsFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
        return lastModified;
    }

    @Override
    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Long getOpenTime() {
        return null;
    }

    @Override
    public void setOpenTime(Long openTime) {

    }
}
