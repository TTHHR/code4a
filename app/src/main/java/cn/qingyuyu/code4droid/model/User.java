package cn.qingyuyu.code4droid.model;

import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017\8\9 0009.
 */

public class User {
    private static String imgUrl = "http://tb.himg.baidu.com/sys/portrait/item/";
    private static String iconName = null;
    private static String userName = null;
    static User u = new User();

    private User() {
    }

    public static User getInstance() {
        if (iconName != null && userName != null) {
            File f = new File("/data/data/cn.qingyuyu.code4droid/user.data");
            if (f.exists()) {
                try {
                    InputStreamReader inputReader = new InputStreamReader(new FileInputStream(f));
                    BufferedReader bufReader = new BufferedReader(inputReader);
                    String line = "";
                    line = bufReader.readLine();
                    inputReader.close();
                    bufReader.close();
                    JSONObject user = JSONObject.parseObject(line);
                    iconName = user.getString("portrait");
                    userName = user.getString("uname");
                } catch (Exception e) {
                    Log.e("readData", e.toString());
                }
            }
        }
        return u;
    }

    public static User getInstance(String userJson) {
        try {
            JSONObject user = JSONObject.parseObject(userJson);
            iconName = user.getString("portrait");
            userName = user.getString("uname");
            File file = new File("/data/data/cn.qingyuyu.code4droid/user.data");
            if (file.exists())
                file.delete();
            file.createNewFile();//新建文件
            FileOutputStream output = new FileOutputStream(file);
            //写入文件
            output.write(userJson.getBytes());
            output.flush();
            output.close();

            downloadIcon();
        } catch (Exception e) {
            Log.e("getInstance", e.toString());
        }
        return u;
    }

    public boolean isLogind() {
        if (userName == null)
            return false;
        else {
            return new File("/data/data/cn.qingyuyu.code4droid/" + iconName).exists();
        }

    }

    private static void downloadIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 构造URL
                    URL url = new URL(imgUrl + iconName);
                    // 打开连接
                    URLConnection con = url.openConnection();
                    //设置请求超时为5s
                    con.setConnectTimeout(5 * 1000);
                    // 输入流
                    InputStream is = con.getInputStream();

                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len;
                    // 输出的文件流
                    File sf = new File("/data/data/cn.qingyuyu.code4droid/" + iconName);
                    if (!sf.exists()) {
                        sf.delete();
                    }
                    sf.createNewFile();
                    OutputStream os = new FileOutputStream(sf);
                    // 开始读取
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    // 完毕，关闭所有链接
                    os.close();
                    is.close();
                } catch (Exception e) {
                    Log.e("downicon", e.toString());
                }
            }
        }).start();
    }

    public String getUserName() {
        return userName;
    }

    public Uri getImgUri() {
        File file = new File("/data/data/cn.qingyuyu.code4droid/" + iconName);
        return Uri.fromFile(file);
    }

    public void logout() {
        File file = new File("/data/data/cn.qingyuyu.code4droid/user.data");
        if (file.exists())
            file.delete();
        file = new File("/data/data/cn.qingyuyu.code4droid/" + iconName);
        if (file.exists())
            file.delete();
    }
}
