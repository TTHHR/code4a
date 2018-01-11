package cn.qingyuyu.commom.ui;

import android.app.Activity;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.net.Uri;


import cn.dxkite.common.R;

/**
 * Created by harry on 2018/1/11.
 */

public class SplashAd extends RelativeLayout {
    //图片
    private final ImageView adImage;
    //“跳过”文字
    private final TextView skipText;

    //图片显示时间
    private static int showtime=3000;

    private SplashAdListener mListener;

    public SplashAd( Context context,  AttributeSet attrs) {
            super(context, attrs);
               // 加载布局
             LayoutInflater.from(context).inflate(R.layout.splash_ad, this);

             // 获取图片控件
              adImage =  findViewById(R.id.adImage);

                adImage.setOnClickListener(new OnClickListener() {//默认点击事件
                                               @Override
                                               public void onClick(View view) {
                                                   if(mListener!=null)
                                                       mListener.onAdImageClicked();
                                               }
                                           }
                );
        //获取文字控件
            skipText =  findViewById(R.id.skipText);

            skipText.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    }
            );


    }

    //显示广告
    public void show(final Activity a, int showtime)
    {
        adImage.setVisibility(View.VISIBLE);
        SplashAd.showtime=showtime;
        //图片显示计时
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (SplashAd.showtime>0)
                {
                    try {
                        a.runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        skipText.setText("跳过 （"+SplashAd.showtime/1000+"）");
                                    }
                                }
                        );
                        Thread.sleep(500);
                        SplashAd.showtime-=500;
                    }
                    catch (Exception e)
                    {
                        Log.e("SplashAd error:",e.toString());
                        SplashAd.showtime=0;
                    }
                }
                if(mListener!=null)
                    mListener.doWhenAdDismiss();//图片显示结束，调用方法

            }
        }).start();
    }
    public void dismiss()
    {
        SplashAd.showtime=0;
    }

    public void setAdImageURI(Uri u)
    {
        if(adImage!=null)
            adImage.setImageURI(u);
    }
    public void setAdImageDraeable(Drawable d)
    {
        if(adImage!=null)
            adImage.setImageDrawable(d);
    }
    public void setSplashAdListener(SplashAdListener sal)
    {
        mListener=sal;
    }


    public interface SplashAdListener
    {
        void onAdImageClicked();

        void doWhenAdDismiss();

    }


}
