package cn.dxkite.baidusign;

import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import cn.atd3.proxy.DefaultController;



/**
 * 状态控制器
 * Created by DXkite on 2017/11/4 0004.
 */

public class SignController extends DefaultController {
    final String TAG = "cookie-controller";
    final String downloadPath="code4a/.cache/temps";
    public SignController() {
        super();
    }

    @Override
    public File saveFile(String contentType, InputStream content, long contentLength) throws IOException {
        File tempFile= super.saveFile(contentType, content, contentLength);
        MimeTypeMap  mimeTypeMap=  MimeTypeMap.getSingleton();
        String extension=mimeTypeMap.getExtensionFromMimeType(contentType);
        String savePath=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+downloadPath;
        File dir=new File(savePath);
        if (!dir.exists() && dir.mkdirs()){
            Log.i(TAG,"mkdir :"+dir.getAbsolutePath());
        }
        File saveFile=new File(dir.getAbsolutePath()+File.separator+tempFile.getName()+"."+extension);
        if(!moveFile(tempFile,saveFile)){
            Log.e(TAG,"error:move to:"+saveFile.getAbsolutePath());
            return tempFile;
        }
        return saveFile;
    }

    private boolean moveFile(File src, File dest){
        if (src.isDirectory() || dest.isDirectory()){
            return false;// 判断是否是文件
        }
        try {
            // 创建输入输出流
            FileInputStream infile = new FileInputStream(src);
            FileOutputStream outfile = new FileOutputStream(dest);
            // 复制文件
            int temp=0;
            while ((temp=infile.read())!=-1)
            {
                outfile.write(temp);
            }
            // 关闭流
            infile.close();
            outfile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if(!src.delete()){
            Log.e(TAG,"delete srcfile error:"+src.getAbsolutePath());
        }
        return true;
    }
}
