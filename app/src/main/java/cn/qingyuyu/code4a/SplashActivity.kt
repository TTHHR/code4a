package cn.qingyuyu.code4a

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log

import android.view.View
import android.widget.Toast
import cn.qingyuyu.commom.SomeValue

import cn.qingyuyu.commom.ui.SplashAd

import cn.qingyuyu.commom.service.FileDealService
import es.dmoral.toasty.Toasty
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SplashActivity : AppCompatActivity() {
var isPermission=false
    lateinit var adView:SplashAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 全屏
        if (actionBar != null){
            actionBar.hide()
        }
        val mContentView:View = findViewById(R.id.splash_view)
        mContentView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        adView=findViewById(R.id.splash_ad)

        if (Build.VERSION.SDK_INT >= 23&&!isPermission) {
            val permissions = ArrayList<String>()
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (permissions.size === 0) {
                goNext()
                return
            } else {
                requestPermissions(permissions.toArray(arrayOfNulls<String>(permissions.size)), 0)
            }
        }
        else
        {
           goNext()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

        // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            0 ->

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // 获取到权限，作相应处理（）


                    val userDir= File(Environment.getExternalStorageDirectory().toString()+ SomeValue.userDir)
                    val zipDir= File(Environment.getExternalStorageDirectory().toString()+ SomeValue.userDir+SomeValue.zipDir)
                    if(!userDir.exists())
                        try {
                            userDir.mkdir()
                            zipDir.mkdir()
                        }catch (e:Exception)
                        {
                            Toasty.warning(this@SplashActivity,getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()
                        }


                  goNext()



                } else {
                    Toasty.warning(this@SplashActivity,getString(R.string.wanning_storage), Toast.LENGTH_SHORT).show()

                }

            else -> {

            }
        }
    }

    private fun goNext()
    {

        adView.setSplashAdListener(object :SplashAd.SplashAdListener{
            var  clicked=false
            override fun doWhenAdDismiss() {//广告结束 ，跳转activity

                Log.e("dismiss","clicked="+clicked)

                val intent = Intent(this@SplashActivity, MainActivity::class.java)

                if (clicked) {
                    intent.putExtra("url", "http://blog.qingyuyu.cn/")
                    Log.e("puturl","")
                }
                //跳转到主界面
                this@SplashActivity.startActivity(intent)
                this@SplashActivity.finish()
            }

            override fun onAdImageClicked() {
                clicked=true
                Log.e("click","clicked="+clicked)
            }

        })

        val adImg=File(SomeValue.dirPath+SomeValue.adImg)



        if(adImg.exists())//设置开屏广告
        {
            adView.setAdImageURI(Uri.fromFile(adImg))
            adView.show(this@SplashActivity,3000)//显示3秒
            val cal = Calendar.getInstance()
            val time = adImg.lastModified()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            cal.timeInMillis=time

           val lastTime=formatter.format(cal.time)

            cal.timeInMillis=System.currentTimeMillis()

            if(formatter.format(cal.time)>lastTime)//ad图片老旧
            {//下载开屏广告
                Thread(
                        Runnable {
                            val fdl=FileDealService.getInstance()
                            val nfile=fdl.saveFile(SomeValue.remoteAdImg)//从网络保存文件
                            fdl.copyFile(nfile.absolutePath,adImg.absolutePath)//将保存的文件复制到adimg
                            fdl.delFile(nfile.absolutePath)
                        }
                ).start()

            }
        }
        else//下载开屏广告
        {
            Thread(
                    Runnable {
                        val fdl=FileDealService.getInstance()
                        val nfile=fdl.saveFile(SomeValue.remoteAdImg)//从网络保存文件
                        fdl.copyFile(nfile.absolutePath,adImg.absolutePath)//将保存的文件复制到adimg
                        fdl.delFile(nfile.absolutePath)
                    }
            ).start()
            adView.show(this@SplashActivity,1000)//显示空白1秒
        }


    }
}
