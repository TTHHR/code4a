package cn.dxkite.common;

import android.content.Context;

/**
 * 错误捕获处理代码
 * Created by DXkite on 2017/12/25 0025.
 */

public interface ExceptionHandler {
    public void uncaughtException(Context context, Thread thread, Throwable exception);
}

