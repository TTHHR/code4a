package cn.atd3.code4a.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import cn.atd3.code4a.R;
import cn.atd3.code4a.presenter.interfaces.SigninContract;
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
    private static final int NAME_FORMAT_ERROR=-1;
    private static final int EMAIL_FORMAT_ERROR=-2;
    private static final int NAME_EXISTS_ERROR=-3;
    private static final int EMAIL_EXISTS_ERROR=-4;
    private static final int ACCOUNT_OR_PASSWORD_ERROR=-5;
    private static final int USER_FREEZED=-6;
    private static final int HUMAN_CODE_ERROR=-7;
    private static final int INVITE_CODE_ERROR=-8;

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

    private void commit(){
        //提交
        mModel.signin(account,password,false,code).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorWithStatus(e.getMessage());
            }

            //在Model中的subscriber.onNext(o);就是调用的这个方法
            @Override
            public void onNext(Integer i) {
                disposeStatus(i);
            }
        });
    }

    private void disposeStatus(int i){
        if(i<0){
            //失败
            switch (i){
                case NAME_FORMAT_ERROR:
                    mView.accountError(mContext.getString(R.string.name_format_error));
                    break;
                case EMAIL_FORMAT_ERROR:
                    mView.accountError(mContext.getString(R.string.email_format_error));
                    break;
                case NAME_EXISTS_ERROR:
                    mView.showErrorWithStatus(mContext.getString(R.string.name_exists_error));
                    break;
                case EMAIL_EXISTS_ERROR:
                    mView.showErrorWithStatus(mContext.getString(R.string.email_exists_error));
                    break;
                case ACCOUNT_OR_PASSWORD_ERROR:
                    mView.showErrorWithStatus(mContext.getString(R.string.account_or_password_error));
                    break;
                case USER_FREEZED:
                    mView.showErrorWithStatus(mContext.getString(R.string.user_freezed));
                    break;
                case HUMAN_CODE_ERROR:
                    mView.codeError(mContext.getString(R.string.human_code_error));
                    break;
                case INVITE_CODE_ERROR:
                    mView.showErrorWithStatus(mContext.getString(R.string.invite_code_error));
                    break;
                default:
                    mView.showErrorWithStatus(mContext.getString(R.string.unknown_error)+i);
            }
            mView.refreshCodeImg();
        }else {
            //注册成功
            mView.signinSuccessful();
        }
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
}
