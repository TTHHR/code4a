package cn.atd3.code4a.model.model;

import java.util.ArrayList;

/**
 * Created by harry on 2018/1/17.
 */

public class PictureListModel {
    private PictureListModel(){}
    private ArrayList<String> al=new ArrayList<>();
    private static PictureListModel flm=null;

    public static PictureListModel getIns()
    {
        if(flm==null)
            flm=new PictureListModel();

        return flm;
    }

    public void addPicture(String filepath)
    {
        al.add(filepath);
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
    public ArrayList<String> getLists()
    {
        return  al;
    }

}
