package cn.atd3.code4a.presenter;

import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.model.inter.SplashAdModelInterface;
import cn.atd3.code4a.model.model.SplashAdModel;
import cn.atd3.code4a.view.inter.SplashViewInterface;
import cn.atd3.code4a.view.view.MainActivity;
import cn.qingyuyu.commom.service.FileDealService;
import cn.qingyuyu.commom.ui.SplashAd;


import static cn.atd3.code4a.Constant.ERROR;

/**
 * Created by harry on 2018/1/12.
 */

public class SplashPresenter {
    private static final String TAG="SplashPresenter";
    SplashAdModelInterface sami;
    SplashViewInterface    svi;

    private boolean isPermission=false;


    public SplashPresenter(SplashViewInterface s) {
        this.svi = s;
        sami=new SplashAdModel();
    }





    private void updateImage()
    {
        svi.onImageUpdate(sami.getImageUri());
    }

    private void setAd(Uri imguri, String url)
    {
        sami.setImageUri(imguri);
        sami.setUrl(url);
    }

    //供View层调用，用来请求广告数据
    public void requestAdInfo(){
            getAd();
    }

    //供ui层调用，用来请求权限

    public void requestPermissions(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23&&!isPermission) {

            String[] permission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//检查权限
                Log.e("请求权限","正在请求");
                activity.requestPermissions(permission,0);//请求
            }
            else
                isPermission=true;

        }
        else//不需要请求权限，直接初始化文件夹
        {
            isPermission=true;
            Log.e("请求权限","老版本，不需要请求");
            onDirInit();
        }

    }
    //供ui层调用，用来处理请求权限结果
    public void onRequestPermissionsResult(int requestCode,int[] grantResults )
    {
        if(requestCode==0&&grantResults[0] == PackageManager.PERMISSION_GRANTED)// 获取到权限，作相应处理
        {
            isPermission=true;
           onDirInit();//创建文件夹
        }

    }

    //绑定广告监听
    public void setSplashAdListener(final Activity activity, final SplashAd sad)
    {
        SplashAd.SplashAdListener sal=new SplashAd.SplashAdListener() {
            Intent intent = new Intent(activity, MainActivity.class);
            @Override
            public void onAdImageClicked() {

                intent.putExtra("url", sami.getUrl()==null?"http://blog.qingyuyu.cn/":sami.getUrl());
                sad.dismiss();
            }

            @Override
            public void doWhenAdDismiss() {
                //跳转到主界面
                activity.startActivity(intent);
                activity.finish();
            }
        };

        svi.setSplashAdListener(sal);
    }
    public void showAd(final int showtime)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isPermission)
                {
                    try {
                        Thread.sleep(500);//等待授权结束
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                svi.showAd(showtime);
            }
        }).start();

    }

    private void onDirInit()//初始化应用文件夹
    {
        File userDir= new File(Environment.getExternalStorageDirectory().toString()+ Constant.userDir);
        File zipDir= new File(Environment.getExternalStorageDirectory().toString()+ Constant.zipDir);
        if(!userDir.exists())
            try {
                userDir.mkdir();

                zipDir.mkdir();
            }catch (Exception e)
            {
                svi.showToast(ERROR,svi.getXmlString(R.string.wanning_storage));
            }
    }

    private void getAd(){//从本地和网络加载广告信息

        try {

            final File adImg=new File(Constant.adImg);//本地图片文件

            final File adUrl=new File(Constant.adUrl);//本地链接文件

            if(adImg.exists()&&adUrl.exists())//设置Uri
            {
                String u= FileDealService.getInstance().readFile(adUrl.getAbsolutePath());//读取链接
                setAd(Uri.fromFile(adImg),u);
                //通知View层改变视图
                updateImage();

                Calendar cal = Calendar.getInstance();
                long time = adImg.lastModified();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                cal.setTimeInMillis(time);
                String lastTime = formatter.format(cal.getTime());
                cal.setTimeInMillis(System.currentTimeMillis());

                if(!(formatter.format(cal.getTime()).equals(lastTime)))//ad图片老旧
                {//下载开屏广告
                    new Thread(
                          new  Runnable() {
                              @Override
                              public void run() {
                                  FileDealService fdl=FileDealService.getInstance();
                                  fdl.saveFile(Constant.adImg,Constant.remoteAdImg);//从网络保存文件
                                  fdl.saveFile(Constant.adUrl,Constant.remoteAdUrl);
                              }
                }
                ).start();

                }


            }
            else//新开线程下载广告，因为带宽问题，广告下次显示
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    FileDealService.getInstance().saveFile(adImg.getAbsolutePath(),Constant.remoteAdImg);
                    FileDealService.getInstance().saveFile(adUrl.getAbsolutePath(),Constant.remoteAdUrl);
                    }
                }).start();
            }

        } catch (Exception e) {
            Log.e(TAG,""+e);
        }finally {

        }

    }


}
