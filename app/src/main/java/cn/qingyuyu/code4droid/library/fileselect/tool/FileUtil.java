package cn.qingyuyu.code4droid.library.fileselect.tool;

/**
 * Created by liwei on 2017/4/28.
 */

public class FileUtil {

    public static String getParentPath(String path) {
        if(path != null && path.contains("/")) {
            String parentPath = path.substring(0, path.lastIndexOf("/"));
            return parentPath.equals("")?"/":parentPath;
        } else {
            return null;
        }
    }
}
