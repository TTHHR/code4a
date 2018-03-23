package cn.atd3.code4a.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.model.model.AdInfo;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.MainViewInterface;
import cn.dxkite.common.ui.notification.popbanner.Adapter;
import cn.dxkite.common.ui.notification.popbanner.Information;
import cn.qingyuyu.commom.service.FileDealService;

/**
 * Created by harry on 2018/1/13.
 */

public class MainPresenter {
    MainViewInterface mvi;

    public MainPresenter(MainViewInterface mvi) {
        this.mvi = mvi;
    }

    public void showMessageBanner() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Object msg = Remote.androidMessage.method("pull", Information.class).call();
                    if (msg instanceof Information) {
                        File f = new File(Constant.getPrivateFilePath() + "/message");
                        String create = FileDealService.getInstance().readFile(f.getAbsolutePath());
                        if (create == null) {
                            Adapter a = new Adapter() {
                                @Override
                                public Information refresh() {
                                    return (Information) msg;
                                }
                            };
                            mvi.showMessageBanner(a);
                            FileDealService.putContent(f, "" + ((Information) msg).getCreate());
                        } else {
                            if (Integer.parseInt(create) < ((Information) msg).getCreate()) {
                                Adapter a = new Adapter() {
                                    @Override
                                    public Information refresh() {
                                        return (Information) msg;
                                    }
                                };
                                mvi.showMessageBanner(a);
                            }
                        }

                    }
                } catch (Exception e) {
                    Log.e("get message", "" + e);
                }
            }
        }).start();

    }

    public void collection(Context c) {
        if (!Constant.collectioninfo)
            return;
        final String deviceId = Constant.getUuid();
        final String deviceName = Build.MODEL;
        final String packageName = c.getPackageName();
        final String activityName = "MainActivity";
        new Thread(new Runnable() {
            @Override
            public void run() {
                // name = "collectingInfo"
                try {
                    // TDDO: 获取驱动ID
                    Remote.collection.method("android").call(deviceName, deviceId, packageName, activityName);
                } catch (Exception e) {
                    Log.e("collection", "" + e);
                }
            }
        }).start();
    }

    public void updateAd() {
        try {

            File adImg = new File(Constant.getAdImg());//本地图片文件

            File adUrl = new File(Constant.getAdUrl());//本地链接文件

            if (adImg.exists() && adUrl.exists())//文件存在
            {

                Calendar cal = Calendar.getInstance();
                long time = adImg.lastModified();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                cal.setTimeInMillis(time);
                String lastTime = formatter.format(cal.getTime());
                cal.setTimeInMillis(System.currentTimeMillis());
                if (!(formatter.format(cal.getTime()).equals(lastTime)))//ad图片老旧
                {//下载开屏广告
                    downAd();
                }
            } else {
                downAd();
            }
        } catch (Exception e) {
            Log.e("updateAd", e.toString());
        }


    }

    private void downAd() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object msg = Remote.androidMessage.method("pullAd", AdInfo.class).call();
                            if (msg instanceof AdInfo) {
                                FileDealService.getInstance().saveFile(Constant.getAdImg(), ((AdInfo) msg).getImage());
                                FileDealService.putContent(new File(Constant.getAdUrl()), ((AdInfo) msg).getUrl());
                            }
                        } catch (Exception e) {
                            Log.e("downAd", "" + e);
                        }
                    }
                }
        ).start();

    }
}
