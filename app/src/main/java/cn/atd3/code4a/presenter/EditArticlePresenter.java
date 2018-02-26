package cn.atd3.code4a.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.model.model.CategoryModel;
import cn.atd3.code4a.model.model.FileListModel;
import cn.atd3.code4a.model.model.PictureListModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.util.ReplaceCHeadInHtml;
import cn.atd3.code4a.util.UriRealPath;
import cn.atd3.code4a.view.inter.EditArticleActivityInterface;
import cn.atd3.proxy.Param;
import cn.qingyuyu.commom.service.FileDealService;
import cn.qingyuyu.zip.DirTraversal;
import cn.qingyuyu.zip.ZipHelper;
import sun.misc.BASE64Encoder;
import top.zibin.luban.Luban;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.WARNING;

/**
 * Created by harry on 2018/1/17.
 */

public class EditArticlePresenter {

    private EditArticleActivityInterface eai;

    private ArticleModel article;

    private boolean editModel = false;

    public EditArticlePresenter(EditArticleActivityInterface eai) {
        this.eai = eai;
        article = new ArticleModel();
    }

    public void setArticleCategoryId(int c) {
        article.setCategory (new CategoryModel(c));
    }

    public boolean isEditModel() {
        return editModel;
    }

    public void setArticleVisibility(String visibility) {
        article.setVisibility(visibility);
    }

    public void setArticleTitle(String title) {
        article.setTitle(title);
        article.setSlug(title);
    }

    public void setArticle(ArticleModel article) {
        this.article = article;
        editModel = true;
    }

    public void setArticlePassword(String passwd) {
        article.setVisibilityPassword(passwd);
    }

    public void setArticleContent(String c) {
        article.setContent(c);
        if (c.length() > 40)
            article.setAbstract(c.substring(0, 40));
        else
            article.setAbstract(c);
    }

    public String getTitle() {
        return article.getTitle();
    }

    private void setArticleModifyTime() {
        article.setModify((int) (System.currentTimeMillis() / 1000));//修改时间
    }

    private boolean checkArticleInfo() {
        if (article.getTitle() == null || article.getSlug() == null) {
            eai.showToast(WARNING, eai.getXmlString(R.string.error_title));
            return false;
        }
        if (article.getCategory() == null) {
            article.setCategory (new CategoryModel(1));//默认分类
        }
        if (article.getVisibility() == null) {
            article.setVisibility("public");//默认公开
        }
        if (article.getCreate() == null) {
            article.setCreate((int) (System.currentTimeMillis() / 1000));//创建时间
        }

        if (article.getVisibility().equals("password")) {
            if (article.getVisibilityPassword() == null) {
                eai.showToast(ERROR, eai.getXmlString(R.string.password_empty));
                return false;
            }
        }
        if (article.getStatus() == null) {
            article.setStatus(2);//默认为发布状态
        }
        if (article.getContent() == null || article.getContent().isEmpty()) {
            eai.showToast(WARNING, eai.getXmlString(R.string.error_content));
            return false;
        }
        setArticleModifyTime();
        return true;
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
                if (m.group(1).startsWith("content:") || m.group(1).startsWith("file:"))//图片
                    pics.add(m.group(1));
            }
        }
        return pics;
    }

    private void getPictureList(Context context) {
        Log.e("get picture list", article.getContent());
        Set<String> ss = getImgStr(article.getContent());
        String c = article.getContent();
        PictureListModel.getIns().clear();
        for (String s : ss) {
            Uri u = Uri.parse(s);
            String realPath = UriRealPath.getRealPathFromUri(context, u);//将Uri转换成文件路径，方便打包
            PictureListModel.getIns().addPicture(realPath);
            Log.e("uri to path", "uri:" + s + "  path:" + realPath);
            c = c.replace(s, "assets/" + new File(realPath).getName());//将文章中的Uri换成服务器路径
        }

        article.setContent(c);//将替换后的文章重新赋值给Article

    }

    public void uploadArticle(final Context context) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        if (!checkArticleInfo()) {
                            //检查文章不通过
                            eai.dismissArticleInfoDialog();
                            return;
                        }
                        eai.progressOfUpload(eai.getXmlString(R.string.action_packfile));


                        getPictureList(context);


                        if (!articleToXml()) {
                            eai.showToast(ERROR, "error");
                            eai.dismissArticleInfoDialog();
                            return;
                        } else if (!packageFile(context)) {
                            eai.showToast(ERROR, "error");
                            eai.dismissArticleInfoDialog();
                            return;
                        } else {
                            eai.progressOfUpload(eai.getXmlString(R.string.title_upfile));//进入上传阶段

                            //上传
                            try {

                                File f = new File(Constant.getPublicFilePath() + Constant.zipFile);

                                if (f.length() > 2048000) {
                                    eai.showToast(Constant.INFO, String.format(eai.getXmlString(R.string.info_upload_slow), f.length() / 1000000 + "M"));
                                }

                                Remote.article.method("upload").call(
                                        new Param("article", f),
                                        new Param("type", "xml"),
                                        new Param("status", 2)
                                );


                                eai.progressOfUpload(eai.getXmlString(R.string.action_upfinish));

                                Thread.sleep(1000);

                            } catch (Exception e) {
                                Log.e("upload", "" + e);
                                eai.showToast(ERROR, e.toString());
                            } finally {
                                FileListModel.getIns().clear();//清除
                                PictureListModel.getIns().clear();
                                eai.dismissArticleInfoDialog();
                            }

                        }

                    }
                }
        ).start();


    }

    private boolean articleToXml()

    {
        File userDir = new File(Constant.getPublicFilePath());

        File xmlFile = new File(userDir.getAbsolutePath() + "/index.xml");
        if (!userDir.exists())
            try {
                userDir.mkdir();
            } catch (Exception e) {
                eai.showToast(ERROR, e.toString());
                return false;
            }

        if (!xmlFile.exists())
            try {
                xmlFile.createNewFile();
            } catch (Exception e) {
                eai.showToast(ERROR, e.toString());
                return false;
            }

        try {

            Log.e("article status", article.toString());


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            BASE64Encoder base64 = new BASE64Encoder();
            document.setXmlStandalone(true);
            Element articles = document.createElement("article");
            Element attrs = document.createElement("attrs");
            Element title = document.createElement("attr");
            Element slug = document.createElement("attr");
            Element category = document.createElement("attr");
            Element tag = document.createElement("attr");
            Element create = document.createElement("attr");
            Element modify = document.createElement("attr");
            Element visibility = document.createElement("attr");
            Element abstracts = document.createElement("attr");
            Element status = document.createElement("attr");
            Element cover = document.createElement("attr");

            title.setAttribute("name", "title");
            title.setTextContent(base64.encode(article.getTitle().getBytes()));

            slug.setAttribute("name", "slug");
            slug.setTextContent(base64.encode(article.getSlug().getBytes()));
            category.setAttribute("name", "category");
            category.setAttribute("value", "" + article.getCategory().getId());
            tag.setAttribute("name", "tag");
            create.setAttribute("name", "create");
            create.setAttribute("value", "" + article.getCreate());
            modify.setAttribute("name", "modify");
            modify.setAttribute("value", "" + article.getModify());
            visibility.setAttribute("name", "visibility");
            visibility.setAttribute("value", article.getVisibility());
            visibility.setAttribute("password", article.getVisibilityPassword() == null ? "code4a" : article.getVisibilityPassword());
            abstracts.setAttribute("name", "abstract");
            abstracts.setTextContent(base64.encode(article.getAbstract().getBytes()));
            status.setAttribute("name", "status");
            status.setAttribute("value", "2");
            cover.setAttribute("name", "cover");
            cover.setAttribute("value", "assets/cover.png");
            attrs.appendChild(title);
            attrs.appendChild(slug);
            attrs.appendChild(category);
            attrs.appendChild(tag);
            attrs.appendChild(create);
            attrs.appendChild(modify);
            attrs.appendChild(visibility);
            attrs.appendChild(abstracts);
            attrs.appendChild(status);
            attrs.appendChild(cover);
            articles.appendChild(attrs);

            Element content = document.createElement("content");
            content.setAttribute("type", "html");

            //替换头文件中的<>字符
            article.setContent(ReplaceCHeadInHtml.getIns().getHtml(article.getContent()));
            Log.e("xml content", article.getContent());
            content.setTextContent(base64.encode(article.getContent().getBytes()));
            Log.e("base64 content", content.getTextContent());
            articles.appendChild(content);

            Element attachments = document.createElement("attachments");
            for (int i = 0; ; i++) {
                String filepath = FileListModel.getIns().get(i);
                if (filepath == null) {
                    break;//退出循环
                }
                Log.e("file to xml", filepath);
                Element attachment = document.createElement("attachment");
                attachment.setAttribute("name", filepath.substring(filepath.lastIndexOf("/") + 1));
                attachment.setAttribute("src", "attachment/" + filepath.substring(filepath.lastIndexOf("/") + 1));
                attachment.setAttribute("visibility", "public");
                attachments.appendChild(attachment);
            }
            articles.appendChild(attachments);


            document.appendChild(articles);


            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (Exception e) {
            Log.e("article to xml", e.toString());
            eai.showToast(ERROR, e.toString());
            return false;
        }
        return true;
    }

    private boolean packageFile(Context c) {
        FileDealService fds = FileDealService.getInstance();
        File zipDir = new File(Constant.getPublicFilePath() + Constant.zipDir);
        if (!zipDir.exists())
            try {
                zipDir.mkdirs();
            } catch (Exception e) {
                eai.showToast(ERROR, e.toString());
                return false;
            }
        else//文件夹存在，可能有其它文件
        {
            try {
                fds.delFolder(zipDir.getPath());
                zipDir.mkdirs();
            } catch (Exception e) {
                eai.showToast(ERROR, e.toString());
                return false;
            }
        }

        fds.copyFile(Constant.getPublicFilePath() + "/index.xml", zipDir.getAbsolutePath() + "/index.xml", true);//复制xml

        File zipFile = new File(Constant.getPublicFilePath() + Constant.zipFile);   //zip

        if (zipFile.exists())
            try {
                zipFile.delete();
            } catch (Exception e) {
                eai.showToast(ERROR, e.toString());
                return false;
            }
        File assetsDir = new File(zipDir.getAbsolutePath() + "/assets");

        try {
            assetsDir.mkdirs();
            for (int i = 0; ; i++) {
                String f = PictureListModel.getIns().get(i);
                if (f == null)
                    break;
                fds.copyFile(f, assetsDir.getAbsolutePath() + "/" + f.substring(f.lastIndexOf("/") + 1), true);
            }
        } catch (Exception e) {
            Log.e("assets", e.toString());
            eai.showToast(ERROR, e.toString());
            return false;
        }
        File attachDir = new File(zipDir.getAbsolutePath() + "/attachment");

        try {
            attachDir.mkdirs();
            for (int i = 0; ; i++) {
                String f = FileListModel.getIns().get(i);
                if (f == null)
                    break;
                fds.copyFile(f, attachDir.getAbsolutePath() + "/" + f.substring(f.lastIndexOf("/") + 1), false);
            }
        } catch (Exception e) {
            Log.e("attach", e.toString());
            eai.showToast(ERROR, e.toString());
            return false;
        }
        ArrayList<File> zipfiles = DirTraversal.arrayListFiles(zipDir.getAbsolutePath());
        for (File f : zipfiles)
            Log.e("files", f.getAbsolutePath());
        try {
            ZipHelper.zipFiles(zipfiles, zipFile);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
