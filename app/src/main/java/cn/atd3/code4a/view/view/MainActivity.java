package cn.atd3.code4a.view.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i=getIntent();

        if(i.getStringExtra("url")!=null)
        {
            Intent intent=new Intent(this,WebActivity.class);
            intent.putExtra("url",i.getStringExtra("url"));
            startActivity(intent);
        }

    }
}
