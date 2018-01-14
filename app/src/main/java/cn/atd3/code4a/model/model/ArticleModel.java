package cn.atd3.code4a.model.model;

import cn.atd3.code4a.model.inter.ArticleModelInterface;

/**
 * Created by DXkite on 2017/11/4 0004.
 */

public class ArticleModel implements ArticleModelInterface{
    Integer id;
    String title;
    String slug;
    Integer user;
    Integer create;
    Integer modify;
    Integer category;
    String mAbstract;
    Integer cover;
    Integer  views;
    Integer status;
    String content;
    String visibility;
    String visibilitypassword;

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

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
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

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
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
    public String getVisibilitypassword() {
        return visibilitypassword;
    }

    public void setVisibilitypassword(String visibilitypassword) {
        this.visibilitypassword = visibilitypassword;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", user=" + user +
                ", create=" + create +
                ", modify=" + modify +
                ", category=" + category +
                ", abstract='" + mAbstract + '\'' +
                ", cover=" + cover +
                ", views=" + views +
                ", status=" + status +
                ", content='" + content + '\'' +
                '}';
    }
}
