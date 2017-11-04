package cn.qingyuyu.code4a.remote.bean;

/**
 * Created by DXkite on 2017/11/4 0004.
 */

public class Article {
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

    @Override
    public String toString() {
        return "Article {" +
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
                '}';
    }
}
