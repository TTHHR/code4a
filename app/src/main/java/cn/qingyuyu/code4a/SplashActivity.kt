package cn.qingyuyu.code4a

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.widget.Toast
import cn.qingyuyu.commom.SomeValue
import es.dmoral.toasty.Toasty
import java.io.File


class SplashActivity : AppCompatActivity() {
var isPermission=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
        Handler().postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            //跳转到主界面
            this@SplashActivity.startActivity(intent)
            this@SplashActivity.finish()
        }, 1000)//1000毫秒后执行上面的跳转主界面语句
    }
}
