package cn.atd3.code4a.presenter;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

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
import cn.atd3.code4a.model.model.FileListModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.view.inter.EditArticleActivityInterface;
import cn.atd3.proxy.Param;
import cn.qingyuyu.commom.service.FileDealService;
import cn.qingyuyu.zip.DirTraversal;
import cn.qingyuyu.zip.ZipHelper;
import sun.misc.BASE64Encoder;

import static cn.atd3.code4a.Constant.ERROR;

/**
 * Created by harry on 2018/1/17.
 */

public class EditArticlePresenter {

    EditArticleActivityInterface eai;

    ArticleModel article;

    public EditArticlePresenter( EditArticleActivityInterface eai)
    {
        this.eai=eai;
        article=new ArticleModel();
    }
    public void setArticleCategory(int c)
    {
        article.setCategory(c);
    }
    public void setArticleVisibility(String visibility)
    {
        article.setVisibility(visibility);
    }

    public void setArticleTitle(String title)
    {
        article.setTitle(title);
        article.setSlug(title);
    }
    public void setArticleId(int id)
    {
        article.setId(id);
    }
    public void setArticlePassword(String passwd)
    {
        article.setVisibilitypassword(passwd);
    }
    public void setArticleContent(String c)
    {
        article.setContent(c);
        if(c.length()>40)
            article.setAbstract(c.substring(0,40));
        else
            article.setAbstract(c);
    }
    public void setArticleCreateTime(int time)
    {
        article.setCreate(time);//创建时间
    }
    public void setArticleModifyTime()
    {
        article.setModify((int)System.currentTimeMillis()/1000);//修改时间
    }
    public void checkArticleInfo()
    {
        if(article.getTitle()==null||article.getSlug()==null)
        {
            eai.showToast(ERROR,eai.getXmlString(R.string.error_title));
            return;
        }
         if(article.getCategory()==null)
        {
            article.setCategory(1);//默认分类
        }
         if(article.getVisibility()==null)
        {
            article.setVisibility("public");//默认公开
        }
         if(article.getCreate()==null)
        {
            article.setCreate( (int)System.currentTimeMillis()/1000);//创建时间
        }
         if(article.getVisibility().equals("password"))
         {
             if(article.getVisibilitypassword()==null)
             {
                    eai.showToast(ERROR,eai.getXmlString(R.string.password_empty));
                    return;
             }
         }
         if(article.getStatus()==null)
         {
             article.setStatus(2);//默认为发布状态
         }

         eai.dismissArticleInfoDialog();//检查无误
    }

    public void uploadArticle()
    {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        eai.prgoressOfUpload(eai.getXmlString(R.string.action_packfile));

                        if(!articleToXml())
                        {
                            eai.showToast(ERROR,"error");
                            eai.dismissArticleInfoDialog();
                            return;
                        }
                        else if(!packageFile())
                        {
                            eai.showToast(ERROR,"error");
                            eai.dismissArticleInfoDialog();
                            return;
                        }
                        else
                        {
                            eai.prgoressOfUpload(eai.getXmlString(R.string.title_upfile));//进入上传阶段

                            //上传
                            try {

                                Remote.article.method("upload").call(
                                        new Param("article", new File(Constant.getPublicFilePath()+Constant.zipFile)),
                                        new  Param("type", "xml"),
                                        new Param("status", 2)
                                );


                                FileListModel.getIns().clear();//清除

                                eai.prgoressOfUpload(eai.getXmlString(R.string.action_upfinish));

                                Thread.sleep(1000);



                            }
                            catch (Exception e)
                            {
                                Log.e("upload",""+e);
                                eai.showToast(ERROR,e.toString());
                            }

                            eai.dismissArticleInfoDialog();

                        }

                    }
                }
        ).start();

        
    }
    private boolean articleToXml()

    {
        File userDir = new File(Constant.getPublicFilePath());

         File xmlFile =new File(userDir.getAbsolutePath() + "/index.xml");
        if (!userDir.exists())
            try {
                userDir.mkdir();
            } catch (Exception e){
       eai.showToast(ERROR,e.toString());
        return false;
    }

        if (!xmlFile.exists())
            try {
                xmlFile.createNewFile();
            } catch (Exception e){
        eai.showToast(ERROR,e.toString());
        return false;
    }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            BASE64Encoder base64 = new BASE64Encoder();
            document.setXmlStandalone( true);
            Element articles = document.createElement("article");
            Element attrs = document.createElement("attrs");
            Element title = document.createElement("attr");
            Element  slug = document.createElement("attr");
            Element  category = document.createElement("attr");
            Element  tag = document.createElement("attr");
            Element  create = document.createElement("attr");
            Element  modify = document.createElement("attr");
            Element visibility = document.createElement("attr");
            Element abstracts = document.createElement("attr");
            Element status = document.createElement("attr");
            Element cover = document.createElement("attr");

            title.setAttribute("name", "title");
            title.setTextContent(base64.encode(article.getTitle().getBytes()));

            slug.setAttribute("name", "slug");
            slug.setTextContent(base64.encode(article.getSlug().getBytes()));
            category.setAttribute("name", "category");
            category.setAttribute("value", "" + article.getCategory());
            tag.setAttribute("name", "tag");
            create.setAttribute("name", "create");
            create.setAttribute("value", "" + article.getCreate());
            modify.setAttribute("name", "modify");
            modify.setAttribute("value", "" + article.getModify());
            visibility.setAttribute("name", "visibility");
            visibility.setAttribute("value", article.getVisibility());
            visibility.setAttribute("password", article.getVisibilitypassword());
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
            content.setTextContent(base64.encode(article.getContent().getBytes()));

            articles.appendChild(content);

             Element attachments = document.createElement("attachments");

            for (int i =0;;i++) {
                String filepath=FileListModel.getIns().get(i);

                if(filepath==null)
                {
                    break;//退出循环
                }
                 Element attarchment = document.createElement("attachment");
                attarchment.setAttribute("name", filepath.substring(filepath.lastIndexOf("/") + 1));
                attarchment.setAttribute("src", "attarchment/" + filepath.substring(filepath.lastIndexOf("/") + 1));
                attarchment.setAttribute("visibility", "public");
                attachments.appendChild(attarchment);
            }
            articles.appendChild(attachments);

            document.appendChild(articles);

            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (Exception e)
        {
            eai.showToast(ERROR,e.toString());
            return false;
        }
        return true;
    }

    private boolean packageFile()
    {
         FileDealService fds= FileDealService.getInstance();
        File zipDir = new File(Constant.getPublicFilePath()+Constant.zipDir);
        if (!zipDir.exists())
            try {
                zipDir.mkdir();

            } catch (Exception e){
                eai.showToast(ERROR,e.toString());
                return false;
            }
        else//文件夹存在，可能有其它文件
        {
            try {
            fds.delFolder(zipDir.getPath());
            zipDir.mkdir();
            } catch (Exception e){
                eai.showToast(ERROR,e.toString());
                return false;
            }
        }

        fds.copyFile(Constant.getPublicFilePath()+"/index.xml",zipDir.getAbsolutePath()+"/index.xml");//复制xml

         File zipFile= new File(Constant.getPublicFilePath()+Constant.zipFile);   //zip

        if(zipFile.exists())
            try {
                zipFile.delete();
            } catch ( Exception e) {
                eai.showToast(ERROR,e.toString());
        return false;
    }
        File assetsDir =new  File(zipDir.getAbsolutePath()+"/assets");

        try {
            assetsDir.mkdir();
        } catch ( Exception e) {
            eai.showToast(ERROR,e.toString());
        return false;
    }
         File attachDir = new File(zipDir.getAbsolutePath()+"/attachment");

        try {
            attachDir.mkdir();
            for (int i=0;;i++) {
                String f=FileListModel.getIns().get(i);
                if(f==null)
                    break;
                fds.copyFile(f, attachDir.getAbsolutePath() + "/" + f.substring(f.lastIndexOf("/") + 1));
            }
        } catch (Exception e) {
       eai.showToast(ERROR,e.toString());
        return false;
    }
        ArrayList<File> zipfiles = DirTraversal.arrayListFiles(zipDir.getAbsolutePath());
        for(File f : zipfiles)
            Log.e("files",f.getAbsolutePath());
        try {
            ZipHelper.zipFiles(zipfiles, zipFile);
        }
        catch (Exception e){
        return false;
    }
        return true;
    }

}
