package cn.atd3.code4a.presenter;


import android.content.Context;
import android.util.Log;

import cn.atd3.code4a.model.model.UpdateInfo;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.AboutInterface;

public class AboutPresenter {
    private AboutInterface abi;

    public AboutPresenter(AboutInterface abi) {
        this.abi = abi;
    }

    public void onCheakUpdate(final Context con) {
        abi.showWaitDialog();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object a = Remote.androidMessage.method("update", UpdateInfo.class).call();
                            if (a instanceof UpdateInfo) {
                                String versionName = con.getPackageManager().getPackageInfo(
                                        con.getPackageName(), 0).versionName;
                                if (versionName.equals(((UpdateInfo) a).getVersion())) {
                                    abi.dismissWaitDialog();
                                } else {
                                    abi.dismissWaitDialog();
                                    Log.e("found new", "" + ((UpdateInfo) a).getVersion());
                                    abi.showUpdateInfo((UpdateInfo) a);
                                }
                            }
                        } catch (Exception e) {
                            abi.dismissWaitDialog();
                        }
                    }
                }
        ).start();
    }
}
