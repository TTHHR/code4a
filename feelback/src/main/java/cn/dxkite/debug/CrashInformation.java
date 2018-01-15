package cn.dxkite.debug;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常信息
 * Created by DXkite on 2018/1/15 0015.
 */

public class CrashInformation implements Serializable{
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 用户ID
     */
    private String userId;

    // 异常信息
    private Throwable throwable;
    private long threadId;
    private String threadName;
    /**
     * 环境信息
     */
    private Map<String,String> environment;
    /**
     * 程序包名
     */
    private String packageName;
    // 用户提交信息
    private String title;
    private String message;

    public CrashInformation(String deviceId, String userId, Throwable throwable, Thread thread) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.throwable = throwable;
        this.threadId = thread.getId();
        this.threadName =thread.getName();
        this.environment=new HashMap<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void addEnv(String name,String value) {
        environment.put(name,value);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 上传的格式，使用JSON描述异常
     * 用于服务器端个性化显示
     * @return JSON格式编码的字符串
     */
    public String toJsonString() {
        JSONObject debug=new JSONObject();
        try {
            debug.put("device",deviceId);
            debug.put("user",userId);
            debug.put("title",title);
            debug.put("message",message);
            debug.put("thread",dumpThreadJson());
            debug.put("env",dumpEnvJson());
            JSONArray traceInfo=new JSONArray();
            for (Throwable tmp:dumpThrowable()) {
                JSONObject trace=new JSONObject();
                trace.put("name",tmp.getClass().getName());
                trace.put("message",tmp.getMessage());
                JSONArray traceback=new JSONArray();
                for (StackTraceElement ele:tmp.getStackTrace() ){
                    JSONObject elo=new JSONObject();
                    elo.put("file",ele.getFileName());
                    if (ele.isNativeMethod()) {
                        elo.put("native",ele.isNativeMethod());
                    }else{
                        elo.put("class",ele.getClassName());
                        elo.put("line",ele.getLineNumber());
                        elo.put("method",ele.getMethodName());
                    }
                    traceback.put(elo);
                }
                trace.put("traceback",traceback);
                traceInfo.put(trace);
            }
            debug.put("traceinfo",traceInfo);
        } catch (JSONException e) {
            return "<invalid json>";
        }
        return debug.toString();
    }

    /**
     * 文件预览模式，用于文件预览
     * @return 纯文本描述异常信息
     */
    @Override
    public String toString() {
        StringBuffer stringBuilder=new StringBuffer();
        stringBuilder.append("Thread Info:\r\n");
        stringBuilder.append(dumpThreadString());
        stringBuilder.append("Environment Info:\r\n");
        stringBuilder.append(dumpEnvString());
        stringBuilder.append("Trace Info:\r\n");
        stringBuilder.append(dumpException());
        return  stringBuilder.toString();
    }

    /**
     * Dump环境信息
     * @return 环境信息
     */
    private Map<String,String> dumpEnv() {
        Map<String,String> env=new HashMap<>();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                env.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return  env;
    }

    private String dumpEnvString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> enter : dumpEnv().entrySet()) {
            sb.append("\t\t")
                    .append(enter.getKey())
                    .append("\t:\t")
                    .append(enter.getValue())
                    .append("\r\n");
        }
        return sb.toString();
    }

    private JSONObject dumpEnvJson() {
        JSONObject object=new JSONObject();
        for (Map.Entry<String, String> enter : dumpEnv().entrySet()) {
            try {
                object.put(enter.getKey(),enter.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private String dumpException() {
        Writer writer = new StringWriter();
        PrintWriter printwriter = new PrintWriter(writer);
        for (Throwable tmp:dumpThrowable()) {
            tmp.printStackTrace(printwriter);
        }
        printwriter.close();
        return writer.toString();
    }

    private List<Throwable> dumpThrowable() {
        List<Throwable> throwables=new ArrayList<>();
        for(Throwable tmp=throwable;tmp!=null;tmp=tmp.getCause()){
            throwables.add(tmp);
        }
        return throwables;
    }

    private Map<String,String> dumpThread() {
        Map<String,String> thread=new HashMap<>();
        thread.put("threadId",String.valueOf(this.threadId));
        thread.put("threadName",this.threadName);
        return  thread;
    }
    private JSONObject dumpThreadJson() {
        JSONObject thread=new JSONObject();
        try {
            thread.put("threadId",String.valueOf(this.threadId));
            thread.put("threadName",this.threadName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  thread;
    }
    private String dumpThreadString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> enter : dumpThread().entrySet()) {
            sb.append("\t\t")
                    .append(enter.getKey())
                    .append("\t:\t")
                    .append(enter.getValue())
                    .append("\r\n");
        }
        return sb.toString();
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
