package cn.dxkite.common.crashhandler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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
        return save(throwable);
    }

    static String  throwable2String(Throwable throwable){
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


    private Map<String, String> infos = new HashMap<String, String>();
    private String packageName=null;
    private void insertDeviceInfo() {
        Log.d(TAG, "prepare device info");
        PackageManager pm = context.getPackageManager();

        try {
            packageName = context.getPackageName();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            infos.put("versionName", pi.versionName == null ? "null" : pi.versionName);
            infos.put("versionCode", pi.versionCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            packageName = "CrashLog";
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                infos.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean save(Throwable ex) {
        insertDeviceInfo();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> enter : infos.entrySet()) {
            sb.append("\t\t").append(enter.getKey()).append("\t:\t").append(enter.getValue()).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printwriter = new PrintWriter(writer);
        ex.printStackTrace(printwriter);
        Throwable cause = ex.getCause();
        Log.d(TAG, "log causes...");
        while (cause != null) {
            cause.printStackTrace(printwriter);
            cause = cause.getCause();
        }
        printwriter.close();
        sb.append(writer.toString());
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(System.currentTimeMillis());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = config.getSavePath();
            File dir = new File(path);
            if (!dir.exists() &&  !dir.mkdirs()) {
                return false;
            }

            try {
                Log.d(TAG, "start write file");
                File file = new File(path + File.separator + packageName + "_" + time + "_" + System.currentTimeMillis() + ".log");
                if (!file.exists() && file.createNewFile()) {
                    Log.d(TAG, "create log file:" + file.getAbsolutePath());
                }
                FileOutputStream out = new FileOutputStream(file);
                out.write(sb.toString().getBytes());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "error writer file!", e);
            }
        }
        return true;
    }
}
