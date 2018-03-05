package cn.atd3.code4a.library.fileselect.ui.fragment;

import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;


import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.atd3.code4a.R;
import cn.atd3.code4a.library.fileselect.adapter.file.FileHeaderBinder;
import cn.atd3.code4a.library.fileselect.adapter.file.FileItemBinder;
import cn.atd3.code4a.library.fileselect.constant.Constant;
import cn.atd3.code4a.library.fileselect.event.ViewEvent;
import cn.atd3.code4a.library.fileselect.factory.DataType;
import cn.atd3.code4a.library.fileselect.factory.FileDataType;
import cn.atd3.code4a.library.fileselect.loader.DataLoader;
import cn.atd3.code4a.library.fileselect.model.DataModel;
import cn.atd3.code4a.library.fileselect.model.domain.FileData;
import cn.atd3.code4a.library.fileselect.model.domain.HeaderData;
import cn.atd3.code4a.library.fileselect.tool.FileUtil;

/**
 * Created by liwei on 2017/4/28.
 */

public class FileFragment extends ListBaseFragment implements android.app.LoaderManager.LoaderCallbacks<DataModel> {

    private String argStr;
    private Context context;
    private String rootPath;

    public static FileFragment newInstance(String argStr){
        FileFragment fragment = new FileFragment();
        Bundle arg = new Bundle();
        arg.putString(Constant.FRAGMENT_AGR_KEY,argStr);
        fragment.setArguments(arg);
        return fragment;
    }

    private void parseArg(Bundle bundle){
        argStr = bundle.getString(Constant.FRAGMENT_AGR_KEY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        parseArg(this.getArguments());
        adapter.register(FileData.class, new FileItemBinder(context));
        adapter.register(HeaderData.class,new FileHeaderBinder(context));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            HeaderData headerData = new HeaderData(rootPath);
            items.add(headerData);
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.FILE_DATA_TYPE, FileDataType.init);
            initLoader(bundle);
        }else {
            Toast.makeText(context,context.getString(R.string.click_file_exit),Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewEvent event) {
        Bundle bundle = new Bundle();
        switch (event.getType()){
            case gotoFileClickPosition:
                FileData fileData = (FileData) event.getArgs().getSerializable(ViewEvent.Keys.GOTO_PATH);
                bundle.putSerializable(Constant.FILE_DATA_TYPE, FileDataType.gotoPath);
                bundle.putString(Constant.GOTO_PATH,fileData.getPath());
                items.clear();
                items.add(new HeaderData(fileData.getPath()));
                break;
            case backPath:
                HeaderData headerData = (HeaderData) event.getArgs().getSerializable(ViewEvent.Keys.BACK_PATH_HEADER);
                if(rootPath.equals(headerData.getPath())){
                    return;
                }
                bundle.putSerializable(Constant.FILE_DATA_TYPE, FileDataType.backPath);
                bundle.putString(Constant.BACK_PATH,headerData.getPath());
                String parentPath = FileUtil.getParentPath(headerData.getPath());
                items.clear();
                items.add(new HeaderData(parentPath));
                break;
        }
        initLoader(bundle);
    }

    @Override
    protected void loadData(boolean clear) {
        HeaderData headerData = (HeaderData) items.get(0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.FILE_DATA_TYPE, FileDataType.gotoPath);
        bundle.putString(Constant.GOTO_PATH,headerData.getPath());
        items.clear();
        items.add(headerData);
        initLoader(bundle);
    }

    @Override
    public void onLoadFinished(Loader<DataModel> loader, DataModel data) {
        items.addAll(data.getDatas());
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        setRefresh(false);
    }

    @Override
    public void onLoaderReset(Loader<DataModel> loader) {

    }

    @Override
    public Loader<DataModel> onCreateLoader(int id, Bundle args) {
        args.putSerializable(Constant.DATA_TYPE_KEY, DataType.fileData);
        return new DataLoader(this.getActivity(),args);
    }


    private void initLoader(Bundle bundle){
        getLoaderManager().destroyLoader(DataType.fileData.ordinal());
        getLoaderManager().initLoader(DataType.fileData.ordinal(), bundle, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
