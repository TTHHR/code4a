package cn.atd3.code4a.model.model;

/**
 * Created by harry on 2018/1/22.
 */

public class DownFileModel {
    private int id;
    private String type;
    private String url;
    private String fileName;
    private String visibility;
    private String attachment;
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
    @Override
    public String toString() {
        return "DownFileModel{" +
                "id=" + id +
                " ,name='" + fileName + '\'' +
                " ,type='" + type  +'\'' +
                " ,size=" + size   +
                " ,time=" + time   +
                " ,visibility='" + visibility + '\'' +
                " ,attachment='" + attachment + '\'' +
                ",url='" + url + '\'' +
                '}';
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
