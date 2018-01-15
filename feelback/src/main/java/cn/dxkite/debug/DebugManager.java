package cn.dxkite.debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import cn.dxkite.debug.activity.ExceptionViewActivity;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Debug 管理
 * Created by DXkite on 2018/1/15 0015.
 */

public class DebugManager {

    static Config config;
    public static final String TAG = "DebugManager";
    private Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;
    private static DebugManager instance = new DebugManager();
    private static CrashInfo crashInfo = null;

    private DebugManager()
    {

    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        DebugManager.config = config;
    }

    public static DebugManager config(Context context,Config config){
        setConfig(config);
        instance.context=context;
        instance.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();


        CrashManager.setThrowable(new RuntimeException("dxkite test"));

        instance.context.startActivity(new Intent(instance.context, ExceptionViewActivity.class));

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                crashInfo=new CrashInfo(null,null,throwable,thread);
                Log.d(TAG,"uncaughtException  " +throwable.getClass().getName());
                instance.defaultHandler.uncaughtException(thread,throwable);
            }
        });
        return instance;
    }

    public static void connectSaveExceptionService() {
        Intent startIntent = new Intent(instance.context, DebugService.class);
        Log.d(TAG,"service start");
        instance.context.startService(startIntent);
        Log.d(TAG,"service connecting");
        instance.context.bindService(startIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder server) {
                Log.d(TAG,"onServiceConnected");
                final DebugService.DebugBinder debugService = (DebugService.DebugBinder)server;
                debugService.saveException();
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG,"onServiceDisconnected");
            }
        },BIND_AUTO_CREATE);
    }
}
