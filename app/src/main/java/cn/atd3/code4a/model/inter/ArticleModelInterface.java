package cn.atd3.code4a.model.inter;

/**
 * Created by harry on 2018/1/14.
 */

public interface ArticleModelInterface {
    Integer getId() ;

    void setId(Integer id) ;

    String getTitle() ;

    void setTitle(String title) ;

    String getSlug() ;

    void setSlug(String slug) ;

    Integer getUser() ;

    void setUser(Integer user) ;

    Integer getCreate();
    void setCreate(Integer create) ;

    Integer getModify() ;

    void setModify(Integer modify) ;

    Integer getCategory() ;

    void setCategory(Integer category) ;
    String getAbstract();

    void setAbstract(String mAbstract) ;
    Integer getCover() ;

    void setCover(Integer cover) ;

    Integer getViews() ;

    void setViews(Integer views) ;

    Integer getStatus() ;

    void setStatus(Integer status) ;
    String getContent() ;

    void setContent(String content) ;

    String getVisibility();

    void setVisibility(String visibility);
    String getVisibilityPassword() ;

    void setVisibilityPassword(String visibilityPassword) ;
}
