package cn.atd3.code4a.model.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by harry on 2018/1/17.
 */

public class FileListModel {
    private FileListModel(){}
    private ArrayList<String> al=new ArrayList<>();
    private static FileListModel flm=null;

    public static FileListModel  getIns()
    {
        if(flm==null)
            flm=new FileListModel();

        return flm;
    }

    public void addFile(String filepath)
    {
        al.add(filepath);
    }

    public void removeFile(int index)
    {
        if(al.size()<=index)
        {
            return;
        }
        else
            al.remove(index);
    }
    public void clear()
    {
        if(al!=null)
            al.clear();
    }
    public String get(int index)
    {
        if(al.size()<=index)
            return null;
        else
            return al.get(index);
    }
    public String[] toArray()
    {
        return  al.toArray(new String[al.size()]);

    }

}
