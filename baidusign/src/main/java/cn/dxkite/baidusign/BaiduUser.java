package cn.dxkite.baidusign;

/**
 * 百度用户信息
 * Created by DXkite on 2017/11/5 0005.
 */

public class BaiduUser {
    Integer user,uid;
    String uname,portrait;

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    @Override
    public String toString() {
        return "BaiduUser{" +
                "user=" + user +
                ", uid=" + uid +
                ", uname='" + uname + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
    }
}
