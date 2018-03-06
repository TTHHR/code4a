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
//todo 处理错误
public class SigninPresenter extends SigninContract.Presenter {
    private static final int INVALID_CODE=-1;
    private static final int INVALID_ACCOUNT=-2;
    private static final int INVALID_PASSWORD=-3;

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
                    mView.codeError("");
                }
            }else {
                mView.passwordError(mContext.getResources().getString(R.string.password_empty));
            }
        }else {
            mView.accountError(mContext.getResources().getString(R.string.account_empty));
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
                case INVALID_ACCOUNT:
                    mView.accountError(mContext.getString(R.string.account_invalid));
                    break;
                case INVALID_PASSWORD:
                    mView.passwordError(mContext.getString(R.string.password_invalid));
                    break;
                case INVALID_CODE:
                    mView.codeError(mContext.getResources().getString(R.string.code_invalid));
                    break;
                default:
                    mView.showErrorWithStatus(mContext.getString(R.string.error)+String.valueOf(i));
            }
        }else {
            //登陆成功
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
