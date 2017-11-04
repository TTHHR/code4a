package cn.qingyuyu.code4droid.remote;

import cn.atd3.proxy.ProxyObject;

/**
 * Created by DXkite on 2017/11/4 0004.
 */

public class Remote {
    /**
     * 用户接口
     */
    public static ProxyObject user=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return "http://code4a.i.atd3.cn/dev.php/open-api/1.0/user";
        }
    };
    /**
     * 文章接口
     */
    public static ProxyObject article=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return "http://code4a.i.atd3.cn/dev.php/open-api/1.0/article";
        }
    };

    /**
     * 标签接口
     */
    public static ProxyObject tag=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return "http://code4a.i.atd3.cn/dev.php/open-api/1.0/tags";
        }
    };

    /**
     * 类别接口
     */
    public static ProxyObject category=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return "http://code4a.i.atd3.cn/dev.php/open-api/1.0/category";
        }
    };

    /**
     * 文章上传接口
     */
    public static ProxyObject upload=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return "http://code4a.i.atd3.cn/dev.php/open-api/1.0/upload";
        }
    };

    /**
     * 百度用户接口
     */
    public static ProxyObject baiduUesr=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return "http://code4a.i.atd3.cn/dev.php/open-api/1.0/baidu-user";
        }
    };
}
