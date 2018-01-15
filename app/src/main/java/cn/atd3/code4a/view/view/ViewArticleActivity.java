package cn.atd3.code4a.view.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import cn.atd3.code4a.R;
import cn.atd3.code4a.presenter.ViewArticlePresenter;
import cn.atd3.code4a.view.inter.ArticleViewInterface;
import es.dmoral.toasty.Toasty;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.INFO;
import static cn.atd3.code4a.Constant.NORMAL;
import static cn.atd3.code4a.Constant.SUCESS;
import static cn.atd3.code4a.Constant.WARNING;

public class ViewArticleActivity extends AppCompatActivity implements ArticleViewInterface {
    private TextView articleText;
    private ViewArticlePresenter vap;
    private BootstrapButton copyButton;
    AlertDialog md;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        vap=new ViewArticlePresenter(this);//控制器



        md = new AlertDialog.Builder(ViewArticleActivity.this)
                .setTitle(R.string.please_waiting)
                .setView(new ProgressBar(ViewArticleActivity.this))
                .setCancelable(false)//不可跳过
                .create();

        vap.shouWaitDialog();//等待


        Intent i = this.getIntent();
        int articleid = i.getIntExtra("articleid", -1);
        int userid = i.getIntExtra("userid", -1);

        vap.checkArticle(articleid,userid);//检查数据是否正常


        getSupportActionBar().setTitle(i.getStringExtra("title")==null?"error":i.getStringExtra("title"));


        articleText = findViewById(R.id.rich_text);
                Log.e("id",""+articleid);
        if(articleText != null) {
            vap.initImageGetter(articleText);//初始化图片加载器

        }



        copyButton = findViewById(R.id.copy);//复制按钮
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(articleText !=null)
                {
                    ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip( ClipData.newPlainText("code", articleText.getText()));
                    Toasty.info(getApplicationContext(),getString(R.string.info_success), Toast.LENGTH_SHORT).show();
                }
            }
        } );



    }


    @Override
    protected void onStart() {

        vap.loadArticle();//加载文章

        super.onStart();
    }

    @Override
    public void showToast(final int infotype, final String info) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        switch (infotype) {
                            case SUCESS:
                                Toasty.success(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case INFO:
                                Toasty.info(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case NORMAL:
                                Toasty.normal(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case WARNING:
                                Toasty.warning(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR:
                                Toasty.error(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                                break;
                            default:

                        }
                    }
                }
        );

    }

    @Override
    public String getXmlString(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public void showWaitDialog() {
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                if(!md.isShowing())
                                 md.show();
                            }
                        }
                );
    }

    @Override
    public void dismissWaitDialog() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if(md.isShowing())
                            md.dismiss();
                    }
                }
        );
    }

    @Override
    public void loadArticle(final String text, final Html.ImageGetter imageGetter) {

        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                            articleText.setText( Html.fromHtml(text, imageGetter, null));
                            copyButton.setClickable(true);
                            vap.dismissWaitDialog();//取消等待
                    }
                }
        );
    }

    @Override
    public void loadUser(final String un) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().setSubtitle(un);
                    }
                }
        );
    }
}
