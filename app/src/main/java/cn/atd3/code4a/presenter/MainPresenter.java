package cn.atd3.code4a.presenter;

import cn.atd3.code4a.model.model.MessageModel;
import cn.atd3.code4a.view.inter.MainViewInterface;

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
        if(mm.getMessge()!=null&&mm.getUrl()!=null)
            mvi.showMessageBanner(mm);
    }

}
