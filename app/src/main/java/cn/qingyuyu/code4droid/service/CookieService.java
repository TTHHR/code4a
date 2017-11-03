package cn.qingyuyu.code4droid.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import cn.qingyuyu.code4droid.SomeValue;

/**
 * Created by harrytit on 2017/11/2.
 */

public class CookieService {
    public boolean saveCookie(String cookies) {
        File f = new File(SomeValue.userCookie);
        try {
            if (!f.exists())
                f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(cookies.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCookieByName(String name) {
        File f = new File(SomeValue.userCookie);
        String cookies;
        if (f.exists()) {
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(f));
                BufferedReader br = new BufferedReader(isr);
                cookies = br.readLine();
                String[] cookiestr = cookies.split(";");
                for (String ctr : cookiestr) {
                    if (name.equals(ctr.substring(0, ctr.indexOf("=")))) {
                        return ctr.substring(ctr.indexOf("=") + 1);
                    }

                }
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
}
