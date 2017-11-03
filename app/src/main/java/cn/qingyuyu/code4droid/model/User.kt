package cn.qingyuyu.code4droid.model

import android.net.Uri
import android.util.Log
import cn.qingyuyu.commom.SomeValue

import com.alibaba.fastjson.JSONObject

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.URL

/**
 *
 * Created by Administrator on 2017\8\9 0009.
 */

class User private constructor() {

    val isLogind: Boolean
        get() {
            if (userName == null)
                return false
            else {
                return File(SomeValue.dirPath + iconName!!).exists()
            }

        }

    fun getUserName(): String? {
        return userName
    }

    val imgUri: Uri
        get() {
            val file = File(SomeValue.dirPath + iconName!!)
            return Uri.fromFile(file)
        }

    fun logout() {
        var file = File(SomeValue.userData)
        if (file.exists())
            file.delete()
        file = File(SomeValue.dirPath + iconName!!)
        if (file.exists())
            file.delete()
    }

    companion object {

        private var iconName: String? = null
        private var userName: String? = null
        internal var u = User()

        val instance: User
            get() {
                if (iconName == null && userName == null) {
                    val f = File(SomeValue.userData)
                    if (f.exists()) {
                        try {
                            val inputReader = InputStreamReader(FileInputStream(f))
                            val bufReader = BufferedReader(inputReader)
                            var line = ""
                            line = bufReader.readLine()
                            inputReader.close()
                            bufReader.close()
                            val user = JSONObject.parseObject(line)
                            iconName = user.getString("portrait")
                            userName = user.getString("uname")
                        } catch (e: Exception) {
                            Log.e("readData", e.toString())
                        }

                    }
                }
                return u
            }

        fun getInstance(userJson: String): User {
            try {
                val user = JSONObject.parseObject(userJson)
                iconName = user.getString("portrait")
                userName = user.getString("uname")
                val file = File(SomeValue.userData)
                if (file.exists())
                    file.delete()
                file.createNewFile()//新建文件
                val output = FileOutputStream(file)
                //写入文件
                output.write(userJson.toByteArray())
                output.flush()
                output.close()

                downloadIcon()
            } catch (e: Exception) {
                Log.e("getInstance", e.toString())
            }

            return u
        }

        private fun downloadIcon() {
            Thread(Runnable {
                try {
                    // 构造URL
                    val url = URL(SomeValue.imgUrl + iconName!!)
                    // 打开连接
                    val con = url.openConnection()
                    //设置请求超时为5s
                    con.connectTimeout = 5 * 1000

                    // 输出的文件流
                    val sf = File(SomeValue.dirPath + iconName!!)
                    if (!sf.exists()) {
                        sf.delete()
                    }
                    sf.createNewFile()
                    val os = FileOutputStream(sf)
                    // 开始读取
                    os.write(url.readBytes())
                    // 完毕，关闭所有链接
                    os.close()
                } catch (e: Exception) {
                    Log.e("downicon", e.toString())
                }
            }).start()
        }
    }
}
