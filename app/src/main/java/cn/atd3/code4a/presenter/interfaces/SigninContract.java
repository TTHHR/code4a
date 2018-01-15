package cn.atd3.code4a.presenter.interfaces;

import cn.atd3.code4a.model.model.SignModel;
import cn.atd3.code4a.mvpbase.BasePresenter;
import cn.atd3.code4a.mvpbase.BaseView;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/10   20:42
 **/
public interface SigninContract {
    interface View extends BaseView {
        void accountError(String message);
        void passwordError(String message);
        void codeError(String message);
        void remoteError(String message);
        void signinSuccessful();
    }

    abstract class Presenter extends BasePresenter<View,SignModel> {
        public abstract void signinButtonClick(String account,String password);
    }
}
