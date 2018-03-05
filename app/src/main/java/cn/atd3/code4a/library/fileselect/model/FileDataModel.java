package cn.atd3.code4a.library.fileselect.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.Collections;

import cn.atd3.code4a.library.fileselect.constant.Constant;
import cn.atd3.code4a.library.fileselect.factory.FileDataType;
import cn.atd3.code4a.library.fileselect.model.domain.FileData;
import cn.atd3.code4a.library.fileselect.tool.DataComparator;
import cn.atd3.code4a.library.fileselect.tool.FileUtil;


/**
 * Created by liwei on 2017/4/27.
 */

public class FileDataModel extends DataModel<FileData> {

    private String currentPath;
    private String gotoPath;
    FileData fileData;

    public FileDataModel(Context context,Bundle bundle) {
        super(context,bundle);
    }

    @Override
    public void loadData() {

        FileDataType fileDataType = (FileDataType) args.getSerializable(Constant.FILE_DATA_TYPE);
        assert fileDataType != null;
        datas.clear();
        switch (fileDataType){
            case init:
                getSdCardFileInfo();
                break;
            case gotoPath:
                gotoPath = args.getString(Constant.GOTO_PATH);
                gotoPath(gotoPath);
                break;
            case backPath:
                currentPath = args.getString(Constant.BACK_PATH);
                backToPath(currentPath);
                break;
            case refresh:
                currentPath = args.getString(Constant.BACK_PATH);
                refreshCurrentPath(currentPath);
                break;
        }

    }

    private void refreshCurrentPath(String currentPath) {
        listFile(currentPath);
    }

    private void backToPath(String currentPath) {
        getParentFiles(currentPath);
    }

    private void getParentFiles(String currentPath) {
        File file = new File(currentPath);
        String parentPath = FileUtil.getParentPath(file.getAbsolutePath());
        listFile(parentPath);
    }

    private void gotoPath(String path) {
        listFile(path);
    }

    private void getSdCardFileInfo() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            listFile(currentPath);
        }
    }

    private void listFile(String path){
        File file = new File(path);
        File[] files = file.listFiles();

        if (files == null) return;

        for (File file1 : files) {
            if(file1.getName().startsWith(".")){
                continue;
            }
            file = file1;
            fileData = new FileData();
            fileData = new FileData();
            fileData.setName(file.getName());
            fileData.setPath(file.getAbsolutePath());
            fileData.setIsFolder(file.isDirectory());
            fileData.setLastModified(file.lastModified());
            datas.add(fileData);
        }
        Collections.sort(datas,new DataComparator());
    }
}
