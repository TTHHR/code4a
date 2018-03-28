package cn.atd3.code4a.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.File;

import cn.atd3.code4a.R;
import cn.atd3.code4a.SigninUserManager;
import cn.atd3.code4a.model.model.User;
import cn.atd3.code4a.net.DisposeErrorSubscriber;
import cn.atd3.code4a.presenter.interfaces.SigninContract;
import cn.atd3.proxy.exception.ProxyException;
import rx.Subscriber;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：主要负责处理业务逻辑
 * 备注消息：
 * 创建时间：2018/01/10   22:15
 **/
public class SigninPresenter extends SigninContract.Presenter {
    private String account;
    private String password;
    private String code;
    @Override
    public void signinButtonClick(String account,String password,String code) {
        this.account=account;
        this.password=password;
        this.code=code;
        //检查用户名
        if (checkAccount()){
            //检查密码
            if (checkPassword()){
                if(checkCode()){
                    commit();
                }else {
                    mView.codeError(mContext.getString(R.string.code_empty));
                }
            }else {
                mView.passwordError(mContext.getString(R.string.password_empty));
            }
        }else {
            mView.accountError(mContext.getString(R.string.account_empty));
        }
    }

    @Override
    public void updateCode() {
        mModel.getCode().subscribe(new DisposeErrorSubscriber<File>(mContext) {
            @Override
            public void onThrowableError(Throwable e) {
                mView.closeProgressDialog();
                mView.showErrorWithStatus("获取验证码失败");
            }

            @Override
            public void onNext(File file) {
                mView.showCodeImg(file);
            }
        });
    }

    private void commit(){
        //提交
        mModel.signin(account,password,false,code).subscribe(new DisposeErrorSubscriber<Integer>(mContext) {

            @Override
            public void onThrowableError(Throwable e){
                mView.closeProgressDialog();
                updateCode();
            }

            @Override
            public void onNext(Integer integer) {
                if(integer<0){
                    updateCode();
                    mView.showErrorWithStatus("出现错误，返回错误代码："+integer);
                }else {
                    //登陆成功
                    //获取用户信息
                    setUserInfo();
                }
            }
        });
    }

    private Boolean checkAccount(){
        return !TextUtils.isEmpty(account);
    }
    private Boolean checkPassword(){
        return !TextUtils.isEmpty(password);
    }
    private Boolean checkCode(){
        return !TextUtils.isEmpty(code);
    }

    private void setUserInfo(){
        mModel.getUserInfo().subscribe(new DisposeErrorSubscriber<User>(mContext) {
            @Override
            public void onThrowableError(Throwable e){
                mView.closeProgressDialog();
                updateCode();
            }

            @Override
            public void onNext(User user) {
                mView.signinSuccessful(user);
            }
        });
    }
}
