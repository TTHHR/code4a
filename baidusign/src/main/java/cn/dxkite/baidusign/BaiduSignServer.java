package cn.dxkite.baidusign;

import cn.atd3.proxy.ProxyObject;

/**
 * 百度服务器测试程序
 * Created by DXkite on 2017/11/4 0004.
 */

public class BaiduSignServer extends ProxyObject {

    @Override
    public String getCallUrl() {
        return "http://code4a.atd3.cn/open-api/1.0/baidu-user";
    }
}
