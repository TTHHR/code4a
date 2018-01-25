package cn.qingyuyu.commom.service;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by harrytit on 2017/11/4.
 */

public class FileDealService {

    public static FileDealService fdl = new FileDealService();

    public static FileDealService getInstance() {
        return fdl;
    }

    private FileDealService() {

    }

    public boolean delFile(String filePath) {//删除文件

        File f = new File(filePath);
        if (f.exists()) {
            try {
                f.delete();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
//删除文件夹
//param folderPath 文件夹完整绝对路径

    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    private boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public boolean copyFile(String oldPath, String newPath) {
        Log.e("copyFile", "old" + oldPath + "  new" + newPath);
        try {
            File newFile = new File(newPath);
            if (!newFile.exists())
                newFile.createNewFile();
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = new FileInputStream(oldPath);
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        } catch (Exception e) {
            Log.e("copyFile", e.toString());
            return false;
        }
        return true;
    }

    /*
    通过网址保存文件
     */
    public void saveFile(String filepath, String http)//暂时保存文件
    {
        try {
            // 构造URL
            URL url = new URL(http);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5000);
            save(filepath, con.getInputStream());
        } catch (Exception e) {
            Log.e("saveFile", e.toString());
        }
    }

    /*
    **读取文件
     */
    public String readFile(String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            try {
                InputStreamReader inputReader = new InputStreamReader(new FileInputStream(f));
                BufferedReader bufReader = new BufferedReader(inputReader);
                String line = "";
                line = bufReader.readLine();
                inputReader.close();
                bufReader.close();
                return line;
            } catch (Exception e) {
                Log.e("readData", e.toString());
            }
        }
        return null;
    }


    /*
    将流保存文件
     */
    private void save(String filePath, InputStream is) {
        File f = null;

        try {
            f = new File(filePath);
            if (f.exists())
                f.delete();
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            is.close();
            fos.close();

        } catch (Exception e) {
            Log.e("save inputstream", filePath + ":" + e.toString());
        }
    }

    public static boolean putContent(File fileName, String content) {
        if (!fileName.exists()) {
            if (!fileName.getParentFile().exists()) {
                fileName.mkdirs();
            }
            try {
                fileName.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }

        }
        try {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(fileName);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();
                return true;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
