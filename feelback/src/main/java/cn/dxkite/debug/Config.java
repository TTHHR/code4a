package cn.dxkite.debug;

/**
 * 配置文件，用于配置Handler
 * Created by DXkite on 2018/1/12 0012.
 */

public class Config {
    /**
     * 上传流
     */
    protected String upstream;
    /**
     * 本地保存路径
     */
    protected String savePath;

    /**
     * 是否为Debug模式
     */
    protected boolean debug;

    /**
     * 是否自动上传异常崩溃信息
     */
    protected boolean autoUpload=true;


    protected String packageName=null;

    public boolean isDebug() {
        return debug;
    }

    public Config setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public String getUpstream() {
        return upstream;
    }

    public Config setUpstream(String upstream) {
        this.upstream = upstream;
        return this;
    }

    public String getSavePath() {
        return savePath;
    }

    public Config setSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
