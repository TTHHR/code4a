package cn.qingyuyu.code4droid.model;

import android.net.Uri;

import java.io.File;

import cn.qingyuyu.commom.SomeValue;

/**
 * Created by harrytit on 2017/11/4.
 */

public class Userr {
    private static   String iconName = null;
    private static String userName = null;

    private static Userr u=new Userr();


    private Userr()
    {

    }


    public static Userr get()
    {
//        if (iconName == null && userName == null) {
//            val f =new File(SomeValue.userData);
//            if (f.exists()) {
//                try {
//                    val inputReader = InputStreamReader(FileInputStream(f))
//                    val bufReader = BufferedReader(inputReader)
//                    var line = ""
//                    line = bufReader.readLine()
//                    inputReader.close()
//                    bufReader.close()
//                    val user = JSONObject.parseObject(line)
//                    iconName = user.getString("portrait")
//                    userName = user.getString("uname")
//                } catch (e: Exception) {
//                    Log.e("readData", e.toString())
//                }
//
//            }
//        }
        return u;
    }











    public boolean isLogind(){

        return userName != null;


    }
    public String getUserName(){
        return userName;
    }
    public Uri getimgUri() {
        File file = new File(SomeValue.dirPath + iconName);
        return Uri.fromFile(file);
    }


}
