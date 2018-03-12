package cn.atd3.code4a;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;


/**
 * 系统定义的常量
 * Created by Administrator on 2017\8\8 0008.
 */

public final class Constant {

    public final static String serverAddress = "http://code4a.atd3.cn/";
    public final static String shareUrl = "http://github.com/tthhr/code4a";
    public final static String donationUrl = "https://github.com/TTHHR/code4a/blob/master/docs/assets/donation.jpg";
    public final static String softwareInfoUrl = "https://github.com/TTHHR/code4a/blob/master/README.md";
    public final static String tiebaUrl = "http://tieba.baidu.com/f?kw=c4droid";
    public final static String categoryListFile = "categories";
    //用户
    public final static String avatar="http://code4a.atd3.cn/user/avatar/";
    public final static String codeImage="http://code4a.atd3.cn/user/verify";
    public final static String resetPassword="http://code4a.atd3.cn/user/reset_password";

    public final static int SUCCESS = 0;
    public final static int INFO = 1;
    public final static int NORMAL = 2;
    public final static int WARNING = 3;
    public final static int ERROR = 4;
    private final static String sdcardName = File.separator + "code4a";
    private final static String privateString = File.separator + ".private";
    public final static String zipDir = File.separator + "zip";
    public final static String zipFile = File.separator + "code.zip";
    private static boolean init = false;
    private static String privateFilePath;
    private static String publicFilePath;

    public static String downloadPath="/code4a/download";
    private static String cachePath=File.separator+"cache";
    private static String adImg = null;
    private static String adUrl = File.separator + "adUrl.txt";
    private static String userData = File.separator + "user.data";
    public static String settingFile = File.separator + "setting.xml";

    public static boolean debugmodeinfo = false;
    public static boolean collectioninfo = false;
    public static String themeColor = "#99cc33";
    public static String defaultThemeColor = "#99cc33";

    private static boolean debug;
    private static String uuid;

    private Constant(Context context) {
        Constant.debug = judgeDebug(context);
        Constant.privateFilePath = context.getFilesDir().getAbsolutePath();
        Constant.publicFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + sdcardName;
        if (debug) {
            Constant.privateFilePath = Constant.publicFilePath + privateString;
        }
        Log.e("privateFilePath", privateFilePath);
        adImg = privateFilePath + File.separator + "adImg.png";
        adUrl = privateFilePath + File.separator + "adUrl.txt";
        userData = privateFilePath + File.separator + "user.data";
        cachePath=publicFilePath+cachePath;
        settingFile=privateFilePath+settingFile;
        init = true;
    }

    private boolean judgeDebug(Context context) {
        return context.getApplicationInfo() != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static void init(Context context) {
        if (!init) {
            new Constant(context);
        }
    }

    public static String getCachePath(){return  cachePath;}

    public static String getPrivateFilePath() {
        return privateFilePath;
    }

    public static String getPublicFilePath() {
        return publicFilePath;
    }

    public static String getAdImg() {
        return adImg;
    }

    public static String getAdUrl() {
        return adUrl;
    }

    public static String getUserData() {
        return userData;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String getCategoryListFilePath() {
        return getPrivateFilePath() + File.separator + categoryListFile;
    }

    public static String getUuid() {
        return uuid;
    }

    public static void setUuid(String uuid) {
        Constant.uuid = uuid;
    }
}
