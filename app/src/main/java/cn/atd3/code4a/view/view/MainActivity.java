package cn.atd3.code4a.view.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.atd3.code4a.Constant;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        if (i.getStringExtra("url") != null) {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("url", i.getStringExtra("url"));
            startActivity(intent);
        }

        if (Constant.isDebug()) {
            Toast.makeText(getApplicationContext(), "Main is debug", Toast.LENGTH_SHORT).show();
            testExcepion();
        }else{
            Toast.makeText(getApplicationContext(), "Main is release", Toast.LENGTH_SHORT).show();
        }



    }

    void testExcepion() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Integer a = 0;
                        a = null;
                        a.byteValue();
                    }
                }
        ).start();
    }
}
