package cn.atd3.code4a.model.model;

import java.io.Serializable;

/**
 * 用户Model
 * Created by DXkite on 2018/2/26 0026.
 */

public class UserModel  implements Serializable{

    private static final long serialVersionUID = 1546093273255439889L;
    private int id;
    private String avatar;
    private String name;
    private String url;

    public UserModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
