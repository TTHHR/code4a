package cn.dxkite.baidusign;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.dxkite.common.rpc.DefaultController;

/**
 * 状态控制器
 * Created by DXkite on 2017/11/4 0004.
 */

public class SignController extends DefaultController {

    final String userCookie = "user.cookie";
    final String TAG = "cookie-controller";
    String cookiePath = null;
    Map<String, String> cookieInfo = new HashMap<String, String>();
    ;
    File cookieFile = null;

    public SignController(Context context) {
        File filesDir = context.getApplicationContext().getFilesDir();
        if (filesDir.canWrite()) {
            cookiePath = filesDir.getAbsolutePath() + File.separator + userCookie;
            cookieFile = new File(cookiePath);
            if (!cookieFile.exists()) {
                try {
                    cookieFile.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, "can't create save cookie file", e);
                }
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(cookieFile));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    Log.d(TAG, "load cookie from file:" + line);
                    setCookie(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, "can't write files directory :" + filesDir.getAbsolutePath());
        }
    }

    @Override
    public boolean saveCookie(String cookie) {
        Log.d(TAG, "save cookie:" + cookie);
        setCookie(cookie);
        FileWriter writer = null;
        try {
            writer = new FileWriter(cookieFile);
        } catch (IOException e) {
            Log.e(TAG, "can't write cookie file", e);
        }
        for (Map.Entry<String, String> cookieMap : cookieInfo.entrySet()) {
            String cookieLine = cookieMap.getKey() + "=" + cookieMap.getValue() + ";\r\n";
            try {
                writer.write(cookieLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void setCookie(String cookie) {
        String cookieStr = null;
        if (cookie.indexOf(";") > 0) {
            cookieStr = cookie.substring(0, cookie.indexOf(";"));
        } else {
            cookieStr = cookie;
        }
        int pos = cookieStr.indexOf("=");
        cookieInfo.put(cookieStr.substring(0, pos), cookieStr.substring(pos + 1));
    }

    @Override
    public String getCookies() {
        StringBuffer cookieStr = new StringBuffer();
        for (Map.Entry<String, String> cookie : cookieInfo.entrySet()) {
            cookieStr.append(cookie.getKey() + "=" + cookie.getValue() + ";");
        }
        Log.d(TAG, "get cookies:" + cookieStr);
        return cookieStr.toString();
    }
}
