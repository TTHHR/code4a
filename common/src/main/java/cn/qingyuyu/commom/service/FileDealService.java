package cn.qingyuyu.commom.service;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.qingyuyu.commom.SomeValue;

/**
 * Created by harrytit on 2017/11/4.
 */

public class FileDealService {
    public File saveFile(InputStream is)//暂时保存文件
    {
        return save(is);
    }






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
