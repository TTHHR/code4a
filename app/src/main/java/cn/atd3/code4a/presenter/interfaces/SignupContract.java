package cn.atd3.code4a.presenter.interfaces;

import java.io.File;

import cn.atd3.code4a.model.SignModel;
import cn.atd3.code4a.model.inter.Sign;
import cn.atd3.code4a.mvpbase.BasePresenter;
import cn.atd3.code4a.mvpbase.BaseView;

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
        void codeError(String message);
        void signupSuccessful();
        void showProgressDialog();
        void closeProgressDialog();
        void showCodeImg(File file);
    }

    abstract class Presenter extends BasePresenter<Sign,View> {
        public abstract void signupButtonClick(String user,String email,String password,String passwordConfirm,String code);
        public abstract void updateCode();
    }
}
