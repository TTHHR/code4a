package cn.qingyuyu.code4droid.remote;

import cn.atd3.proxy.ProxyObject;

/**
 * Created by DXkite on 2017/11/4 0004.
 */

public class Remote {
    public static String apiInterface ="http://code4a.i.atd3.cn/dev.php/open-api/1.0";
    /**
     * 用户接口
     */
    public static ProxyObject user=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface +"/user";
        }
    };
    /**
     * 文章接口
     */
    public static ProxyObject article=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface +"/article";
        }
    };

    /**
     * 标签接口
     */
    public static ProxyObject tag=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface +"/tags";
        }
    };

    /**
     * 类别接口
     */
    public static ProxyObject category=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface +"/category";
        }
    };

    /**
     * 文章上传接口
     */
    public static ProxyObject upload=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface +"/upload";
        }
    };

    /**
     * 百度用户接口
     */
    public static ProxyObject baiduUser=new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface +"/baidu-user";
        }
    };
}
