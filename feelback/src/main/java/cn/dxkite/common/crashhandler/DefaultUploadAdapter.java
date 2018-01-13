package cn.dxkite.common.crashhandler;

import org.json.*;

/**
 * 上传适配器
 * Created by DXkite on 2018/1/12 0012.
 */

public class DefaultUploadAdapter implements UploadAdapter {
    @Override
    public boolean upload(Throwable throwable) {
        return false;
    }
}
