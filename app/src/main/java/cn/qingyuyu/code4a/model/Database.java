package cn.qingyuyu.code4a.model;

/**
 * Created by harrytit on 2017/10/10.
 */

public abstract class Database {
    private String tableName = "";
    private int id = 99999;

    Database(String tableName) {
        this.tableName = tableName;
    }

    public String getTabltName() {
        return tableName;
    }

    public abstract String getCreate();
    private static final String CREATE_TBL = " create table "
            + " Article(_id integer primary key autoincrement,name text,url text, desc text) ";
}
