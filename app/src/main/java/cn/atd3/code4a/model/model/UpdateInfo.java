package cn.atd3.code4a.model.model;

/**
 * Created by DXkite on 2018/3/22 0022.
 * 更新信息
 *
 */

public class UpdateInfo {
    protected String name;
    protected String version;

    private String download;
    private String versionInfo;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }
}
