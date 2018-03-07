package cn.atd3.code4a.model.model;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/07   15:10
 **/
public class User {
//    {"email":"2369015621@qq.com",
//     "id":"4",
//     "name":"yglll",
//     "signupTime":"1520356893"
//    }

    private String email;
    private String id;
    private String name;
    private String signupTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignupTime() {
        return signupTime;
    }

    public void setSignupTime(String signupTime) {
        this.signupTime = signupTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", signupTime='" + signupTime + '\'' +
                '}';
    }
}
