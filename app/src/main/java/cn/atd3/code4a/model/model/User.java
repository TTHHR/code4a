package cn.atd3.code4a.model.model;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/07   15:10
 **/
public class User {
    private String account;
    private Boolean signin;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getSignin() {
        return signin;
    }

    public void setSignin(Boolean signin) {
        this.signin = signin;
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", signin=" + signin +
                '}';
    }
}
