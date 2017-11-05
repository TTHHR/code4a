package cn.qingyuyu.code4a.library.fileselect.model;

import java.io.Serializable;

/**
 * Entity mapped to table "BASE_DATA".
 */
public abstract class BaseData implements Serializable{

    private static final long serialVersionUID = -2083503801443301445L;

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getPath();

    public abstract void setPath(String path);

    public abstract String getAppName();

    public abstract void setAppName(String appName);

    public abstract String getAppPackageName();

    public abstract void setAppPackageName(String appPackageName);

    public abstract Long getSize();

    public abstract void setSize(Long size);

    public abstract Long getLastModified();

    public abstract void setLastModified(Long lastModified);

    public abstract Long getOpenTime();

    public abstract void setOpenTime(Long openTime);

}
