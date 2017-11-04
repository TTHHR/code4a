package cn.qingyuyu.code4droid.remote.bean;

/**
 * 百度用户信息包
 * Created by DXkite on 2017/11/4 0004.
 */

public class BaiduUserInfo {
    /**
     * 用户ID
     */
    Integer user;
    /**
     * 百度UID
     */
    Integer uid;

    /**
     * 百度用户名
     */
    String uname;

    /**
     *  百度头像HASH
     */
    String portrait;

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
        return "BaiduUserInfo{" +
                "user=" + user +
                ", uid=" + uid +
                ", uname='" + uname + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
    }
}
