package cn.dxkite.debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
    private static CrashInfomation crashInfo = null;

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
        File crashDump=new File(instance.config.getCrashDumpPath());

        if (crashDump.exists()) {
            CrashInfomation infomation=(CrashInfomation)loadObject(crashDump);
            if (infomation!=null){
                DebugManager.crashInfo=infomation;
                Log.e(TAG," load exception ",infomation.getThrowable());
                instance.context.startActivity(new Intent(instance.context, ExceptionViewActivity.class));
            }
            crashDump.delete();
        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                crashInfo=new CrashInfomation(null,null,throwable,thread);
                Log.d(TAG,"uncaughtException  " +throwable.getClass().getName());
                saveObject(new File(instance.config.getCrashDumpPath()),crashInfo);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        return instance;
    }

    public static Object loadObject(File file) {
        if (!file.exists()) {
            return null;
        }
        ObjectInputStream input;
        try {
            input = new ObjectInputStream(new FileInputStream(file));
            Object obj = input.readObject();
            input.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveObject(File file, Object object) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(object);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static CrashInfomation getCrashInfo() {
        return crashInfo;
    }

    public static void setCrashInfo(CrashInfomation crashInfo) {
        DebugManager.crashInfo = crashInfo;
    }
}
