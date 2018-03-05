package cn.atd3.code4a.model.model;

/**
 * 附件控制
 * TODO 附件内容保存
 * Created by harry on 2018/1/22.
 */

public class AttachmentFileModel {

    private int id;
    private int article;
    private String type;
    private String url;
    private String fileName;
    private String visibility;
    private AttachmentInfo info;
    private int size;
    private int time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return fileName;
    }

    public void setName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }




    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }


    public AttachmentInfo getInfo() {
        return info;
    }

    public void setInfo(AttachmentInfo info) {
        this.info = info;
    }
}
