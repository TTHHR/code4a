package cn.atd3.code4a.model.model;

import cn.atd3.code4a.model.inter.MessageModelInterface;

/**
 * Created by harry on 2018/1/13.
 */

public class MessageModel implements MessageModelInterface {
    private String message=null;
    private String url=null;

    @Override
    public String getMessge() {
        return message;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setMessge(String text) {
            message=text;
    }

    @Override
    public void setUrl(String url) {
        this.url=url;
    }
}
