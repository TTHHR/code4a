package cn.qingyuyu.code4a.library.fileselect;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.qingyuyu.code4a.EditArticleActivity;
import cn.qingyuyu.code4a.R;
import cn.qingyuyu.code4a.library.fileselect.ui.fragment.FileFragment;
import es.dmoral.toasty.Toasty;

/**
 * Created by harrytit on 2017/10/14.
 */

public class FileSelectActivity extends AppCompatActivity{
    Long exitTime=0l;
    private int SELECTFILE = 555;
    private int SELECTFILE_CANCLE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileselect);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        FileFragment fragment = FileFragment.newInstance("文件");
        tx.add(R.id.framlayout, fragment,"ONE");
        tx.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_selectfile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

               switch (item.getItemId()) {
                case R.id.ok:
                    Intent intent=new Intent(FileSelectActivity.this, EditArticleActivity.class);
                    FileSelectActivity.this.finish();
                         break;
                   default:
                          break;
                   }
        return super.onOptionsItemSelected(item);
          }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_MENU:
                return false;
            case KeyEvent.KEYCODE_BACK:
                if ((System.currentTimeMillis() - exitTime) > 2000)
                {
                    Toasty.warning(getApplicationContext(), getString(R.string.click_file_exit), Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                }
                else
                {
                    Intent intent=new Intent(FileSelectActivity.this, EditArticleActivity.class);
                    this.setResult(SELECTFILE_CANCLE, intent);
                    //关闭Activity
                    finish();
                }
        }
        return true;
    }
}
