package cn.dxkite.debug;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;

/**
 * 调试服务，用于程序调试
 *
 * 功能：
 *     TODO: 异常显示
 *     TODO: 功能反馈
 *     TODO: 后台上传日志
 *
 * Created by DXkite on 2018/1/15 0015.
 */

public class DebugService extends Service {

    public static final String TAG = "DebugService";

    /**
     * 上次异常信息
     */
    public static CrashInfo crashInfo = null;
    private  DebugBinder debugBinder=new DebugBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        Toast.makeText(this,"create service",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        Toast.makeText(this,"onStartCommand() executed",Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
        Toast.makeText(this,"onDestroy() executed",Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return debugBinder;
    }

    class DebugBinder extends Binder {
        public void saveException() {
            Log.d(TAG, "saveException() executed");
        }
    }
}
