package cn.atd3.code4a.presenter;

import android.text.TextUtils;

import cn.atd3.code4a.R;
import cn.atd3.code4a.SigninUserManager;
import cn.atd3.code4a.model.model.User;
import cn.atd3.code4a.presenter.interfaces.SigninUserContract;
import rx.Subscriber;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/09   9:43
 **/
public class SigninUserPresenter extends SigninUserContract.Presenter {
    private String oldPwd;
    private String newPwd;
    private String pwdConfirm;

    @Override
    public void changeEmail(String email) {
        mModel.setEmail(email).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorWithStatus(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean){
                    mView.showErrorWithStatus(mContext.getString(R.string.change_failure));
                }else {
                    mView.setEmailSuccessful();
                }
            }
        });
    }

    @Override
    public void changeAvatar(String avatar) {

    }

    @Override
    public void changePassword(String oldPwd,String newPwd,String pwdConfirm) {
        this.oldPwd=oldPwd;
        this.newPwd=newPwd;
        this.pwdConfirm=pwdConfirm;
        if(!checkOldPassword()){
            mView.showErrorWithStatus(mContext.getString(R.string.password_empty));
            return;
        }
        if(!checkNewPassword()){
            mView.showErrorWithStatus(mContext.getString(R.string.password_empty));
            return;
        }
        if(!checkPasswordDiff()){
            mView.showErrorWithStatus(mContext.getString(R.string.password_diff));
            return;
        }
        mModel.setPassword(oldPwd,newPwd).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorWithStatus(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean){
                    mView.showErrorWithStatus(mContext.getString(R.string.change_failure));
                }else {
                    mView.setPasswordSuccessful("");
                }
            }
        });
    }

    @Override
    public void setUserInfo() {
        mModel.getUserInfo().subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorWithStatus(e.getMessage());
            }

            @Override
            public void onNext(User user) {
                mView.onUserInfoRefresh(user);
            }
        });
    }

    private Boolean checkOldPassword(){
        return !TextUtils.isEmpty(oldPwd);
    }
    private Boolean checkNewPassword(){
        return !TextUtils.isEmpty(newPwd);
    }
    private Boolean checkPasswordDiff(){
        if(newPwd.equals(pwdConfirm)){
            return true;
        }else {
            return false;
        }
    }
}
