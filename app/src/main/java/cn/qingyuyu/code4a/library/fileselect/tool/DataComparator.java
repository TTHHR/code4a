package cn.qingyuyu.code4a.library.fileselect.tool;


import cn.qingyuyu.code4a.library.fileselect.model.domain.FileData;

/**
 * Created by liwei on 2015/10/20.
 */
public class DataComparator implements java.util.Comparator<FileData> {

    private int sortType(FileData first, FileData second) {
        int result;
        result = first.getName().compareToIgnoreCase(second.getName());
        return result;
    }

    @Override
    public int compare(FileData lhs, FileData rhs) {
        if (lhs == null || rhs == null) {
            return 0;
        }

        int cr;
        if (rhs.isFolder() && (!lhs.isFolder())) {
            cr = 3;
        } else if ((!rhs.isFolder()) && lhs.isFolder()) {
            cr = -1;
        } else {
            cr = sortType(lhs, rhs);
        }

        return cr;
    }
}
