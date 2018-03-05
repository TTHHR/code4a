package cn.atd3.code4a.presenter;

import android.util.Log;
import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.FeedbackInterface;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.SUCCESS;

public class FeedbackPresenter {
    private FeedbackInterface fbi;
    public FeedbackPresenter(FeedbackInterface fbi)
    {
        this.fbi=fbi;
    }
    public void upToServer(final String text,final String contact)
    {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object o = Remote.report.method("send").call(fbi.getXmlString(R.string.app_name), text, contact);
                        } catch (Exception e) {
                            Log.e("send feedback",""+e);
                            fbi.showToast(ERROR,e.toString());
                            return;
                        }
                        fbi.showToast(SUCCESS,"success");
                    }
                }
        ).start();

    }
}
