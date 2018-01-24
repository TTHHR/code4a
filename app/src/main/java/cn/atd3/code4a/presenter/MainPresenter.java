package cn.atd3.code4a.presenter;

import android.os.Build;

import java.io.IOException;

import cn.atd3.code4a.model.model.MessageModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.MainViewInterface;
import cn.atd3.code4a.view.view.SplashActivity;
import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;

/**
 * Created by harry on 2018/1/13.
 */

public class MainPresenter {
    MainViewInterface mvi;
    MessageModel mm;
    public MainPresenter(MainViewInterface mvi)
    {
        this.mvi=mvi;
        mm=new MessageModel();
    }

    private void showMessageBanner()
    {

    }
}
