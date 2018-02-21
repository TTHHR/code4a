package cn.atd3.code4a.model.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.atd3.code4a.model.inter.ArticleModelInterface;

/**
 * 文章模块
 * Created by DXkite on 2017/11/4 0004.
 */

public class ArticleModel implements ArticleModelInterface, Serializable {
    private Integer id;
    private String title;
    private String slug;
    private Integer userId;
    private String user;
    private Integer create;
    private Integer modify;
    private Integer categoryId;
    private String category;
    private String mAbstract;
    private Integer cover;
    private Integer views;
    private Integer status;
    private String content;
    private String visibility;
    private String visibilityPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getCreate() {
        return create;
    }

    public void setCreate(Integer create) {
        this.create = create;
    }

    public Integer getModify() {
        return modify;
    }

    public void setModify(Integer modify) {
        this.modify = modify;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getAbstract() {
        return mAbstract;
    }

    public void setAbstract(String mAbstract) {
        this.mAbstract = mAbstract;
    }

    public Integer getCover() {
        return cover;
    }

    public void setCover(Integer cover) {
        this.cover = cover;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getVisibilityPassword() {
        return visibilityPassword;
    }

    public void setVisibilityPassword(String visibilityPassword) {
        this.visibilityPassword = visibilityPassword;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", user='" + user + '\'' +
                ", userId=" + userId +
                ", create=" + create +
                ", modify=" + modify +
                ", category=" + category +
                ", categoryId=" + categoryId +
                ", abstract='" + mAbstract + '\'' +
                ", cover=" + cover +
                ", views=" + views +
                ", status=" + status +
                ", content='" + content + '\'' +
                '}';
    }


    public static String time(int unix) {
        long time = unix * 1000;
        String showTime;
        String hTime = new SimpleDateFormat("H:mm", Locale.getDefault()).format(time);
        String rTime = new SimpleDateFormat("M-d H:mm", Locale.getDefault()).format(time);
        long passTime = (System.currentTimeMillis() - time) / 1000;
        if (passTime < 60) {
            showTime = "刚刚";
        } else if (passTime < 60 * 60) {
            showTime = ((int) Math.floor(passTime / 60)) + "分钟前";
        } else if (passTime < 60 * 60 * 24) {
            showTime = ((int) Math.floor(passTime / (60 * 60))) + "小时前 " + hTime;
        } else if (passTime < 60 * 60 * 24 * 3) {
            double day = Math.floor(passTime / (60 * 60 * 24));
            if (day == 1) {
                showTime = "昨天 " + hTime;
            } else {
                showTime = "前天 " + hTime;
            }
        } else {
            showTime = rTime;
        }
        return showTime;
    }

    public static String category(int id) {
        CategoryModel model = CategoryModel.getById(id);
        if (model == null) {
            return "默认分类";
        } else {
            return model.getName();
        }
    }
}
