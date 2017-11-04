package cn.dxkite.common.rpc;

import java.io.File;
import java.io.InputStream;

import cn.atd3.proxy.ProxyController;

/**
 * 远程调用状态控制器
 * Created by DXkite on 2017/11/3 0003.
 */

public class DefaultController implements ProxyController {

    /**
     * 获取全部保持用户状态的 Cookie
     * @return 字符串，返回一个Http协议的Cookie设置头：name=value;name=value
     */
    @Override
    public String getCookies() {
        return null;
    }

    /**
     * 设置一个Cookie,
     * @param cookie 接受一个HTTP协议的Set-Cookie字段的格式：name=value; something setting
     * @return 是否保存成功
     */
    @Override
    public boolean saveCookie(String cookie) {
        return false;
    }

    /**
     * 保存从服务器上下载的文件
     * @param contentType 文件的类型（MIME）
     * @param content 文件的二进制内容
     * @param contentLength 文件的长度
     * @return 文件引用File
     */
    @Override
    public File saveFile(String contentType, InputStream content, long contentLength) {
        return null;
    }
}
