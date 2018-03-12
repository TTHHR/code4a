package cn.atd3.code4a.presenter.interfaces;

import cn.atd3.code4a.model.model.User;
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
        Observable<Boolean> setPassword(String oldPwd,String newPwd);
        Observable<User> getUserInfo();
    }

    interface View extends BaseView{
        void setEmailSuccessful();
        void setAvatarSuccessful(String message);
        void setPasswordSuccessful(String message);
        void showProgressDialog();
        void closeProgressDialog();
        void onUserInfoRefresh(User user);
    }

    abstract class Presenter extends BasePresenter<Model,View> {
        public abstract void changeEmail(String email);
        public abstract void changeAvatar(String avatar);
        public abstract void changePassword(String oldPwd,String newPwd,String pwdConfirm);
        public abstract void setUserInfo();
    }
}
