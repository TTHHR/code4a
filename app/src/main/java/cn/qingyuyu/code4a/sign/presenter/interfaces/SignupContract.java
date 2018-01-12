package cn.qingyuyu.code4a.sign.presenter.interfaces;

import cn.qingyuyu.code4a.sign.model.SignModel;
import cn.qingyuyu.code4a.sign.mvpbase.BaseModel;
import cn.qingyuyu.code4a.sign.mvpbase.BasePresenter;
import cn.qingyuyu.code4a.sign.mvpbase.BaseView;
import rx.Observable;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/12   9:47
 **/
public interface SignupContract {
    interface View extends BaseView {
        void userNameError(String message);
        void emailError(String message);
        void passwordError(String message);
        void passwordDifferent(String message);
        void remoteError(String message);
        void signupSuccessful();
    }

    abstract class Presenter extends BasePresenter<View,SignModel> {
        public abstract void signupButtonClick(String user,String email,String password);
    }
}
