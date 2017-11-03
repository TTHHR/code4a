package cn.dxkite.atd3proxy

import cn.atd3.proxy.Proxy
import cn.atd3.proxy.ProxyObject

/**
 * 服务对象
 * Created by DXkite on 2017/11/3 0003.
 */
open abstract class ServerObject : ProxyObject {
    abstract fun getServerUrl(): String;
    fun call(funName: String): Proxy {
        return Proxy(this, funName);
    }
}