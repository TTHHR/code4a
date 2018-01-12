package cn.qingyuyu.code4a.sign.presenter.interfaces;

import cn.qingyuyu.code4a.sign.mvpbase.BaseModel;
import rx.Observable;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/12   10:24
 **/
public interface Sign extends BaseModel {
    Observable<Object> signin(String account, String password, Boolean remember, String code);
    Observable<Object> signup(String user, String email, String password, String code);
}