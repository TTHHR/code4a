package cn.dxkite.atd3proxy

import cn.atd3.proxy.ProxyController
import java.io.File
import java.io.InputStream

/**
 * 代理资源控制器
 * Created by DXkite on 2017/11/2 0002.
 */
class Controller : ProxyController {
    override fun getCookies(): String {
        return "";
    }

    override fun saveCookies(p0: String?): Boolean {
        return false;
    }

    override fun saveFile(p0: String?, p1: InputStream?, p2: Long): File {
        return File("some.txt");
    }
}