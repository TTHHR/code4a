package cn.atd3.code4a.presenter;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.model.model.DownFileModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.ArticleViewInterface;
import cn.atd3.proxy.Param;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.SUCCESS;

/**
 * Created by harry on 2018/1/15.
 */

public class ViewArticlePresenter {

    private ArticleViewInterface avi;
    private URLImageParser urlImageParser;

    private String content = "";//原始文章数据

    private List<DownFileModel> fileList;
    private int create = 0;//文章创建时间
    private int articleid = -1;
    private int userid = -1;
    private static final String TAG = "ViewArticle";

    public ViewArticlePresenter(ArticleViewInterface avi) {
        this.avi = avi;
    }

    public void initImageGetter(TextView tv) {

        urlImageParser = new URLImageParser(tv);


    }

    public String getContent() {
        return content;
    }

    public int getCreate() {
        return create;
    }

    public int getArticleid() {
        return articleid;
    }

    public void shouWaitDialog() {
        avi.showWaitDialog();
    }

    public void checkArticle(int articleid, int userid) {
        if (articleid == -1 || userid == -1) {
            avi.showToast(ERROR, "error");
            avi.dismissWaitDialog();//错误，取消弹窗
            return;
        }
        this.articleid = articleid;
        this.userid = userid;
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
                            new Param("id", articleid)
                    );
                } catch (Exception e) {
                    avi.showToast(ERROR, "" + e);
                }
                avi.showToast(SUCCESS, "success");
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


    public void loadArticle() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String usern = "";
                        try {
                            Object username = Remote.user.method("id2name", String.class).call(userid);
                            if (username instanceof String) {
                                usern = (String) username;
                            } else {
                                usern = "error";
                            }

                            Log.e("username", "" + username);
                        } catch (Exception e) {
                            Log.e("userid", e.toString());
                        }

                        avi.loadUser(usern);//UI加载用户名

                        try {
                            Object a = Remote.article.method("getArticleById", ArticleModel.class).call(articleid);
                            if (a instanceof ArticleModel) {
                                Log.i("obj", "is article");
                                if (((ArticleModel) a).getContent() != null) {

                                    create = ((ArticleModel) a).getCreate();
                                    content = ((ArticleModel) a).getContent();
                                    Log.d(TAG, "article abstract = " + ((ArticleModel) a).getAbstract());
                                    Log.d(TAG, "article content = " + content);
                                    Set<String> imgSet = getImgStr(content);
                                    for (String imgurl : imgSet) {
                                        Log.e("img", "" + imgurl);
                                        content = content.replace(imgurl, Constant.serverAddress + imgurl);//地址转换成绝对地址
                                    }
                                    Log.e("final", content);
                                } else {
                                    Log.e("obj", "null");
                                    content = "";
                                }
                            }
                        } catch (Exception e) {
                            Log.e("net error", "" + e);
                            avi.showToast(ERROR, e.toString());
                        }

                        avi.loadArticle(content, urlImageParser);//显示文章

                        try {
                            Object a = Remote.article.method("getAttachments", DownFileModel.class).call(articleid);
                            if (a instanceof List) {
                                fileList = (List<DownFileModel>) a;
                            }
                        } catch (Exception e) {
                            Log.e("net error", "" + e);
                            avi.showToast(ERROR, e.toString());
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
     * @param
     */
    private Set<String> getImgStr(String htmlStr) {
        HashSet pics = new HashSet<String>();
        String img = "";
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
                if (!m.group(1).startsWith("http:"))//图片为表情就跳过
                    pics.add(m.group(1));
            }
        }
        return pics;
    }

    class URLImageParser implements Html.ImageGetter {
        private TextView mTextView;

        public URLImageParser(TextView mTextView) {
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
