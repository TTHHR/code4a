package cn.dxkite.atd3proxy

import cn.atd3.proxy.Function
import cn.atd3.proxy.ProxyObject

/**
 * 服务器对象
 * Created by DXkite on 2017/11/3 0003.
 */
open abstract class ServerObject : ProxyObject() {
    abstract fun getServerUrl(): String;
    fun call(funName: String): Function {
        return Function(this, funName);
    }
    fun <T> call(funName: String,ret:Class<T>): Function {
        return Function(this, funName,ret);
    }
}