package cn.qingyuyu.code4a.sign.view;

import cn.qingyuyu.code4a.R;
import cn.qingyuyu.code4a.sign.model.SignModel;
import cn.qingyuyu.code4a.sign.mvpbase.BaseActivity;
import cn.qingyuyu.code4a.sign.mvpbase.BaseView;
import cn.qingyuyu.code4a.sign.presenter.impl.SignupPresenter;
import cn.qingyuyu.code4a.sign.presenter.interfaces.SignupContract;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/12   9:37
 **/
public class SignupActivity extends BaseActivity<SignModel,SignupPresenter> implements SignupContract.View {
    @Override
    public void showErrorWithStatus(String msg) {

    }

    @Override
    public void userNameError(String message) {

    }

    @Override
    public void emailError(String message) {

    }

    @Override
    public void passwordError(String message) {

    }

    @Override
    public void passwordDifferent(String message) {

    }

    @Override
    public void remoteError(String message) {

    }

    @Override
    public void signupSuccessful() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }
}
