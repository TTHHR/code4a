package cn.dxkite.debug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cn.dxkite.debug.activity.ExceptionViewActivity;

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
    private static CrashInformation crashInfo = null;

    private DebugManager() {

    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        DebugManager.config = config;
    }

    public static DebugManager config(Context context, Config config) {
        setConfig(config);
        instance.context = context;
        instance.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                crashInfo = new CrashInformation(null, null, throwable, thread);
                // 保存到文件
                instance.saveCrashInfomation(crashInfo);
                Log.d(TAG, "uncaughtException  " + throwable.getClass().getName());
                saveObject(new File(instance.config.getCrashDumpPath()), crashInfo);
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(instance.context, "程序异常，文件日志记录成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }
                }.start();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // TODO 重启APP
                instance.defaultHandler.uncaughtException(thread, throwable);
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

    public static CrashInformation getCrashInfo() {
        return crashInfo;
    }

    public static void setCrashInfo(CrashInformation crashInfo) {
        DebugManager.crashInfo = crashInfo;
    }

    public boolean saveCrashInfomation(CrashInformation information) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String millis = String.valueOf(information.getTimestamp());
        String fileName = context.getPackageName() + "_" + format.format(information.getTimestamp()) + "_" + millis;
        return saveFile(config.getSavePath() + File.separator + fileName + ".log", information.toString())
                &&
                saveFile(config.getUploadSavePath() + File.separator + fileName + ".json", information.toJsonString());
    }

    private static boolean saveFile(String path, String content) {
        File file = new File(path);
        Log.d(TAG, "prepare directory:" + path);
        if (!file.getParentFile().exists() && file.getParentFile().mkdirs()) {
            Log.d(TAG, "create log directory");
        }
        try {
            Log.d(TAG, "start write file");
            if (!file.exists() && file.createNewFile()) {
                Log.d(TAG, "create log file:" + file.getAbsolutePath());
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.toString().getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error writer file!", e);
            return false;
        }
        return true;
    }

    public static boolean hasLastCrashInfomation() {
        File crashDump = new File(instance.config.getCrashDumpPath());
        if (crashDump.exists()) {
            CrashInformation infomation = (CrashInformation) loadObject(crashDump);
            if (infomation == null) {
                return false;
            }
            DebugManager.crashInfo = infomation;
            Log.e(TAG, " load exception ", infomation.getThrowable());
            crashDump.delete();
            return true;
        }
        return false;
    }

    public static void jumpToShow() {
        instance.context.startActivity(new Intent(instance.context, ExceptionViewActivity.class));
    }

    public static void askIfCrash(Context context, int iconRes) {
        if (hasLastCrashInfomation()) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setIcon(iconRes);
            normalDialog.setTitle("异常崩溃提示");
            normalDialog.setMessage("检测到上次异常崩溃，是否查看详情?");
            normalDialog.setPositiveButton("是",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            jumpToShow();
                        }
                    });
            normalDialog.setNegativeButton("否",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(instance.context, "作为程序员，你居然不想看看", Toast.LENGTH_SHORT).show();
                        }
                    });
            normalDialog.show();
        }
    }

}
