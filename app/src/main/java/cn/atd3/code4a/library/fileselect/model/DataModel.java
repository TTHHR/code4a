package cn.atd3.code4a.library.fileselect.model;

import android.content.Context;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/22.
 */
public abstract class DataModel<T extends BaseData> {

    protected Context context;
    protected Bundle args;

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    protected List<T> datas = new ArrayList<>();

    public DataModel(Context context, Bundle bundle) {
        this.context = context;
        this.args = bundle;
    }

    public abstract void loadData();

    public List<T> getDatas(){
        return datas;
    }
}
