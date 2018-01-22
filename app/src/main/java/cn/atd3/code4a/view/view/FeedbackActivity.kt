package cn.atd3.code4a.view.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.atd3.code4a.R
import cn.atd3.code4a.view.inter.FeedbackInterface

class FeedbackActivity : AppCompatActivity() ,FeedbackInterface{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
    }
}
