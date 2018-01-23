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
import cn.atd3.code4a.model.model.FileListModel;
import cn.atd3.code4a.model.model.PictureListModel;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.util.UriRealPath;
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
        article.setVisibilityPassword(passwd);
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
             if(article.getVisibilityPassword()==null)
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


    /**
     * 得到网页中图片的地址
     * @param
     */
    private Set<String> getImgStr(String htmlStr)  {
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
                if(m.group(1).startsWith("content:")||m.group(1).startsWith("file:"))//图片
                    pics.add(m.group(1));
            }
        }
        return pics;
    }
    private void getPictureList(Context context)
    {
        Log.e("get picture list",article.getContent());
        Set<String> ss=getImgStr(article.getContent());

        String c=article.getContent();

        for(String s:ss)
        {
            Uri u=Uri.parse(s);
            String realPath=UriRealPath.getRealPathFromUri(context,u);//将Uri转换成文件路径，方便打包
            PictureListModel.getIns().addPicture(realPath);
            Log.e("uri to path","uri:"+s+"  path:"+realPath);
            c=c.replace(s,"assets/"+new File(realPath).getName());//将文章中的Uri换成服务器路径
        }

        article.setContent(c);//将替换后的文章重新赋值给Article

    }

    public void uploadArticle(final Context context)
    {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        eai.prgoressOfUpload(eai.getXmlString(R.string.action_packfile));


                        getPictureList(context);



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

                                File f=new File(Constant.getPublicFilePath()+Constant.zipFile);

                                if(f.length()>2048000)
                                {
                                    eai.showToast(Constant.INFO,String.format(eai.getXmlString(R.string.info_upload_slow),f.length()/1000000+"M"));
                                }

                                Remote.article.method("upload").call(
                                        new Param("article",f ),
                                        new  Param("type", "xml"),
                                        new Param("status", 2)
                                );


                                eai.prgoressOfUpload(eai.getXmlString(R.string.action_upfinish));

                                Thread.sleep(1000);

                            }
                            catch (Exception e)
                            {
                                Log.e("upload",""+e);
                                eai.showToast(ERROR,e.toString());
                            }
                            finally {
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
            visibility.setAttribute("password", article.getVisibilityPassword());
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
            Log.e("xml content",article.getContent());
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
            for (int i=0;;i++) {
                String f=PictureListModel.getIns().get(i);
                if(f==null)
                    break;
                fds.copyFile(f, assetsDir.getAbsolutePath() + "/" + f.substring(f.lastIndexOf("/") + 1));
            }
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
