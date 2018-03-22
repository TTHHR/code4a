package cn.atd3.code4a.view.view;

import android.os.Bundle;
import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.UpdateInfo;
import cn.atd3.code4a.view.inter.UpdateCheckInterface;

public class UpdateCheckActivity extends Activity implements UpdateCheckInterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_check);
    }

    @Override
    public void noticeUpdateIcon() {

    }

    @Override
    public void noticeUpdateInfo(@NotNull UpdateInfo version) {

    }
}
