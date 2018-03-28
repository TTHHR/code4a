package cn.atd3.code4a.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;

import cn.atd3.code4a.R;
import cn.atd3.code4a.model.SignModel;
import cn.atd3.code4a.net.DisposeErrorSubscriber;
import cn.atd3.code4a.presenter.interfaces.SignupContract;
import rx.Observer;
import rx.Subscriber;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/12   10:19
 **/
public class SignupPresenter extends SignupContract.Presenter {

    private String user;
    private String email;
    private String password;
    private String passwordConfirm;
    private String code;
    @Override
    public void signupButtonClick(String user,String email, String password,String passwordConfirm,String code) {
        this.user=user;
        this.email=email;
        this.password=password;
        this.passwordConfirm=passwordConfirm;
        this.code=code;
        if (checkUser()){
            if(checkEmail()){
                if(checkPassword()){
                    if(checkPasswordDiff()){
                        if(checkCode()){
                            commit();
                        }else {
                            mView.codeError(mContext.getString(R.string.code_empty));
                        }
                    }else {
                        mView.passwordError(mContext.getString(R.string.password_diff));
                    }
                }else {
                    mView.passwordError(mContext.getString(R.string.password_empty));
                }
            }else {
                mView.emailError(mContext.getString(R.string.email_empty));
            }
        }else {
            mView.userNameError(mContext.getString(R.string.account_empty));
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

    public void commit(){
        //提交数据
        mModel.signup(user,email,password,code).subscribe(new DisposeErrorSubscriber<Integer>(mContext) {

            @Override
            public void onThrowableError(Throwable e) {
                mView.closeProgressDialog();
                updateCode();
            }

            @Override
            public void onNext(Integer i) {
                if (i<0){
                    mView.showErrorWithStatus(String.valueOf(i));
                }else {
                    mView.signupSuccessful();
                }
            }
        });
    }

    private Boolean checkUser(){
        return !TextUtils.isEmpty(user);
    }
    private Boolean checkEmail(){
        return !TextUtils.isEmpty(email);
    }
    private Boolean checkPassword(){
        return !TextUtils.isEmpty(password);
    }
    private Boolean checkPasswordDiff(){
        if(password.equals(passwordConfirm)){
            return true;
        }else {
            return false;
        }
    }
    private Boolean checkCode(){
        return !TextUtils.isEmpty(code);
    }
}
