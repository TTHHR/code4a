package cn.dxkite.common.crashhandler;

/**
 * 配置文件，用于配置Handler
 * Created by DXkite on 2018/1/12 0012.
 */

public class Config {
    protected String upstream;
    protected String savePath;

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
}
