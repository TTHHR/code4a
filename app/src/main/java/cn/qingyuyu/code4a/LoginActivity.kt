package cn.qingyuyu.code4a


import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.TextInputLayout

import android.view.View

import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.TextView
import android.widget.Toast

import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import es.dmoral.toasty.Toasty
import java.util.regex.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    var accountString=""
    var emailString=""  //for register
    var password=""
    var passwordConfirm=""

    lateinit var account:BootstrapEditText

    lateinit var passwd:BootstrapEditText

    lateinit var passwdConfirm:BootstrapEditText
    lateinit var debuginfo:BootstrapEditText
    lateinit var confirmLayout:TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.

         account=findViewById(R.id.account)

         passwd=findViewById(R.id.password)

         passwdConfirm=findViewById(R.id.password_confirm)

        debuginfo=findViewById(R.id.debug)

        confirmLayout=findViewById<TextInputLayout>(R.id.confirm_layout)

        val loginButton=findViewById<BootstrapButton>(R.id.login_in_button)

        val signinButton=findViewById<BootstrapButton>(R.id.sign_in_button)

        val buttonListener=LoginButtonClickListener()

        loginButton.setOnClickListener(buttonListener)

        signinButton.setOnClickListener(buttonListener)
    }

    inner class LoginButtonClickListener : View.OnClickListener
    {
        override fun onClick(v: View?) {

            accountString=account.text.toString()

            password=passwd.text.toString()

            passwordConfirm=passwdConfirm.text.toString()

            if(v!=null) {
                val infoLayout=layoutInflater.inflate(R.layout.dialog_uploadarticle,null)

                val messages=infoLayout.findViewById<TextView>(R.id.message)

                val md= AlertDialog.Builder(this@LoginActivity)
                        .setView(infoLayout)
                        .setCancelable(true)
                val hd=object:Handler(){
                    override fun handleMessage(msg: Message) {
                        val s = msg.what
                        Log.e("what",""+s)
                        when(s)
                        {
                           1->Toasty.info(this@LoginActivity,getString(R.string.info_success),Toast.LENGTH_SHORT).show()
                            2-> {
                                Toasty.info(this@LoginActivity, getString(R.string.info_fail), Toast.LENGTH_SHORT).show()
                                if(msg.obj is String)
                                debuginfo.setText(msg.obj.toString())
                            }
                        }

                        super.handleMessage(msg)
                    }
                }
                when (v.id) {
                    R.id.login_in_button -> {
                        if (accountString.isEmpty() || password.isEmpty())
                            Toasty.error(this@LoginActivity, getString(R.string.prompt_form_check), Toast.LENGTH_SHORT).show()
                        else {
                                md.setTitle(getString(R.string.action_login_in))
                                md.create()
                                md.show()
                        }
                    }
                    R.id.sign_in_button -> {
                        if (accountString.isEmpty() || password.isEmpty())
                            Toasty.error(this@LoginActivity, getString(R.string.prompt_form_check), Toast.LENGTH_SHORT).show()
                        else if (passwordConfirm.isEmpty()) {
                            Toasty.error(this@LoginActivity, getString(R.string.need_password_confirm), Toast.LENGTH_SHORT).show()
                            confirmLayout.visibility=View.VISIBLE
                        }
                        else if(passwordConfirm!=password)
                            Toasty.error(this@LoginActivity, getString(R.string.check_confirm_fail), Toast.LENGTH_SHORT).show()
                        else if(emailString.isEmpty())
                        {
                            val registerLayout=layoutInflater.inflate(R.layout.dialog_register,null)

                            val registerInfo=registerLayout.findViewById<TextView>(R.id.registerinfo)

                            val registerdialog= AlertDialog.Builder(this@LoginActivity)
                                    .setView(registerInfo)
                                    .setNegativeButton(getString(R.string.button_cancel),null)
                                    .setPositiveButton(getString(R.string.button_ok),DialogInterface.OnClickListener { dialogInterface, i ->

                                        //注册操作

                                    })
                                    .setCancelable(true)
                            val regex = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}"

                            if(Pattern.matches(regex,accountString))//如果账户是邮箱
                            {
                                registerdialog.setTitle(getString(R.string.need_nickname))
                            }
                            else
                            {
                                registerdialog.setTitle(getString(R.string.need_email))
                            }
                        }
                        else {
                            md.setTitle(getString(R.string.action_sign_in))
                            md.create()
                            md.show()
                        }
                    }
                }
            }
        }

    }

}
