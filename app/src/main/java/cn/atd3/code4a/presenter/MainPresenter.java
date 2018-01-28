package cn.atd3.code4a.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.MainViewInterface;
import cn.dxkite.common.ui.notification.popbanner.Adapter;
import cn.dxkite.common.ui.notification.popbanner.Information;

/**
 * Created by harry on 2018/1/13.
 */

public class MainPresenter {
    MainViewInterface mvi;
    public MainPresenter(MainViewInterface mvi)
    {
        this.mvi=mvi;
    }
    public void showMessageBanner()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Object msg = Remote.androidMessage.method("pull", Information.class).call();
                    if (msg instanceof Information){
                        Adapter a=new Adapter() {
                            @Override
                            public Information refresh() {
                                return (Information) msg;
                            }
                        };
                       mvi.showMessageBanner(a);
                    }
                } catch (Exception e) {
                    Log.e("get message",""+e);
                }
            }
        }).start();

    }
    public void collection(Context c)
    {
        if(!Constant.collectioninfo)
            return;
        final String deviceId = Constant.getUuid();
        final String deviceName = Build.MODEL;
        final String packageName =c.getPackageName();
        final String activityName = "MainActivity";
        new Thread(new Runnable() {
            @Override
            public void run() {
               // name = "collectingInfo"
                try {
                    // TDDO: 获取驱动ID
                    Remote.collection.method("android").call(deviceName, deviceId, packageName, activityName);
                } catch (Exception e) {
                    Log.e("collection",""+e);
                }
            }
        }).start();
    }
}
