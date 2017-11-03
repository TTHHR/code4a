package cn.qingyuyu.commom.service;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import cn.qingyuyu.commom.SomeValue;

/**
 * Created by harrytit on 2017/11/4.
 */

public class FileDealService {
    public File saveFile(InputStream is)//暂时保存文件
    {
        return save(is);
    }
    public boolean delFile(String filePath)
    {

        File f = new File(filePath);
        if (f.exists()) {
            try {
                    f.delete();
                    return true;
            } catch (Exception e) {
                return false;
            }
        }
        return  false;
    }
    /*
    通过网址保存文件
     */
    public File saveFile(String http)//暂时保存文件
    {
        File f=null;
        try {
            // 构造URL
            URL url = new URL(http);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5000);
            f=save(con.getInputStream());
        } catch (Exception e) {
        Log.e("downicon", e.toString());
    }
    return f;
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
    private File save(InputStream is)
    {
        String name="temp";
        File f=null;

        try{
            f=new File(Environment.getExternalStorageDirectory().toString()+ SomeValue.userDir+name);
            if(f.exists())
                f.delete();
            f.createNewFile();
            FileOutputStream fos=new FileOutputStream(f);

            int len = 0;
            byte[] buffer=new byte[1024];
            while((len = is.read(buffer)) != -1)
            {
                fos.write(buffer,0,len);
            }

            is.close();
            fos.close();

        }
        catch (Exception e)
        {

        }
        return f;
    }
}
