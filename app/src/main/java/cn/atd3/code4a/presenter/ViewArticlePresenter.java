package cn.atd3.code4a.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.database.ArticleDatabase;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.model.model.DownFileModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.ArticleViewInterface;
import cn.atd3.proxy.Param;
import cn.dxkite.common.ui.support.CacheableImageGetter;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.SUCCESS;
import static cn.atd3.code4a.Constant.WARNING;

/**
 * 文章显示处理器
 * Created by harry on 2018/1/15.
 */

public class ViewArticlePresenter {

    private ArticleViewInterface avi;
    private CacheableImageGetter urlImageParser;

    private String content = "";//原始文章数据
    public boolean deleteArticle = false;
    private List<DownFileModel> fileList;
    private int create = 0;//文章创建时间
    private ArticleModel article;
    private static final String TAG = "ViewArticle";
    private ArticleDatabase databasePresenter;

    public ViewArticlePresenter(Context c, ArticleViewInterface avi, ArticleModel article) {
        this.avi = avi;
        this.article = article;
        databasePresenter = new ArticleDatabase(c);
    }

    public void initImageGetter(TextView tv) {
//        urlImageParser = new URLImageParser(tv);
        urlImageParser = new CacheableImageGetter(tv, new File(Constant.getPublicFilePath(), "image_caches"));

    }

    public String getContent() {
        return content;
    }

    public int getCreate() {
        return create;
    }

    public int getArticleid() {
        return article.getId();
    }

    public ArticleModel getArticle() {
        return article;
    }

    public void shouWaitDialog() {
        avi.showWaitDialog();
    }

    public void checkArticle() {
        if (article.getId() == null || article.getUser() == null) {
            avi.showToast(ERROR, "error");
            avi.dismissWaitDialog();//错误，取消弹窗
            return;
        }

    }

    private void saveToDatabase(Context c) {
        if (databasePresenter == null)
            databasePresenter = new ArticleDatabase(c);
        if (deleteArticle) {
            databasePresenter.deleteArticle(article.getId());
        } else
            databasePresenter.saveArticle(article);
        Log.e("save base", "" + article);
    }

    public void deleteArticle() {
        /*

        为了减轻服务器压力，之后这里需要对作者进行本地检查
        现在的方法有服务器进行验证，不会误删的

         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Remote.article.method("delete").call(
                            new Param("id", article.getId())
                    );
                    avi.showToast(SUCCESS, "success");
                    deleteArticle = true;
                } catch (Exception e) {
                    avi.showToast(ERROR, "" + e);
                }

            }
        }).start();


    }

    public String[] getDownFileList() {
        ArrayList<String> al = new ArrayList<>();
        if (fileList != null && fileList.size() != 0)
            for (DownFileModel d : fileList)
                al.add(d.getName());
        else
            return new String[]{};
        return al.toArray(new String[al.size()]);
    }

    public String getFileUrl(int p) {
        return fileList.get(p).getUrl();
    }


    public void onDestory(Context c) {
        saveToDatabase(c);
        fileList = null;
        article = null;
        databasePresenter = null;
    }


    public void loadArticle() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Object a = Remote.article.method("getArticleById", ArticleModel.class).call(article.getId());
                            if (a instanceof ArticleModel) {
                                Log.i("obj", "is article");
                                if (((ArticleModel) a).getContent() != null) {
                                    article = (ArticleModel) a;
                                    content = article.getContent();
                                    Log.d(TAG, "article abstract = " + ((ArticleModel) a).getAbstract());
                                    Log.d(TAG, "article content = " + content);
                                    Set<String> imgSet = getImgStr(content);
                                    for (String imgurl : imgSet) {
                                        Log.e("img", "" + imgurl);
                                        content = content.replace(imgurl, Constant.serverAddress + imgurl);//地址转换成绝对地址
                                    }
                                    Log.e("final", content);
                                    article.setContent(content);
                                } else {
                                    Log.e("obj", "null");
                                    content = "";
                                }
                            } else//服务器已删除这篇文章
                            {
                                deleteArticle = true;
                                avi.showToast(WARNING, avi.getXmlString(R.string.no_article_error));
                            }
                        } catch (Exception e) {
                            Log.e("net error", "" + e);
                            ArticleModel am = databasePresenter.getArticle(article.getId());
                            if (am != null) {
                                article = am;
                                content = article.getContent();
                                avi.showToast(SUCCESS, avi.getXmlString(R.string.error_network_use_local));
                            } else {
                                Log.e("local", "article not cache");
                                content = "error";
                                avi.showToast(WARNING, avi.getXmlString(R.string.error_network));
                            }
                        }
                        avi.loadUser(article.getUser().getName());//UI加载用户名
                        avi.loadArticle(content, urlImageParser);//显示文章

                        try {
                            Object a = Remote.attachment.method("getAttachment", DownFileModel.class).call(article.getId());
                            if (a instanceof List) {
                                fileList = (List<DownFileModel>) a;
                            }
                        } catch (Exception e) {
                            Log.e("get Attach", "" + e);
                        } finally {
                            avi.dismissWaitDialog();//取消等待
                        }

                    }
                }

        ).start();

    }

    /**
     * 得到网页中图片的地址
     *
     * @param htmlStr 文本
     */
    private Set<String> getImgStr(String htmlStr) {
        HashSet pics = new HashSet<String>();
        String img;
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                if (!m.group(1).startsWith("http:"))//图片为完整网址就跳过
                    pics.add(m.group(1));
            }
        }
        return pics;
    }

    class URLImageParser implements Html.ImageGetter {
        private TextView mTextView;

        private URLImageParser(TextView mTextView) {
            this.mTextView = mTextView;
        }


        @Override
        public Drawable getDrawable(String s) {
            final URLDrawable urlDrawable = new URLDrawable();
            ImageLoader.getInstance().loadImage(s,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            urlDrawable.drawBitmap = loadedImage;
                            urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                            mTextView.invalidate();
                            mTextView.setText(mTextView.getText());
                        }
                    });
            return urlDrawable;
        }
    }


    class URLDrawable extends BitmapDrawable {
        Bitmap drawBitmap = null;

        @Override
        public void draw(Canvas canvas) {
            if (drawBitmap != null) {
                canvas.drawBitmap(drawBitmap, 0f, 0f, getPaint());
            }
        }

    }

}
