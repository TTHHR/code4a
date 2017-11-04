package cn.qingyuyu.code4droid.remote.bean;

/**
 * 用户信息包，主要用来获取从服务器上获取的数据，并转换为JAVA支持的数据对象
 * Created by DXkite on 2017/11/4 0004.
 */

public class UserInfo {
    protected String email;
    protected Integer group_id;
    protected String name;
    protected Integer id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email='" + email + '\'' +
                ", group_id=" + group_id +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
