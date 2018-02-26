package cn.atd3.code4a.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String encode(String pwd) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[]  bytes = digest.digest(pwd.getBytes());
            StringBuffer sb = new  StringBuffer();
            for(int i = 0;i<bytes.length;i++){
                String s = Integer.toHexString(0xff&bytes[i]);

                if(s.length()==1){
                    sb.append("0"+s);
                }else{
                    sb.append(s);
                }
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  "";
    }

}
