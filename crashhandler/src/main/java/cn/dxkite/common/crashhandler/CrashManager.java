package cn.dxkite.common.crashhandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 异常管理类
 * <p>
 * Created by DXkite on 2018/1/12 0012.
 */

public class CrashManager implements Thread.UncaughtExceptionHandler {

    private static CrashManager instance = new CrashManager();
    public static final String TAG = "CrashManager";
    private Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;
    private Config config;

    static Throwable throwable;
    static Thread thread;

    private CrashManager() {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void active(Config config, Context context) {
        this.config = config;
        this.context = context;
    }

    public static CrashManager getInstance() {
        return instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        CrashManager.throwable = throwable;
        CrashManager.thread = thread;
        context.startActivity(new Intent(context,ViewActivity.class));
    }

    public Context getContext() {
        return context;
    }

    public Config getConfig() {
        return config;
    }

    public boolean uploadException(Throwable throwable) {
        return false;
    }

    public boolean saveException(Throwable throwable) {
        return false;
    }

    public static String  throwable2String(Throwable throwable){
        Writer writer = new StringWriter();
        PrintWriter printwriter = new PrintWriter(writer);
        throwable.printStackTrace(printwriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printwriter);
            cause = cause.getCause();
        }
        printwriter.close();
        return writer.toString();
    }
}
