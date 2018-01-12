package cn.qingyuyu.code4a.sign.presenter.interfaces;

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
 * 创建时间：2018/01/10   20:42
 **/
public interface SigninContract {
    interface View extends BaseView{
        void accountError(String message);
        void passwordError(String message);
        void remoteError(String message);
        void signinSuccessful();
    }

    interface Model extends BaseModel{
        Observable<Object> signin(String account, String password, Boolean remember, String code);
    }

    abstract class Presenter extends BasePresenter<View,Model>{
        public abstract void signinButtonClick(String account,String password);
    }
}
