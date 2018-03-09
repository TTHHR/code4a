package cn.atd3.code4a.model;

import cn.atd3.code4a.presenter.interfaces.SigninUserContract;
import rx.Observable;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/09   9:42
 **/
public class SigninUserModel implements SigninUserContract.Model{
    @Override
    public Observable<Boolean> setEmail(String email) {
        return null;
    }

    @Override
    public Observable<Boolean> setAvatar(String avatar) {
        return null;
    }

    @Override
    public Observable<Boolean> setPassword(String password) {
        return null;
    }
}
