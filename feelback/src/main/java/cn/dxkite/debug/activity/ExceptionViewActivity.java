package cn.dxkite.debug.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import cn.dxkite.debug.DebugManager;
import cn.dxkite.debug.adapter.ExceptionViewListAdapter;
import cn.dxkite.debug.R;

public class ExceptionViewActivity extends AppCompatActivity {
    ExpandableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=(ExpandableListView) findViewById(R.id.list);
        listView.setAdapter(new ExceptionViewListAdapter(DebugManager.getCrashInfo(),getLayoutInflater()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            Toast.makeText(this,"日志保存成功 ",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"上传还没弄 ",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
