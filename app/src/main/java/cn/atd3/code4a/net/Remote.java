package cn.atd3.code4a.net;

import cn.atd3.proxy.ProxyObject;

/**
 * Created by DXkite on 2017/11/4 0004.
 */

public class Remote {
    private static String apiInterface = "http://code4a.atd3.cn/api/v1.0";
    /**
     * 用户接口
     */
    public static ProxyObject user = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/user";
        }
    };
    /**
     * 超级用户接口
     */
    public static ProxyObject superUser = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/android-super-user";
        }
    };
    /**
     * 文章接口
     */
    public static ProxyObject article = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/article";
        }
    };
    /**
     * 附件接口
     */
    public static ProxyObject attachment = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/attachment";
        }
    };


    /**
     * 类别接口
     */
    public static ProxyObject category = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/category";
        }
    };

    /**
     * 百度用户接口
     */
    public static ProxyObject baiduUser = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/baidu-user";
        }
    };

    /**
     * Android 信息接口
     */
    public static ProxyObject androidMessage = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/android-message";
        }
    };

    /**
     * 启动信息收集接口
     */
    public static ProxyObject collection = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/android-collection";
        }
    };
    /**
     * 反馈
     */
    public static ProxyObject report = new ProxyObject() {
        @Override
        public String getCallUrl() {
            return apiInterface + "/android-report";
        }
    };
}
