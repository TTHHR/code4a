package cn.atd3.code4a.view.view;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.presenter.ViewArticlePresenter;
import cn.atd3.code4a.view.inter.ArticleViewInterface;
import cn.carbs.android.library.MDDialog;
import es.dmoral.toasty.Toasty;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.INFO;
import static cn.atd3.code4a.Constant.NORMAL;
import static cn.atd3.code4a.Constant.SUCCESS;
import static cn.atd3.code4a.Constant.WARNING;

public class ViewArticleActivity extends AppCompatActivity implements ArticleViewInterface {
    private TextView articleText;
    private ViewArticlePresenter vap;
    private BootstrapButton copyButton, mycomment;
    private AlertDialog md;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);
        Intent i = this.getIntent();
        ArticleModel article=(ArticleModel) i.getSerializableExtra("article");


        // 固定横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        md = new AlertDialog.Builder(ViewArticleActivity.this)
                .setTitle(R.string.please_waiting)
                .setView(new ProgressBar(ViewArticleActivity.this))
                .setCancelable(false)//不可跳过
                .create();

        vap = new ViewArticlePresenter(this,this,article);//控制器



        vap.shouWaitDialog();//等待


        vap.checkArticle();//检查数据是否正常


        getSupportActionBar().setTitle(article.getTitle()== null ? "error" : article.getTitle());


        articleText = findViewById(R.id.rich_text);

        if (articleText != null) {
            vap.initImageGetter(articleText);//初始化图片加载器
        }


        copyButton = findViewById(R.id.copy);//复制按钮
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (articleText != null) {
                    ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("code", articleText.getText()));
                    Toasty.info(getApplicationContext(), getString(R.string.info_success), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        mycomment=findViewById(R.id.mycomment);
        mycomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new MDDialog.Builder(ViewArticleActivity.this)
                    .setTitle("Edit my comment")
                        .setContentView(R.layout.dialog_mycomment)
                        .setNegativeButton(R.string.button_cancel,new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                            }
                        })
                   .setPositiveButton(R.string.button_ok,new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                       }
                   })

                    .setWidthMaxDp(600)
                    .setShowTitle(true)
                    .setShowButtons(true)
                    .create()
                    .show();
            }
        });

*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            vap.onDestory(this);
            //获得文章详情保存到本地

            //数据是使用Intent返回
            Intent intent = new Intent();
            //设置返回数据
            this.setResult(vap.deleteArticle?1:0, intent);
            //关闭Activity
            this.finish();

            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    @Override
    protected void onStart() {

        vap.loadArticle();//加载文章

        super.onStart();
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_viewarticle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_downloadfile: {
                //下载附件

                new MDDialog.Builder(ViewArticleActivity.this)
                        .setMessages(vap.getDownFileList())
                        .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int index) {
                                //创建下载任务,downloadUrl就是下载链接
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(vap.getFileUrl(index)));
                                // 设置Title
                                request.setTitle(vap.getDownFileList()[index]);
                                // 设置描述
                                request.setDescription(getString(R.string.info_down)+vap.getDownFileList()[index]);
                                //默认只显示下载中通知
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                //指定下载路径和下载文件名

                                request.setDestinationInExternalPublicDir(Constant.downloadPath+"/"+vap.getArticleid()+"/", vap.getDownFileList()[index]);

//                                        runOnUiThread(
//                                                new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        Toasty.error(ViewArticleActivity.this,Constant.debugmodeinfo==true?""+e:"error",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                        );

                                    Log.e("down path",Constant.downloadPath+"/"+vap.getArticleid()+"/");
                                Log.e("file",vap.getDownFileList()[index]);
                    //获取下载管理器
                                DownloadManager downloadManager= (DownloadManager) ViewArticleActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);

                    //将下载任务加入下载队列，否则不会进行下载
                                downloadManager.enqueue(request);
                            }
                        })
                        .setWidthMaxDp(600)
                        .create()
                        .show();


                break;
            }

            case R.id.del: {
                //删除文章
                vap.deleteArticle();
                break;
            }

            case R.id.share: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, articleText.getText() + Constant.shareUrl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            }

            case R.id.edit: {
                //编辑文章
                Intent i = new Intent(this, EditArticleActivity.class);

                i.putExtra("content", vap.getContent());
                i.putExtra("create", vap.getCreate());
                i.putExtra("id", vap.getArticleid());
                startActivity(i);
                break;

            }

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showToast(final int infotype, final String info) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        switch (infotype) {
                            case SUCCESS:
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
                                Toasty.error(getApplicationContext(), Constant.debugmodeinfo ? info : getString(R.string.remote_error), Toast.LENGTH_SHORT).show();
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
                        if (!md.isShowing())
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
                        if (md.isShowing())
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
                        articleText.setText(Html.fromHtml(text, imageGetter, null));
                        copyButton.setClickable(true);
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
