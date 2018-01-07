package cn.qingyuyu.code4a

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import cn.qingyuyu.commom.SomeValue
import cn.qingyuyu.commom.service.FileDealService
import es.dmoral.toasty.Toasty
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SplashActivity : AppCompatActivity() {
var isPermission=false
    lateinit var adImage:ImageView
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

        adImage=findViewById(R.id.adView)

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
        val adImg=File(SomeValue.dirPath+SomeValue.adImg)

        if(adImg.exists())//设置开屏广告
        {
            adImage.setImageURI(Uri.fromFile(adImg))
            val cal = Calendar.getInstance()
            val time = adImg.lastModified()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            cal.timeInMillis=time
            System.out.println("修改时间 " + formatter.format(cal.time))
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
        }

        Handler().postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            //跳转到主界面
            this@SplashActivity.startActivity(intent)
            this@SplashActivity.finish()
        }, 1000)//1000毫秒后执行上面的跳转主界面语句
    }
}
