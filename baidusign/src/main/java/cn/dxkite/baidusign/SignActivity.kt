package cn.dxkite.baidusign

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var signView =findViewById(R.id.sign_view) as SignWebView
        var button =findViewById(R.id.button) as Button
        button.setOnClickListener(View.OnClickListener() {
            Log.d("SignActivity","Sign Test");
            signView.change2SignPage();
        })
    }
}
