package cn.atd3.code4a.model.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dxkite.common.StorageData;

/**
 * 分类模型
 * Created by DXkite on 2018/1/15 0015.
 */

public class CategoryModel extends StorageData {

    private static final long serialVersionUID = 0x60d4b6bc997429e1L;
    private static Map<Integer,CategoryModel> categoryModelMap=new HashMap<>();
    private int id;
    private String name;
    private String slug;
    private int count;
    private int parent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        categoryModelMap.put(id,this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", count=" + count +
                ", parent=" + parent +
                '}';
    }

    public static CategoryModel getById(int id){
        return categoryModelMap.get(id);
    }

    public static Map<Integer, CategoryModel> getCategories() {
        return categoryModelMap;
    }
}
