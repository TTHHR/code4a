package cn.atd3.code4a.view.view;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.presenter.ViewArticlePresenter;
import cn.atd3.code4a.view.inter.ArticleViewInterface;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.INFO;
import static cn.atd3.code4a.Constant.NORMAL;
import static cn.atd3.code4a.Constant.SUCCESS;
import static cn.atd3.code4a.Constant.WARNING;

public class ViewArticleActivity extends AppCompatActivity implements ArticleViewInterface {
    private TextView articleText;
    private ViewArticlePresenter vap;
    private BootstrapButton copyButton, mycomment;
    private QMUITipDialog md;
    private QMUITopBarLayout mTopBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);
        Intent i = this.getIntent();
        ArticleModel article = (ArticleModel) i.getSerializableExtra("article");


        // 固定横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        md = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.info_loading))
                .create();

        vap = new ViewArticlePresenter(this, this, article);//控制器


        vap.shouWaitDialog();//等待


        vap.checkArticle();//检查数据是否正常

        mTopBar=findViewById(R.id.topbar);
        mTopBar.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTopBar.setTitle(article.getTitle() == null ? "error" : article.getTitle());

        // 菜单按钮
        mTopBar.addRightImageButton(R.mipmap.topbar_menu,1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetList();
            }
        });

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
                    new QMUITipDialog.Builder(getApplicationContext())
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                            .setTipWord(getString(R.string.button_ok))
                            .create()
                            .show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        vap.onDestory(this);
        //获得文章详情保存到本地

        //数据是使用Intent返回
        Intent intent = new Intent();
        //设置返回数据
        this.setResult(vap.deleteArticle ? 1 : 0, intent);
        //关闭Activity
        this.finish();

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
                       final  QMUITipDialog tipDialog ;
                        switch (infotype) {
                            case SUCCESS:
                                tipDialog = new QMUITipDialog.Builder(ViewArticleActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case INFO:
                                tipDialog = new QMUITipDialog.Builder(ViewArticleActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case NORMAL:
                                tipDialog = new QMUITipDialog.Builder(ViewArticleActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case WARNING:
                                tipDialog = new QMUITipDialog.Builder(ViewArticleActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case ERROR:
                                tipDialog =  new QMUITipDialog.Builder(ViewArticleActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord(Constant.debugmodeinfo?info:getString(R.string.remote_error))
                                        .create();
                                break;
                                default:
                                    tipDialog = new QMUITipDialog.Builder(ViewArticleActivity.this)
                                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                                            .setTipWord(info)
                                            .create();
                        }
                            tipDialog.show();
                            new Thread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            finally {
                                                tipDialog.dismiss();
                                            }

                                        }
                                    }
                            ).start();
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
                     mTopBar.setSubTitle(un);
                    }
                }
        );
    }
    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(getString(R.string.nav_share))
                .addItem(getString(R.string.button_edit))
                .addItem(getString(R.string.download_file))
                .addItem(getString(R.string.button_del))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, articleText.getText() + Constant.shareUrl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                                break;
                            case 1:
                                //编辑文章
                Intent i = new Intent(ViewArticleActivity.this, EditArticleActivity.class);

                i.putExtra("article", vap.getArticle());
                startActivity(i);
                                break;
                            case 2:
                final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(ViewArticleActivity.this)
                        .addItems(vap.getDownFileList(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.addAction(getString(R.string.button_cancel), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });
                builder.addAction(getString(R.string.button_ok), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                        for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                            //创建下载任务,downloadUrl就是下载链接
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(vap.getFileUrl(i)));
                            // 设置Title
                            request.setTitle(vap.getDownFileList()[i]);
                            // 设置描述
                            request.setDescription(getString(R.string.info_down) + vap.getDownFileList()[i]);
                            //默认只显示下载中通知
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            //指定下载路径和下载文件名

                            request.setDestinationInExternalPublicDir(Constant.downloadPath + "/" + vap.getArticleid() + "/", vap.getDownFileList()[i]);
                            Log.e("down path", Constant.downloadPath + "/" + vap.getArticleid() + "/");
                            Log.e("file", vap.getDownFileList()[i]);
                            //获取下载管理器
                            DownloadManager downloadManager = (DownloadManager) ViewArticleActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);

                            //将下载任务加入下载队列，否则不会进行下载
                            downloadManager.enqueue(request);
                        }

                        dialog.dismiss();
                    }
                });
                builder.show();
                                break;

                            case 3:
                                //删除文章
                vap.deleteArticle();
                                break;

                        }
                    }
                })
                .build()
                .show();
    }
}
