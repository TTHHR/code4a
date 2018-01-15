package cn.atd3.code4a.presenter;

import android.text.TextUtils;
import android.util.Log;

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
    private static final String TAG = "SigninPresenter";
    private static final int INVALID_CODE=-1;
    private static final int INVALID_ACCOUNT=-2;
    private static final int INVALID_PASSWORD=-3;

    private String account;
    private String password;
    @Override
    public void signinButtonClick(String account,String password) {
        this.account=account;
        this.password=password;
        //检查用户名
        if (checkAccount()){
            //检查密码
            if (checkPassword()){
                //提交
                mModel.signin(account,password,false,null).subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,"onError(Throwable e) e:"+e);
                    }

                    @Override
                    public void onNext(Object o) {
                        //在Model中的subscriber.onNext(o);就是调用的这个方法
                        Integer i=Integer.valueOf(o.toString());
                        disposeStatus(i);
                    }
                });
            }else {
                mView.passwordError(mContext.getResources().getString(R.string.password_empty));
            }
        }else {
            mView.accountError(mContext.getResources().getString(R.string.account_empty));
        }
    }

    private void disposeStatus(int i){
        if(i<0){
            //失败
            switch (i){
                case INVALID_ACCOUNT:
                    mView.accountError(mContext.getResources().getString(R.string.account_invalid));
                    break;
                case INVALID_PASSWORD:
                    mView.passwordError(mContext.getResources().getString(R.string.password_invalid));
                    break;
                case INVALID_CODE:
                    mView.codeError(mContext.getResources().getString(R.string.code_invalid));
                    break;
                default:
                    mView.remoteError(mContext.getResources().getString(R.string.remote_error)+String.valueOf(i));
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
}
