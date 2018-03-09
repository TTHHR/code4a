package cn.atd3.code4a.presenter.interfaces;

import cn.atd3.code4a.mvpbase.BaseModel;
import cn.atd3.code4a.mvpbase.BasePresenter;
import cn.atd3.code4a.mvpbase.BaseView;
import rx.Observable;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/09   9:18
 **/
public interface SigninUserContract {
    interface Model extends BaseModel{
        Observable<Boolean> setEmail(String email);
        Observable<Boolean> setAvatar(String avatar);
        Observable<Boolean> setPassword(String password);
    }

    interface View extends BaseView{
        void setEmailSuccessful(String message);
        void setAvatarSuccessful(String message);
        void setPasswordSuccessful(String message);
    }

    abstract class Presenter extends BasePresenter<Model,View> {
        public abstract void changeEmail(String email);
        public abstract void changeAvatar(String avatar);
        public abstract void changePassword(String password);
    }
}
