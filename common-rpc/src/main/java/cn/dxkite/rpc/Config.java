package cn.dxkite.rpc;

import android.app.Application;
import cn.atd3.proxy.ProxyConfig;
import cn.atd3.proxy.ProxyController;

/**
 * RPC 配置操作
 * Created by DXkite on 2017/11/3 0003.
 */

public class Config extends  ProxyConfig {
    static  {
        Config.setController(new Controller());
    }
}
