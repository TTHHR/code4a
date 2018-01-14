package cn.atd3.code4a.model.inter;


/**
 * Created by harry on 2018/1/12.
 */

public interface MessageModelInterface {

    //提供数据
    String getMessge();
    String getUrl();

    //存储数据
    void setMessge(String text);
    void setUrl(String url);

}
